package org.riversoft.salt.gui.controller

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.model.request.ExecuteMinionsAndScripts
import org.riversoft.salt.gui.service.ExecuteScriptService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Slf4j
@RestController
class ExecuteScriptController extends BaseRestController {

    @Autowired
    private ExecuteScriptService executeScriptService

    @PreAuthorize("hasAnyRole('ROLE_ROOT', 'ROLE_EXECUTE_SCRIPTS_ON_MINION', 'ROLE_EXECUTE_SCRIPT_ON_MINIONS')")
    @RequestMapping(value = '/execute-scripts', method = RequestMethod.POST)
    acceptMinions(@RequestBody ExecuteMinionsAndScripts executeMinionsAndScripts) {

        executeScriptService.executeScripts(executeMinionsAndScripts.minions, executeMinionsAndScripts.scripts)
    }

//    @PreAuthorize("hasAnyRole('ROLE_ROOT')")
//    @RequestMapping(value = '/check-job-by-jid', method = RequestMethod.GET)
//    checkJobByJid(@RequestParam(value = "jid", required = true) String jid) {
//
//        executeScriptService.checkJobByJid(jid)
//    }

    @PreAuthorize("hasAnyRole('ROLE_ROOT','ROLE_RE_EXECUTE_SCRIPTS_ON_MINIONS')")
    @RequestMapping(value = '/reexecute-scripts', method = RequestMethod.POST)
    reExecuteScripts(@RequestBody String[] jobResultIds) {
        executeScriptService.reExecuteScripts(jobResultIds)
    }
}
