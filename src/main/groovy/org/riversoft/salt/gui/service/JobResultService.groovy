package org.riversoft.salt.gui.service

import com.fasterxml.jackson.databind.ObjectMapper
import groovy.util.logging.Slf4j
import org.joda.time.DateTime
import org.riversoft.salt.gui.domain.Job
import org.riversoft.salt.gui.domain.JobResult
import org.riversoft.salt.gui.model.view.JobResultDetailsViewModel
import org.riversoft.salt.gui.model.view.JobResultViewModel
import org.riversoft.salt.gui.model.view.JobResultsCountsViewModel
import org.riversoft.salt.gui.repository.JobRepository
import org.riversoft.salt.gui.repository.JobResultRepository
import org.riversoft.salt.gui.utils.DateTimeParser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service

@Slf4j
@Service
class JobResultService {

    private String currentJid = ""

    private Date fromDate

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
     * Обновление значения текущего jid
     * @param jid - уникальный номер работы
     */
    void updateJid(String jid) {
        currentJid = jid
    }

    /**
     * Обновление значения даты с которой начинать поиск результатов работы
     * @param from - минуты
     */
    void updateFromDate(Integer from = null) {

        if (!from) {
            //по умолчанию, если не указано время берется за последние 2 часа тюею 120 минут
            from = 120
        }

        DateTime dateTime = new DateTime().minusMinutes(from)
        fromDate = dateTime.toDate()
    }

    /**
     * Получение количества результатов выполнения работы по статусам
     * @return список моделей JobResultsCountsViewModel
     * @see JobResultsCountsViewModel
     */
    @Scheduled(fixedDelayString = '${salt.job_results.update_counts_interval:5000}')
    def findAllJobResultsCount() {

        Sort sort = new Sort(Sort.Direction.DESC, "createDate")

        if (!fromDate) {
            updateFromDate()
        }

        //поиск по промежутку дат
        List<Job> jobs = jobRepository.findAllByCreateDateBetween(fromDate, new Date(), sort)

        List<JobResultsCountsViewModel> resultsData = []

        for (Job job : jobs) {

            def totalCount = job.results.size()
            def waitingCount = job.results.findAll { it.isResult == null }.size()
            def notConnectedCount = job.results.findAll { it.isResult == false }.size()
            def falseCount = job.results.findAll {
                it.jobResultDetails.findAll { it.result == false } && it.isResult
            }.size()

            def trueCount = totalCount - notConnectedCount - falseCount - waitingCount

            resultsData.add(
                    new JobResultsCountsViewModel(
                            jobName: job.name,
                            jid: job.jid,
                            notConnectedCounts: notConnectedCount,
                            falseCounts: falseCount,
                            trueCounts: trueCount,
                            waitingCount: waitingCount
                    )
            )
        }

        sendJobResultsBySignal('/queue/job-results/update-counts-job-results', "result counts", resultsData)
    }

    /**
     * Поиск всех результатов работы
     */
    @Scheduled(fixedDelayString = '${salt.job_results.update_list_interval:5000}')
    def findAllResultsByJob() {

        if (currentJid) {

            Job job = jobRepository.findOne(currentJid)

            if (job) {
                def results = job.results.collect { new JobResultViewModel(it) }

                sendJobResultsBySignal('/queue/job-results/update-all-results-by-job', "all results by job with jid [${currentJid}]", results)
            } else {
                log.debug("Job with [${currentJid}] not fount in db. Clear currentJid value.")
                currentJid= ""
            }
        }
    }

    /**
     * Поиск деталей результата выполнения работы
     * @param resultId - уникальный идентификатор результата работы
     * @return список объктов JobResultDetailsViewModel
     * @see JobResultDetailsViewModel
     */
    List<JobResultDetailsViewModel> findDetailsByJobResult(String resultId) {

        log.debug("Start searching details by job result with id [${resultId}]")

        JobResult jobResult = jobResultRepository.findOne(resultId)

        List<JobResultDetailsViewModel> result = jobResult.jobResultDetails.collect {
            new JobResultDetailsViewModel(it)
        }

        log.debug("Finish searching details by job result with id [${resultId}], found [${result.size()}] details records.")

        return result
    }

    /**
     * Отправка данных результатов выполнения работы по статусам
     * @param signal - сигнал для отправки результатов
     * @param message - сообщение выполняемого действия
     * @param map - объект/мапа с данными
     */
    void sendJobResultsBySignal(String signal, String message, def map) {

        log.trace("Update ${message} to [${mapper.writeValueAsString(map)}].")

        messagingTemplate.convertAndSend(signal, mapper.writeValueAsString(map))
    }
}