package org.riversoft.salt.gui.controller

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.model.view.MinionGroupSimpleViewModel
import org.riversoft.salt.gui.service.MinionGroupService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Slf4j
@RestController
class MinionGroupController extends BaseRestController {

    @Autowired
    private MinionGroupService minionGroupService

    @RequestMapping('/minions-groups')
    findAllMinionsGroups() {

        minionGroupService.findAllMinionsGroups()
    }

    @RequestMapping('/groups-by-minion')
    findAllMinionGroupsWithChecks(@RequestParam(value = "name", required = true) String name) {
        minionGroupService.findAllMinionGroupsWithChecks(name)
    }

    @RequestMapping(value = '/minion-group', method = RequestMethod.POST)
    createMinionGroup(@RequestParam(value = "name", required = true) String name) {

        new MinionGroupSimpleViewModel(minionGroupService.createMinionGroup(name.trim()))
    }

    @RequestMapping(value = '/minion-group', method = RequestMethod.PUT)
    updateMinionGroup(@RequestParam(value = "id", required = true) String id,
                      @RequestParam(value = "name", required = true) String name) {

        new MinionGroupSimpleViewModel(minionGroupService.updateMinionGroup(id, name.trim()))
    }

    @RequestMapping(value = '/minion-group', method = RequestMethod.DELETE)
    deleteMinionGroup(@RequestParam(value = "id", required = true) String id) {

        minionGroupService.deleteMinionGroup(id)
    }
}
