package org.riversoft.salt.gui.service

import com.fasterxml.jackson.databind.ObjectMapper
import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.domain.Job
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

            def notConnectedCount = job.results.findAll { it.isResult == false }.size()

            def falseCount = job.results.findAll {
                it.jobResultDetails.findAll { it.result == false } && it.isResult == true
            }.size()

            def trueCount = 0

            if (!falseCount) {
                trueCount = job.results.findAll {
                    it.jobResultDetails.findAll { it.result == true } && it.isResult == true
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

        sendCountsOfJobResultsByStatus(resultsData)

//        return resultsData
    }

    /**
     * Отправка количества результатов выполнения работы по статусам
     * @param map - объект/мапа с данными
     */
    void sendCountsOfJobResultsByStatus(def map) {

        log.trace("Update counts of job results by statuses [${mapper.writeValueAsString(map)}]")

        messagingTemplate.convertAndSend('/queue/job-results/update-counts-job-results', mapper.writeValueAsString(map))
    }

}
