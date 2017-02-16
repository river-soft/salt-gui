package org.riversoft.salt.gui.controller

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.model.CreateSaltScriptGroup
import org.riversoft.salt.gui.model.EditSaltScript
import org.riversoft.salt.gui.service.SaltScriptService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Slf4j
@RestController
class SaltScriptController extends BaseRestController {

    @Autowired
    private SaltScriptService saltScriptGroupService

    @Autowired
    private SaltScriptService saltScriptCRUDService

    @RequestMapping('/grouped-scripts')
    def findGroupedScripts() {

        saltScriptCRUDService.findAllGroupedScripts()
    }

    @RequestMapping(value = '/salt-script', method = RequestMethod.POST)
    def createSaltScriptGroup(@RequestBody CreateSaltScriptGroup createSaltScriptGroup) {

        saltScriptGroupService.createSaltScriptGroupAndScripts(createSaltScriptGroup)
    }

    @RequestMapping('/script-by-id')
    def findScriptByName(@RequestParam(value = "id", required = true) String id) {

        saltScriptGroupService.findScriptById(id)
    }

    @RequestMapping(value = '/salt-script', method = RequestMethod.PUT)
    def updateSaltScriptGroup(@RequestBody EditSaltScript editSaltScript) {

        saltScriptGroupService.updateSaltScript(editSaltScript)
    }

    @RequestMapping(value = '/salt-script', method = RequestMethod.DELETE)
    def deleteSaltScriptGroup(@RequestParam(value = "id", required = true) String scriptId) {

        saltScriptGroupService.deleteSaltScript(scriptId)
    }

}
