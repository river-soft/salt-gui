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
import org.riversoft.salt.gui.domain.Minion
import org.riversoft.salt.gui.domain.SaltScript
import org.riversoft.salt.gui.repository.JobRepository
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
    JobRepository jobRepository

    @Autowired
    JobResultRepository jobResultRepository

    @Autowired
    SaltScriptService saltScriptService

    @Autowired
    MinionCRUDService minionCRUDService

    //endregion

    /**
     * Выполнение списка скриптов для списка миньонов
     * @param minions - список миньонов
     * @param scripts - список скриптов
     */
    def executeScripts(String[] minions, String[] scripts) {

        //TODO собрать массив названий скриптов на выполнение или же принимать прямо с фронта список из названий?
        Target<List<String>> minionList = new MinionList(minions);

        LocalAsyncResult<Map<String, Result<Boolean>>> result = State.apply(scripts).callAsync(
                saltClient, minionList, USER, PASSWORD, AuthModule.PAM);

        Job job = new Job(jid: result.jid, done: false)

        job = jobRepository.save(job)

        for (String minionResult : result.minions) {

            Minion minion = minionCRUDService.getMinionByName(minionResult)

            for (String script : scripts) {

                SaltScript saltScript = saltScriptService.getSaltScriptByName(script)

                JobResult jobResult = new JobResult(minion: minion, job: job, saltScript: saltScript)

                jobResultRepository.save(jobResult)

                job.results.add(jobResult)
                jobRepository.save(job)
            }
        }
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
        def jobResultsSalt = Jobs.lookupJid(jid).callSync(saltClient, USER, PASSWORD, AuthModule.PAM);

        Job job = jobRepository.findOne(jobInfoSalt.jid)

        boolean isDone = true

        // region update job results

        List<String> scriptNames = jobInfoSalt.arguments["mods"]

        for (def jobResultSalt : jobResultsSalt) {

            Minion minion = minionCRUDService.getMinionByName(jobResultSalt.key)

            List<JobResult> jobResults = jobResultRepository.findAllByMinionIdAndJobJid(minion.id, job.jid)
            //.findAll { scriptNames.indexOf(it.saltScript.name) }

            for (JobResult jobResult : jobResults) {

                log.debug("Start updating JobResult for minion [${jobResults.minion.name}] and Job jid [${jobResult.job.jid}].")

                for (def val : jobResultSalt.value) {

                    jobResult.name = val["value"]["name"]
                    jobResult.comment = val["value"]["comment"]
                    jobResult.result = val["value"]["result"]
                    jobResult.duration = val["value"]["duration"] as double
                    jobResult.description = val["value"]["__id__"]
                    jobResult.changes = val["value"]["changes"]

                    //TODO может не надо?
                    if (!jobResult.result) {
                        isDone = false
                    }
                }

                jobResultRepository.save(jobResult)
                log.debug("Finish updating JobResult => [${jobResult.minion.name}], [${jobResult.name}], [${jobResult.job.jid}], [${jobResult.description}].")
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
