package org.riversoft.salt.gui.controller

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.service.JobResultService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.web.bind.annotation.RestController

@Slf4j
@RestController
class JobResultsController extends BaseRestController {

    @Autowired
    JobResultService jobResultService

    @MessageMapping('/job-results-counts')
    getAllJobResultsCount() {

        jobResultService.getAllJobResultsCount()
    }

    @MessageMapping('/find-all-results-by-job')
    findAllResultsByJob(HashMap map) {

        jobResultService.updateJid(map["jid"] as String)
        jobResultService.findAllResultsByJob()
    }

//    @MessageMapping('/find-no-connected-results-by-job')
//    findNoConnectedResultsByJob(HashMap map) {
//
//        jobResultService.updateJid(map["jid"] as String)
//        jobResultService.findNoConnectedResultsByJob()
//    }
//
//    @MessageMapping('/find-false-results-by-job')
//    findFalseResultsByJob(HashMap map) {
//
//        jobResultService.updateJid(map["jid"] as String)
//        jobResultService.findFalseResultsByJob()
//    }
//
//    @MessageMapping('/find-true-results-by-job')
//    findTrueResultsByJob(HashMap map) {
//
//        jobResultService.updateJid(map["jid"] as String)
//        jobResultService.findTrueResultsByJob()
//    }

    //TODO получение списка деталей конкретного результата

}
