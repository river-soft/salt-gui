package org.riversoft.salt.gui.service

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.AuthModule
import org.riversoft.salt.gui.calls.LocalAsyncResult
import org.riversoft.salt.gui.calls.modules.State
import org.riversoft.salt.gui.calls.runner.Jobs
import org.riversoft.salt.gui.client.SaltClient
import org.riversoft.salt.gui.datatypes.target.MinionList
import org.riversoft.salt.gui.datatypes.target.Target
import org.riversoft.salt.gui.domain.Job
import org.riversoft.salt.gui.domain.JobResult
import org.riversoft.salt.gui.domain.JobResultDetail
import org.riversoft.salt.gui.domain.Minion
import org.riversoft.salt.gui.domain.SaltScript
import org.riversoft.salt.gui.repository.JobRepository
import org.riversoft.salt.gui.repository.JobResultDetailRepository
import org.riversoft.salt.gui.repository.JobResultRepository
import org.riversoft.salt.gui.results.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Slf4j
@Service
class ExecuteScriptService {

    //region injection

    @Value('${salt.user}')
    private String USER

    @Value('${salt.password}')
    private String PASSWORD

    @Autowired
    private SaltClient saltClient

    @Autowired
    private JobRepository jobRepository

    @Autowired
    private SaltScriptService saltScriptService

    @Autowired
    private MinionCRUDService minionCRUDService

    @Autowired
    private JobResultRepository jobResultRepository

    @Autowired
    private JobResultDetailRepository jobResultDetailRepository

    //endregion

    /**
     * Выполнение списка скриптов для списка миньонов
     * @param minions - список миньонов
     * @param scripts - список скриптов
     */
    def executeScripts(String[] minions, String[] scripts) {

        //TODO собрать массив названий скриптов на выполнение или же принимать прямо с фронта список из названий?
        //region отправка миньонов и скриптов на выполнение на salt сервер

        log.debug("Sending scripts names [${scripts.join(",")}] and minions names [${minions.join(",")}] for execution to salt server.")

        Target<List<String>> minionList = new MinionList(minions);

        LocalAsyncResult<Map<String, Result<Boolean>>> result = State.apply(scripts).callAsync(
                saltClient, minionList, USER, PASSWORD, AuthModule.PAM);

        log.debug("Successfully returned responce from salt server with job id [${result.jid}].")

        //endregion

        //region список скриптов

        List<SaltScript> saltScripts = []

        for (String script : scripts) {

            saltScripts.add(saltScriptService.getSaltScriptByName(script))
        }

        //endregion

        //region работа

        log.debug("Start creating Job with jid [${result.jid}].")

        Job job = jobRepository.save(new Job(jid: result.jid, done: false, name: saltScripts.collect {
            it.name
        }.join("/")))

        log.debug("Successfully created Job with jid [${result.jid}] and with name [${job.name}].")

        //endregion

        //region результаты скриптов

        for (String minionResult : result.minions) {

            Minion minion = minionCRUDService.getMinionByName(minionResult)

            log.debug("Start creating JobResult for minion [${minion.name}] and job with jid [${job.jid}].")

            JobResult jobResult = new JobResult(minion: minion, job: job, saltScripts: saltScripts)

            jobResultRepository.save(jobResult)

            job.results.add(jobResult)
            jobRepository.save(job)

            log.debug("Successfully created JobResult for minion [${minion.name}] and job with jid [${result.jid}].")
        }

        //endregion
    }

    /**
     * Проверка результатов выполнения работы
     * @param jid - уникальный id работы
     * @return
     */
    def checkJobByJid(String jid) {

        log.debug("Start check Job by jid [${jid}].")

        // возвращает результат по самой job и результаты по миньонам такие как в job
        def jobInfoSalt = Jobs.listJob(jid).callSync(saltClient, USER, PASSWORD, AuthModule.PAM);

        //вовзращает список результатов по миньонам
        //TODO подумать может результат все таки получать из jobInfoSalt что бы не делать два запроса на salt сервер
        def jobResultsSalt = Jobs.lookupJid(jobInfoSalt.jid).callSync(saltClient, USER, PASSWORD, AuthModule.PAM);

        Job job = jobRepository.findOne(jobInfoSalt.jid)

        boolean isDone = false

        // region update job results

        if (jobResultsSalt.size() == 0) {
            log.warn("Result by job with jid [${job.jid}] not found yet.")
        }

        for (def jobResultSalt : jobResultsSalt) {

            Minion minion = minionCRUDService.getMinionByName(jobResultSalt.key)

            List<JobResult> jobResults = jobResultRepository.findAllByMinionIdAndJobJid(minion.id, job.jid)

            for (JobResult jobResult : jobResults) {

                log.debug("Start updating JobResult for minion [${jobResults.minion.name}] and Job jid [${jobResult.job.jid}].")

                for (def jobResultSaltItem : jobResultSalt) {

                    for (def val : jobResultSaltItem.value) {

                        log.debug("Start creating JobResultDetail fom minion [${jobResult.minion.name}] and job " +
                                "id [${jobResult.job.jid}].")

                        JobResultDetail jobResultDetail = new JobResultDetail()

                        jobResultDetail.cmd = val["key"]

                        jobResultDetail.name = val["value"]["name"]
                        jobResultDetail.comment = val["value"]["comment"]
                        jobResultDetail.result = val["value"]["result"]
                        jobResultDetail.duration = val["value"]["duration"] ? val["value"]["duration"] as double : null
                        jobResultDetail.description = val["value"]["__id__"]
                        jobResultDetail.changes = val["value"]["changes"]
                        jobResultDetail.startTime = val["value"]["start_time"]
                        jobResultDetail.jobResult = jobResult

                        jobResult.jobResultDetails.add(jobResultDetail)

                        jobResultDetailRepository.save(jobResultDetail)
                        log.debug("Successfully created JobResultDetail for minion [${jobResult.minion.name}] and job " +
                                "id [${jobResult.job.jid}], result name [${jobResultDetail.name}].")
                    }
                }

                //TODO может проверять если все результаты true ?
                jobResult.isResult = true

                jobResultRepository.save(jobResult)
                log.debug("Finish updating JobResult for minion [${jobResult.minion.name}] and job [${jobResult.job.jid}].")

                //TODO подумать когда отмечать задачу как выполненную
                isDone = true
            }
        }

        // endregion

        //region update job

        job.startTime = jobInfoSalt.startTime.date
        job.user = jobInfoSalt.user

        job.done = isDone

        jobRepository.save(job)

        // endregion

        //job2.result.values().find().members.find().value.members.find().value.members

        log.debug("Finish check Job by jid [${jid}].")

        return jobInfoSalt
    }

    /**
     * Автоматическая проверка и получение результатов выполнения работы скриптов
     */
    @Scheduled(fixedDelayString = '${salt.minions.update_list_by_status:60000}')
    def checkNotDoneJobs() {

        log.debug("Start check not done jobs.")

        List<Job> notDoneJobs = jobRepository.findAllByDoneFalse()

        log.debug("Found [${notDoneJobs.size()}] not done jobs.")

        for (Job job : notDoneJobs) {

            checkJobByJid(job.jid)
        }
    }

}
