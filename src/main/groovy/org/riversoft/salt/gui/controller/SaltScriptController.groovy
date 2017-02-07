package org.riversoft.salt.gui.controller

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.service.SaltScriptService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Slf4j
@RestController
class SaltScriptController extends BaseRestController {

    @Autowired
    private SaltScriptService scriptService

    @RequestMapping('/grouped-scripts')
    def findGroupedScripts() {

        scriptService.findAllGroupedScripts()
    }

}
