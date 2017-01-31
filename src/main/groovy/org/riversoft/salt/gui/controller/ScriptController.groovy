package org.riversoft.salt.gui.controller

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.service.ScriptService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Slf4j
@RestController
class ScriptController {

    @Autowired
    ScriptService scriptService

    @RequestMapping('/scripts')
    def findScripts() {

        scriptService.findAllScripts()
    }

    @RequestMapping('/script-by-name')
    def findScriptByName(@RequestParam(value = "name", required = true) String name) {

        scriptService.findScriptByName(name)
    }

}
