package org.riversoft.salt.gui.service

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.AuthModule
import org.riversoft.salt.gui.calls.LocalAsyncResult
import org.riversoft.salt.gui.calls.modules.State
import org.riversoft.salt.gui.calls.runner.Jobs
import org.riversoft.salt.gui.calls.runner.Manage
import org.riversoft.salt.gui.client.SaltClient
import org.riversoft.salt.gui.datatypes.target.MinionList
import org.riversoft.salt.gui.datatypes.target.Target
import org.riversoft.salt.gui.domain.Job
import org.riversoft.salt.gui.domain.JobResult
import org.riversoft.salt.gui.domain.JobResultDetail
import org.riversoft.salt.gui.domain.Minion
import org.riversoft.salt.gui.domain.SaltScript
import org.riversoft.salt.gui.exception.JobIdNotReturnedException
import org.riversoft.salt.gui.exception.MinionNotRegisteredOnSaltException
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

    private String pingJid = ""

    @Autowired
    private SaltClient saltClient

    @Autowired
    private JobRepository jobRepository

    @Autowired
    private SaltScriptService saltScriptService

    @Autowired
    private MinionCRUDService minionCRUDService

    @Autowired
    private MinionsSaltService minionsSaltService

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

            log.debug("Start executing scripts on minions.")

            //region проверка все ли миньоны зарегистрированы на сервере солт

            String[] acceptedMinions = minionsSaltService.getAllAcceptedMinions()
            String[] notRegisteredMinions = minions - acceptedMinions

            if (notRegisteredMinions.size()) {

                String notRegisteredMinionsNames = notRegisteredMinions.collect { it }.join(", ")

                log.error("Minions with names: [${notRegisteredMinionsNames}] not registered on salt server.")
                throw new MinionNotRegisteredOnSaltException("Minions with names: [${notRegisteredMinionsNames}] not registered on salt server.",
                        "error.minions.not_registered_on_salt", [notRegisteredMinionsNames])

            }

            //endregion

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

            String jid = result.jid

            log.debug("Successfully returned response from salt server with job id [${jid}].")

            //endregion

            //region проверка если работа не создалась для выполнения скриптов

            if (!jid) {

                log.error("Job id not returned from salt server.")

                log.debug("Start deleting scripts files from salt server after not created Job.")

                //удаление sls файлов скрипов
                for (SaltScript script : saltScripts) {

                    saltScriptFileService.deleteSaltScriptSlsFile(script.name)
                }

                log.debug("End deleting scripts files from salt server after not created Job.")

                throw new JobIdNotReturnedException("Job id not returned from salt server.",
                        "error.scripts.executing.jid_not_returned")
            }

            //endregion

            //region создание работы

            log.debug("Start creating Job with jid [${result.jid}].")

            Job job = jobRepository.save(
                    new Job(
                            jid: jid,
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

            //region запись результатов работы по миньонам на salt сервере

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

            log.debug("Finish executing scripts on minions.")

        } catch (JobIdNotReturnedException e) {
            throw e

        } catch (MinionNotRegisteredOnSaltException e) {
            throw e
        }
        catch (Exception e) {

            log.error("Error of executing scripts for minions.")
            throw new SaltGuiException("Error of executing scripts for minions.", e,
                    "error.scripts.executing")
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
                    "error.scripts.reexecuting")
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

            log.debug("Found jobInfoSalt: [${jobInfoSalt}]")

            log.debug("Start getting result for jid [${jobInfoSalt?.jid}]")

            //вовзращает список результатов по миньонам
            //TODO подумать может результат все таки получать из jobInfoSalt что бы не делать два запроса на salt сервер
            def jobResultsSalt = Jobs.lookupJid(jobInfoSalt.jid).callSync(saltClient, USER, PASSWORD, AuthModule.PAM);

            log.debug("Found jobResultSalt: [${jobResultsSalt}]")

            Job job = jobRepository.findOne(jobInfoSalt.jid)

            boolean isDone = false

            // region update job results

            if (jobResultsSalt.size() == 0) {
                log.warn("Result by job with jid [${job.jid}] not found yet.")

            } else {

                log.debug("Got [${jobResultsSalt.size()}] results by job with jid [${job.jid}].")
            }

            for (def jobResultSalt : jobResultsSalt) {

                log.debug("Search minion from jobResultsSalt: [${jobResultsSalt.toString()}]")

                Minion minion = minionCRUDService.getMinionByName(jobResultSalt.key)

                log.debug("Found minion: [${minion.name}]")

                log.debug("Search jobResults by minionId: [${minion.id}] and jid: [${job.jid}]")

                List<JobResult> jobResults = jobResultRepository.findAllByMinionIdAndJobJid(minion.id, job.jid)

                log.debug("Found jobResults size [${jobResults.size()}]")

                for (JobResult jobResult : jobResults) {

                    log.debug("Check isResult for jobResult: [${jobResult.toString()}]")

                    if (jobResult.isResult == null) {

                        log.debug("Start updating JobResult for minion [${jobResults.minion.name}] and Job jid [${jobResult.job.jid}].")

                        for (def jobResultSaltItem : jobResultSalt) {

                            log.debug("Each jobResultSalt. Item [${jobResultSaltItem.toString()}]")

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

                                    log.debug("Create jobResultDetails from val: [${val.toString()}]")

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

                    } else {

                        log.debug("Property [isResult] of job result is [${jobResult.isResult}]. Nothing to update.")
                    }
                }
            }

            checkNotConnectedMinionAndUpdateResultStatus(job.results)

            // endregion

            //region update job

            if (!job.results.findAll { it.isResult == null }) {
                isDone = true
            }

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
                    "error.check.job")
        }
    }

    /**
     * Проверка не подключенных миньонов и обновление результата работ для них
     * @param jobResults - список результатов работы в виде объектов JobResult
     */
    void checkNotConnectedMinionAndUpdateResultStatus(List<JobResult> jobResults) {

        log.debug("Try check not connected minions")
        log.debug("Current ping Jid: [${pingJid}].")

        if (!pingJid) {

            def result = Manage.up().callAsync(saltClient, USER, PASSWORD, AuthModule.PAM);

            log.debug("Got result with pingJid [${result.jid}].")

            pingJid = result.jid
            log.debug("Set Current pingJid: [${pingJid}]")
        }

        log.debug("Try get results for connected minions from SALT by pingJid [${pingJid}]")

        //вовзращает список результатов по миньонам
        def connectedMinionsJobResult = Jobs.lookupJid(pingJid).callSync(saltClient, USER, PASSWORD, AuthModule.PAM);

        log.debug("Got results for connected minions from SALT [${connectedMinionsJobResult}]")

        if (connectedMinionsJobResult.size()) {

            String[] connectedMinions = connectedMinionsJobResult.find() ? connectedMinionsJobResult.find()["value"]["return"] : []

            log.debug("Got [${connectedMinions.size()}] connectedMinions.")

            String[] minionsFromResult = jobResults.collect { it.minion.name }

            log.debug("Got [${minionsFromResult.size()}] minionsFromResult with names [${minionsFromResult.toString()}].")

            String[] notConnectedMinions = minionsFromResult - connectedMinions

            log.debug("Got [${notConnectedMinions.size()}] notConnectedMinions with names [${notConnectedMinions.toString()}].")

            def notConnectedResults = jobResults.findAll {
                notConnectedMinions.contains(it.minion.name) && it.isResult == null
            }

            log.debug("Got notConnectedResults [${notConnectedResults.size()}]")

            for (JobResult jobResult : notConnectedResults) {

                log.debug("JobResult for minion [${jobResult.minion.name}] and job [${jobResult.job.jid}] don't received the response.")

                jobResult.isResult = false
                jobResult.lastModifiedDate = new Date()

                jobResultRepository.save(jobResult)

                log.debug("Updated JobResult for minion [${jobResult.minion.name}] and job [${jobResult.job.jid}] setting value [isResult] to [${jobResult.isResult}].")
            }

            pingJid = ""

        } else {
            log.debug("Results for connected minions from SALT not found")
        }
    }

    /**
     * Автоматическая проверка и получение результатов выполнения работы скриптов
     */
    @Scheduled(fixedDelayString = '${salt.minions.update_list_by_status:60000}')
    void checkNotDoneJobs() {

        log.debug("Start check not done jobs.")

        List<Job> notDoneJobs = jobRepository.findAllByDoneFalse()

        log.debug("Found [${notDoneJobs.size()}] not done jobs.")

        for (Job job : notDoneJobs) {

            try {

                checkJobByJid(job.jid)

            } catch (Exception ex) {
                log.error("Occurred error when check job by jid [${job.jid}].Scip it.", ex)
            }
        }
    }
}
