package org.riversoft.salt.gui.controller

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.service.MinionsService
import org.springframework.beans.factory.annotation.Autowired
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

    @RequestMapping('/unaccepted-minions')
    findAllUnaccepted() {

        minionsService.findAllUnaccepted()
    }

    @RequestMapping('/rejected-minions')
    findAllRejected() {

        minionsService.findAllRejected()
    }

    @RequestMapping('/denied-minions')
    findAllDenied() {

        minionsService.findAllDenied()
    }

    @RequestMapping('/minions-by-state')
    findAllByState(@RequestParam(value = "state", required = true) String state) {

        minionsService.findAllByState(state)
    }

    @RequestMapping('/counts-minions-by-state')
    getCountsOfMinionsByStatus() {

        minionsService.getCountsOfMinionsByStatus()
    }

    @RequestMapping('/counts-minions-by-group')
    getCountsOfMinionsByGroup() {

        minionsService.getCountsOfMinionsByGroup()
    }

}
