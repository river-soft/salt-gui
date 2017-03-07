package org.riversoft.salt.gui.controller

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.service.ExecuteScriptService
import org.riversoft.salt.gui.service.JobResultService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Slf4j
@RestController
class ExecuteScriptController extends BaseRestController {

    @Autowired
    private ExecuteScriptService executeScriptService

    @Autowired
    JobResultService jobResultService

    @RequestMapping(value = '/execute-scripts', method = RequestMethod.POST)
    acceptMinions(@RequestParam(value = "minions", required = true) String[] minions,
                  @RequestParam(value = "scripts", required = true) String[] scripts) {

        executeScriptService.executeScripts(minions, scripts)
    }

    @RequestMapping(value = '/check-job-by-jid', method = RequestMethod.GET)
    checkJobByJid(@RequestParam(value = "jid", required = true) String jid) {

        executeScriptService.checkJobByJid(jid)
    }

    @RequestMapping(value = '/job-results', method = RequestMethod.GET)
    jobResults() {

        jobResultService.findAllJobResultsCount()
    }

}
