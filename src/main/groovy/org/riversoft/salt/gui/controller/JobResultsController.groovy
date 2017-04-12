package org.riversoft.salt.gui.controller

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.model.UserPrincipal
import org.riversoft.salt.gui.service.JobResultService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import javax.servlet.http.HttpServletRequest

@Slf4j
@RestController
class JobResultsController extends BaseRestController {

    @Autowired
    JobResultService jobResultService

    @PreAuthorize("hasAnyRole('ROLE_ROOT', 'ROLE_SHOW_JOB_RESULTS_COUNTERS')")
    @MessageMapping('/job-results-counts')
    findAllJobResultsCount(HashMap map) {

        jobResultService.updateFromDate(map["from"] as Integer)
        jobResultService.findAllJobResultsCount()
    }

    @PreAuthorize("hasAnyRole('ROLE_ROOT', 'ROLE_SHOW_RESULTS_BY_JOB')")
    @MessageMapping('/find-all-results-by-job')
    findAllResultsByJob(HashMap map) {

        jobResultService.updateJid(map["jid"] as String)
        jobResultService.findAllResultsByJob()
    }

    @PreAuthorize("hasAnyRole('ROLE_ROOT', 'ROLE_SHOW_RESULT_DETAILS')")
    @RequestMapping(value = '/find-details-by-job-result')
    def findDetailsByJobResult(@RequestParam(value = "result_id", required = true) String resultId) {

        jobResultService.findDetailsByJobResult(resultId)
    }

    //TODO тест
    @PreAuthorize("hasAnyRole('ROLE_ROOT')")
    @RequestMapping(value = '/find-results')
    def findJobResult(@RequestParam(value = "id", required = true) String id) {

        jobResultService.updateJid(id)
        jobResultService.findAllResultsByJob()
    }
}
