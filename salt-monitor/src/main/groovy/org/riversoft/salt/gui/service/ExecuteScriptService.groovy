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

import java.util.concurrent.ConcurrentHashMap

@Slf4j
@Service
class ExecuteScriptService {

    private String pingJid = ""
    private boolean inProcess = false
    @Value('${salt.user}') private String USER
    @Value('${salt.password}') private String PASSWORD

    @Autowired private SaltClient saltClient
    @Autowired private JobRepository jobRepository
    @Autowired private SaltScriptService saltScriptService
    @Autowired private MinionCRUDService minionCRUDService
    @Autowired private MinionsSaltService minionsSaltService
    @Autowired private JobResultRepository jobResultRepository
    @Autowired private SaltScriptFileService saltScriptFileService
    @Autowired private JobResultDetailRepository jobResultDetailRepository

    ConcurrentHashMap<String, Integer> countOfAttempts = new ConcurrentHashMap<String, Integer>()

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

        } catch (Exception e) {

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
    def checkJobByJid(Job job) {

        try {

            countOfAttempts.putIfAbsent(job.jid, 0)

            boolean isDone = false
            Jobs.Info jobInfoSalt = new Jobs.Info()

            if (countOfAttempts[job.jid] < 5) {

                log.debug("Start check Job by jid [${job.jid}].")

                // возвращает результат по самой job и результаты по миньонам такие как в job
                jobInfoSalt = Jobs.listJob(job.jid).callSync(saltClient, USER, PASSWORD, AuthModule.PAM)

                log.debug("Found jobInfoSalt: [${jobInfoSalt}]")

                for (String m : jobInfoSalt.minions) {

                    Minion minion = minionCRUDService.getMinionByName(m)
                    JobResultDetail jobResultDetail = new JobResultDetail()
                    log.debug("Found minion: [${minion.name}]")
                    log.debug("Search jobResults by minionId: [${minion.id}] and jid: [${job.jid}]")

                    JobResult jobResult = jobResultRepository.findAllByMinionIdAndJobJid(minion.id, job.jid)
                    log.debug("Found jobResults size [${jobResult}]")

                    if (jobResult.isResult == null) {

                        def res = jobInfoSalt.getResult(m, Object.class)?.value?.find()
                        log.debug("Res is: ${res}")

                        if (res instanceof String) {

                            log.warn("Something went wrong: ${res}")
                            jobResultDetail.comment = res
                            jobResultDetail.createDate = new Date()
                            jobResultDetail.lastModifiedDate = new Date()
                            jobResultDetail.jobResult = jobResult
                            jobResultDetail.result = false
                            jobResult.jobResultDetails.add(jobResultDetail)
                            jobResultDetailRepository.save(jobResultDetail)

                        } else {

                            log.debug("Create jobResultDetails from val: [${res.toString()}]")

                            def resValue = res?.value ?: [:]

                            jobResultDetail.cmd = res?.key
                            jobResultDetail.name = resValue.name
                            jobResultDetail.comment = resValue.comment
                            jobResultDetail.result = resValue.result ?: false
                            jobResultDetail.duration = resValue.duration
                            jobResultDetail.description = resValue.__id__
                            jobResultDetail.changes = resValue.changes
                            jobResultDetail.startTime = resValue.start_time
                            jobResultDetail.jobResult = jobResult
                            jobResultDetail.createDate = new Date()
                            jobResultDetail.lastModifiedDate = new Date()
                            jobResult.jobResultDetails.add(jobResultDetail)
                            jobResultDetailRepository.save(jobResultDetail)

                            log.debug("Successfully created JobResultDetail for minion [${jobResult.minion.name}] and job " +
                                    "id [${jobResult.job.jid}], result name [${jobResultDetail.name}].")

                        }

                        jobResult.isResult = true
                        jobResult.lastModifiedDate = new Date()
                        jobResultRepository.save(jobResult)
                        log.debug("Finish updating JobResult for minion [${jobResult.minion.name}] and job [${jobResult.job.jid}].")

                    } else {

                        log.debug("Property [isResult] of job result is [${jobResult.isResult}]. Nothing to update.")
                    }
                }

//                checkNotConnectedMinionAndUpdateResultStatus(job.results)

            } else {

                log.warn("After 5 attempts results by jod ${job.jid} not found. Set isResult false")

                List<JobResult> results = job.results.findAll {it.isResult == null}
                results*.isResult = false
                results*.lastModifiedDate = new Date()

                jobResultRepository.save(results)
            }

//            checkNotConnectedMinionAndUpdateResultStatus(job.results)
//
//            // endregion
//
//            //region update job

            if (!job.results.findAll { it.isResult == null }) {
                isDone = true
            }

            job.startTime = jobInfoSalt.startTime?.date
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

            log.debug("Finish check Job by jid [${job.jid}].")

            countOfAttempts.remove(job.jid)

            return jobInfoSalt

        } catch (Exception e) {

            countOfAttempts[job.jid]++

            log.error("Count of attempts: ${countOfAttempts[job.jid]}")
            log.error("Error of checking Job by jid [${job.jid}].")
            throw new SaltGuiException("Error of checking Job by jid [${job.jid}].", e,
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

            def result = Manage.up().callAsync(saltClient, USER, PASSWORD, AuthModule.PAM)

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
    @Scheduled(fixedDelayString = '${salt.minions.check_not_done_jobs:60000}')
    void checkNotDoneJobs() {

        if (inProcess) return

        log.debug("Start check not done jobs.")

        inProcess = true

        List<Job> notDoneJobs = jobRepository.findAllByDoneFalse()

        log.debug("Found [${notDoneJobs.size()}] not done jobs.")

        for (Job job : notDoneJobs) {

            try {

                checkJobByJid(job)

            } catch (Exception ex) {
                log.error("Occurred error when check job by jid [${job.jid}].Scip it.", ex)
            }
        }

        inProcess = false
    }
}
