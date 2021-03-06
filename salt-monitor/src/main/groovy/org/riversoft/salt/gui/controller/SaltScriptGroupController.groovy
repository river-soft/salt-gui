package org.riversoft.salt.gui.controller

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.model.view.SaltScriptGroupViewModel
import org.riversoft.salt.gui.service.SaltScriptGroupService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@Slf4j
@RestController
class SaltScriptGroupController extends BaseRestController {

    @Autowired
    SaltScriptGroupService saltScriptGroupService

    @PreAuthorize("hasAnyRole('ROLE_ROOT', 'ROLE_EDIT_SCRIPTS_GROUP')")
    @RequestMapping(value = '/salt-script-group', method = RequestMethod.PUT)
    def updateSaltScriptGroup(@RequestParam(value = "id", required = true) String id,
                              @RequestParam(value = "name", required = true) String name) {

        new SaltScriptGroupViewModel(saltScriptGroupService.updateSaltScriptGroup(id, name))
    }

    @PreAuthorize("hasAnyRole('ROLE_ROOT', 'ROLE_DELETE_SCRIPTS_GROUP')")
    @RequestMapping(value = '/salt-script-group', method = RequestMethod.DELETE)
    def deleteSaltScriptGroup(@RequestParam(value = "id", required = true) String id) {

        saltScriptGroupService.deleteSaltScriptGroup(id)
    }
}
