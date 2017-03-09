package org.riversoft.salt.gui.controller

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.service.JobResultService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Slf4j
@RestController
class JobResultsController extends BaseRestController {

    @Autowired
    JobResultService jobResultService

    @MessageMapping('/job-results-counts')
    findAllJobResultsCount() {

        jobResultService.findAllJobResultsCount()
    }

    @MessageMapping('/find-all-results-by-job')
    findAllResultsByJob(HashMap map) {

        jobResultService.updateJid(map["jid"] as String)
        jobResultService.findAllResultsByJob()
    }

    @RequestMapping(value = '/find-details-by-job-result')
    def findDetailsByJobResult(@RequestParam(value = "result_id", required = true) String resultId) {

        jobResultService.findDetailsByJobResult(resultId)
    }
}
