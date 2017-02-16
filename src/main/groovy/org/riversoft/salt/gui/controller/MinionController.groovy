package org.riversoft.salt.gui.controller

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.model.EditMinion
import org.riversoft.salt.gui.service.MinionCRUDService
import org.riversoft.salt.gui.service.MinionDetailsService
import org.riversoft.salt.gui.service.MinionsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Slf4j
@RestController
class MinionController extends BaseRestController {

    @Autowired
    private MinionsService minionsService

    @Autowired
    private MinionCRUDService minionCRUDService

    @Autowired
    private MinionDetailsService minionDetailsService

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

    @RequestMapping('/grouped-minions')
    def findAllGroupedMinions() {

        minionDetailsService.findAllGroupedMinions()
    }

    @RequestMapping(value = '/minion-details')
    def findMinionDetails(@RequestParam(value = "name", required = true) String minionName) {

        minionDetailsService.findMinionDetails(minionName)
    }

    @RequestMapping(value = '/change-minion-groups', method = RequestMethod.PUT)
    changeMinionGroups(@RequestBody EditMinion editMinion) {

        minionCRUDService.updateMinion(editMinion)
    }
}
