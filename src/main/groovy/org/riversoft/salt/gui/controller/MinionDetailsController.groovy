package org.riversoft.salt.gui.controller

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.service.MinionDetailsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Slf4j
@RestController
class MinionDetailsController extends BaseRestController {

    @Autowired
    MinionDetailsService minionDetailsService

    @RequestMapping('/grouped-minions')
    def findGroupedMinions() {

        minionDetailsService.findAllGroupedMinions()
    }

    @RequestMapping(value = '/minion-details')
    def createSaltScriptGroup(@RequestParam(value = "name", required = true) String minionName) {

        minionDetailsService.findMinionDetails(minionName)
    }

}
