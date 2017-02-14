package org.riversoft.salt.gui.controller

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.service.MinionsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Slf4j
@RestController
class MinionController extends BaseRestController {

    @Autowired
    MinionsService minionsService

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


    @RequestMapping('/change-minion-groups')
    changeMinionGroups(@RequestParam(value = "name", required = true) String name,
                       @RequestParam(value = "groups", required = true) List<String> groups) {

        minionsService.findAllAcceptedMinions()
    }

}
