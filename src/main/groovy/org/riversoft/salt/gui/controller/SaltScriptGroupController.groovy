package org.riversoft.salt.gui.controller

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.model.CreateSaltScriptGroup
import org.riversoft.salt.gui.service.SaltScriptGroupService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@Slf4j
@RestController
class SaltScriptGroupController extends BaseRestController {

    @Autowired
    SaltScriptGroupService saltScriptGroupService

    @RequestMapping(value = '/salt-script-group', method = RequestMethod.POST)
    def createSaltScriptGroup(@RequestBody CreateSaltScriptGroup createSaltScriptGroup) {

        saltScriptGroupService.createSaltScriptGroupAndScripts(createSaltScriptGroup)
    }

}
