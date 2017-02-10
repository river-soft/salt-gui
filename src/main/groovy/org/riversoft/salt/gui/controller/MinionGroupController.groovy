package org.riversoft.salt.gui.controller

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.service.MinionGroupService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Slf4j
@RestController
class MinionGroupController extends BaseRestController {

    @Autowired
    MinionGroupService minionGroupService

    @RequestMapping('/minions-groups')
    findAllAcceptedMinions() {

        minionGroupService.findAllMinionsGroups()
    }

}
