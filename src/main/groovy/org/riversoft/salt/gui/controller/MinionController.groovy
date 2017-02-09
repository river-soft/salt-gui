package org.riversoft.salt.gui.controller

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.service.MinionsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
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

    @RequestMapping('/minions-all-data')
    findAllMinions() {

        minionsService.findAndSendAllMinionsByStatuses()
        minionsService.findAndSendAllAcceptedMinions()
        minionsService.getAndSendCountsOfMinionsByStatus()
        minionsService.getAndSendCountsOfMinionsByGroup()
    }
}
