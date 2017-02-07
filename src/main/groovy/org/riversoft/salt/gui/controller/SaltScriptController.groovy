package org.riversoft.salt.gui.controller

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.service.SaltScriptService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Slf4j
@RestController
class SaltScriptController extends BaseRestController {

    @Autowired
    private SaltScriptService scriptService

//    @RequestMapping('/scripts')
//    def findScripts() {
//
//        scriptService.findAllScripts()
//    }

    @RequestMapping('/grouped-scripts')
    def findGroupedScripts() {

        scriptService.findAllGroupedScripts()
    }

    @RequestMapping('/script-by-id')
    def findScriptByName(@RequestParam(value = "id", required = true) String id) {

        scriptService.findScriptById(id)
    }

}
