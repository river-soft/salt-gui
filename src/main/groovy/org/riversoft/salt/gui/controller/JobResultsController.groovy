package org.riversoft.salt.gui.controller

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.service.JobResultService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@Slf4j
@RestController
class JobResultsController extends BaseRestController {

    @Autowired
    JobResultService jobResultService

    @RequestMapping(value = '/job-results-counts', method = RequestMethod.GET)
    jobResults() {

        jobResultService.getAllJobResultsCount()
    }

}
