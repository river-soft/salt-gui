package org.riversoft.salt.gui.service

import com.fasterxml.jackson.databind.ObjectMapper
import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.domain.Job
import org.riversoft.salt.gui.model.view.JobResultViewModel
import org.riversoft.salt.gui.model.view.JobResultsCountsViewModel
import org.riversoft.salt.gui.repository.JobRepository
import org.riversoft.salt.gui.repository.JobResultRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Slf4j
@Service
class JobResultService {

    private String currentJid = ""

    //region injection

    @Autowired
    private ObjectMapper mapper

    @Autowired
    private JobRepository jobRepository

    @Autowired
    private JobResultRepository jobResultRepository

    @Autowired
    private SimpMessagingTemplate messagingTemplate

    //endregion

    /**
     * Получение количества результатов выполнения работы по статусам
     * @return список моделей JobResultsCountsViewModel
     * @see JobResultsCountsViewModel
     */
    @Scheduled(fixedDelayString = '${salt.job_results.update_counts_interval:5000}')
    def getAllJobResultsCount() {

        List<Job> jobs = jobRepository.findAll()

        List<JobResultsCountsViewModel> resultsData = []

        for (Job job : jobs) {

            def notConnectedCount = job.results.findAll { !it.isResult }.size()

            def falseCount = job.results.findAll {
                it.jobResultDetails.findAll { !it.result } && it.isResult
            }.size()

            def trueCount = 0

            if (!falseCount) {
                trueCount = job.results.findAll {
                    it.jobResultDetails.findAll { it.result } && it.isResult
                }.size()
            }

            resultsData.add(
                    new JobResultsCountsViewModel(
                            jobName: job.name,
                            jid: job.jid,
                            notConnectedCounts: notConnectedCount,
                            falseCounts: falseCount,
                            trueCounts: trueCount
                    )
            )
//
//            //TODO если isResult = false значит результат не пришел no connection
//            //TODO если isResult = true результ есть
//            //TODO если result.resultItems содержит хоть один false значит не выполнен
        }

        sendJobResultsBySignal('/queue/job-results/update-counts-job-results', "result counts", resultsData)

        return resultsData
    }

    /**
     * Обновление значения текущего jid
     * @param jid - уникальный номер работыn
     */
    def updateJid(String jid) {
        currentJid = jid
    }

    /**
     * Поиск всех результатов работы
     */
    @Scheduled(fixedDelayString = '${salt.minions.update_counts_interval:5000}')
    def findAllResultsByJob() {

        if (currentJid) {

            Job job = jobRepository.findOne(currentJid)

            def results = job.results.collect { new JobResultViewModel(it) }

            sendJobResultsBySignal('/queue/job-results/update-all-results-by-job', "all results by job with jid [${currentJid}]", results)
        }
    }

//    /**
//     * Поиск no connected результатов работы
//     */
//    @Scheduled(fixedDelayString = '${salt.minions.update_counts_interval:5000}')
//    def findNoConnectedResultsByJob() {
//
//        if (currentJid) {
//
//            Job job = jobRepository.findOne(currentJid)
//
//            //isResult = false значит результат не пришел
//
//            def notConnectedResults = job.results.findAll { !it.isResult }
//
//            def results = notConnectedResults.collect { new JobResultViewModel(it) }
//
//            sendJobResultsBySignal('/queue/job-results/update-no-connected-results-by-job', "no connected results by job with jid [${currentJid}]", results)
//        }
//    }
//
//    /**
//     * Поиск false результатов работы
//     */
//    @Scheduled(fixedDelayString = '${salt.minions.update_counts_interval:5000}')
//    def findFalseResultsByJob() {
//
//        if (currentJid) {
//
//            Job job = jobRepository.findOne(currentJid)
//
//            //есди result.resultItems содержит хоть один false значит не выполнен
//
//            def falseResults = job.results.findAll {
//                it.jobResultDetails.findAll { !it.result } && it.isResult
//            }
//
//            def results = falseResults.collect { new JobResultViewModel(it) }
//
//            sendJobResultsBySignal('/queue/job-results/update-false-results-by-job', "false results by job with jid [${currentJid}]", results)
//        }
//    }
//
//    /**
//     * Поиск true результатов работы
//     */
//    @Scheduled(fixedDelayString = '${salt.minions.update_counts_interval:5000}')
//    def findTrueResultsByJob() {
//
//        if (currentJid) {
//
//            Job job = jobRepository.findOne(currentJid)
//
//            def falseResultsCount = job.results.findAll {
//                it.jobResultDetails.findAll { !it.result } && it.isResult
//            }.size()
//
//            def trueResults = []
//
//            if (!falseResultsCount) {
//                trueResults = job.results.findAll {
//                    it.jobResultDetails.findAll { it.result } && it.isResult
//                }
//            }
//
//            def results = trueResults.collect { new JobResultViewModel(it) }
//
//            sendJobResultsBySignal('/queue/job-results/update-true-results-by-job', "true results by job with jid [${currentJid}]", results)
//            return results
//        }
//    }

    /**
     * Отправка данных результатов выполнения работы по статусам
     * @param map - объект/мапа с данными
     */
    void sendJobResultsBySignal(String signal, String message, def map) {

        log.trace("Update ${message} to [${mapper.writeValueAsString(map)}].")

        messagingTemplate.convertAndSend(signal, mapper.writeValueAsString(map))
    }
}