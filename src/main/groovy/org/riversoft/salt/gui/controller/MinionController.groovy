package org.riversoft.salt.gui.controller

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.model.EditMinion
import org.riversoft.salt.gui.service.MinionCRUDService
import org.riversoft.salt.gui.service.MinionsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@Slf4j
@RestController
class MinionController extends BaseRestController {

    @Autowired
    MinionsService minionsService

    @Autowired
    MinionCRUDService minionCRUDService

    @RequestMapping('/accepted-minions')
    findAllAcceptedMinions() {

        minionsService.findAllAcceptedMinions()
    }

    @MessageMapping('/minions-all-data')
    findAllMinions() {

        minionsService.findAndSendAllMinionsByStatuses()
        minionsService.findAndSendAllAcceptedMinions()
        minionsService.getAndSendCountsOfMinionsByStatus()
        minionsService.getAndSendCountsOfMinionsByGroup()
    }

    @RequestMapping(value = '/change-minion-groups', method = RequestMethod.POST)
    changeMinionGroups(@RequestBody EditMinion editMinion) {

        minionCRUDService.updateMinion(editMinion)
    }
}
