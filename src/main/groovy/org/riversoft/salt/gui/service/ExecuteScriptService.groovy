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
import org.riversoft.salt.gui.exception.SaltGuiException
import org.riversoft.salt.gui.repository.JobRepository
import org.riversoft.salt.gui.repository.JobResultDetailRepository
import org.riversoft.salt.gui.repository.JobResultRepository
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
    private SaltScriptFileService saltScriptFileService

    @Autowired
    private JobResultDetailRepository jobResultDetailRepository

    //endregion

    /**
     * Выполнение списка скриптов для списка миньонов
     * @param minions - список миньонов
     * @param scripts - список скриптов
     */
    def executeScripts(String[] minions, String[] scripts) {

        try {

            //region список скриптов и создание файлов скриптов на salt server

            List<SaltScript> saltScripts = []

            log.debug("Start preparing scripts files for execution on salt server.")

            for (String scriptName : scripts) {

                SaltScript script = saltScriptService.getSaltScriptByName(scriptName)

                //создание sls файлы скрипта
                saltScriptFileService.createSaltScriptSlsFile(script.name, script.content)

                saltScripts.add(script)
            }

            log.debug("End preparing scripts files for execution on salt server.")

            //endregion

            //region отправка миньонов и скриптов на выполнение на salt сервер

            log.debug("Sending scripts names [${scripts.join(",")}] and minions names [${minions.join(",")}] for execution to salt server.")

            Target<List<String>> minionList = new MinionList(minions)

            LocalAsyncResult<Map<String, State.ApplyResult>> result = State.apply(scripts).callAsync(
                    saltClient, minionList, USER, PASSWORD, AuthModule.PAM)

            log.debug("Successfully returned response from salt server with job id [${result.jid}].")

            //endregion

            //region работа

            log.debug("Start creating Job with jid [${result.jid}].")

            Job job = jobRepository.save(
                    new Job(
                            jid: result.jid,
                            done: false,
                            createDate: new Date(),
                            lastModifiedDate: new Date(),
                            name: saltScripts.collect {
                                it.name
                            }.join("/")
                    )
            )

            log.debug("Successfully created Job with jid [${result.jid}] and with name [${job.name}].")

            //endregion

            //region результаты скриптов

            for (String minionResult : result.minions) {

                Minion minion = minionCRUDService.getMinionByName(minionResult)

                log.debug("Start creating JobResult for minion [${minion.name}] and job with jid [${job.jid}].")

                JobResult jobResult = new JobResult(
                        minion: minion,
                        job: job,
                        saltScripts: saltScripts,
                        reExecuted: false,
                        createDate: new Date(),
                        lastModifiedDate: new Date()
                )

                jobResultRepository.save(jobResult)

                job.results.add(jobResult)
                jobRepository.save(job)

                log.debug("Successfully created JobResult for minion [${minion.name}] and job with jid [${result.jid}].")
            }

            //endregion

        } catch (Exception e) {

            log.error("Error of executing scripts for minions.")
            throw new SaltGuiException("Error of executing scripts for minions.", e,
                    "Произошла ошибка при выполнении скриптов для миньонов, ${e.message}")
        }
    }

    /**
     * Метод перезапуска скриптов
     * @param jobResultIds - список уникальных идентивикаторов результата работы
     */
    void reExecuteScripts(String[] jobResultIds) {

        List<JobResult> jobResults = jobResultRepository.findAllByIdIn(jobResultIds)
        String[] minions = []
        String[] scripts = []

        jobResults.each {
            minions += it.minion.name
            if (!scripts.size()) {
                it.saltScripts.each {
                    scripts += it.name
                }
            }
        }

        try {

            log.debug("Start reExecuting scripts ${scripts.toString()} for minions ${minions.toString()}")

            this.executeScripts(minions, scripts)

            jobResults.each {
                it.reExecuted = true
            }

            jobResultRepository.save(jobResults)

            log.debug("Update JobResults with size [${jobResults.size()}], change reExecute to true")
            log.debug("Successfully reExecuting scripts")

        } catch (Exception e) {

            log.error("Error of re-executing scripts for minions.")
            throw new SaltGuiException("Error of re-executing scripts for minions.", e,
                    "Произошла ошибка при перезапуске скриптов для миньонов, ${e.message}")
        }
    }

    /**
     * Проверка результатов выполнения работы
     * @param jid - уникальный id работы
     * @return
     */
    def checkJobByJid(String jid) {

        try {

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

            List<JobResult> jobResultsNoConnection = jobResultRepository.findAllByJobJid(job.jid)

            for (def jobResultSalt : jobResultsSalt) {

                Minion minion = minionCRUDService.getMinionByName(jobResultSalt.key)

                List<JobResult> jobResults = jobResultRepository.findAllByMinionIdAndJobJid(minion.id, job.jid)

                for (JobResult jobResult : jobResults) {

                    log.debug("Start updating JobResult for minion [${jobResults.minion.name}] and Job jid [${jobResult.job.jid}].")

                    for (def jobResultSaltItem : jobResultSalt) {

                        for (def val : jobResultSaltItem.value) {

                            log.debug("Start creating JobResultDetail fom minion [${jobResult.minion.name}] and job " +
                                    "id [${jobResult.job.jid}].")

                            if (val instanceof String) {

                                log.warn("No result details fom minion [${jobResult.minion.name}] and job id [${jobResult.job.jid}]." +
                                        "Reason - [${val}].")

                                JobResultDetail jobResultDetail = new JobResultDetail()
                                jobResultDetail.comment = val
                                jobResultDetail.createDate = new Date()
                                jobResultDetail.lastModifiedDate = new Date()
                                jobResultDetail.jobResult = jobResult
                                jobResultDetail.result = false

                                jobResult.jobResultDetails.add(jobResultDetail)

                                jobResultDetailRepository.save(jobResultDetail)

                            } else {

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
                                jobResultDetail.createDate = new Date()
                                jobResultDetail.lastModifiedDate = new Date()

                                jobResult.jobResultDetails.add(jobResultDetail)

                                jobResultDetailRepository.save(jobResultDetail)
                                log.debug("Successfully created JobResultDetail for minion [${jobResult.minion.name}] and job " +
                                        "id [${jobResult.job.jid}], result name [${jobResultDetail.name}].")
                            }
                        }
                    }

                    jobResult.isResult = true
                    jobResult.lastModifiedDate = new Date()

                    jobResultRepository.save(jobResult)
                    log.debug("Finish updating JobResult for minion [${jobResult.minion.name}] and job [${jobResult.job.jid}].")

                    //TODO подумать когда отмечать задачу как выполненную
                    isDone = true

                    jobResultsNoConnection.removeAll { it.id == jobResult.id }
                }

                //отмечаем результаты которые не получили ответ т.е. no connected
                for (JobResult jobResultNoConnection : jobResultsNoConnection) {

                    log.debug("JobResult for minion [${jobResultNoConnection.minion.name}] and job [${jobResultNoConnection.job.jid}] don't received the response.")

                    jobResultNoConnection.isResult = false
                    jobResultNoConnection.lastModifiedDate = new Date()

                    jobResultRepository.save(jobResultNoConnection)
                    log.debug("Updated JobResult for minion [${jobResultNoConnection.minion.name}] and job [${jobResultNoConnection.job.jid}].")
                }
            }

            // endregion

            //region update job

            job.startTime = jobInfoSalt.startTime.date
            job.user = jobInfoSalt.user
            job.done = isDone
            job.lastModifiedDate = new Date()

            jobRepository.save(job)

            // endregion

            //region delete scripts from salt server

            if (job.done) {

                log.debug("Start deleting scripts files from salt server after execution.")

                List<SaltScript> saltScripts = job.results.find()?.saltScripts ?: []

                //удаление sls файлов скрипов
                for (SaltScript script : saltScripts) {

                    saltScriptFileService.deleteSaltScriptSlsFile(script.name)
                }

                log.debug("End deleting scripts files from salt server after execution.")
            }

            // endregion

            //job2.result.values().find().members.find().value.members.find().value.members

            log.debug("Finish check Job by jid [${jid}].")

            return jobInfoSalt

        } catch (Exception e) {

            log.error("Error of checking Job by jid [${jid}].")
            throw new SaltGuiException("Error of checking Job by jid [${jid}].", e,
                    "Произошла ошибка при проверке работы, ${e.message}")
        }
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
