package org.riversoft.salt.gui.controller

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.model.EditMinion
import org.riversoft.salt.gui.service.MinionCRUDService
import org.riversoft.salt.gui.service.MinionDetailsService
import org.riversoft.salt.gui.service.MinionsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.security.access.prepost.PreAuthorize
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

    @PreAuthorize("hasAnyRole('ROLE_ROOT', 'ROLE_MAIN_MINIONS_ACCEPTED_TAB')")
    @RequestMapping('/accepted-minions')
    findAllAcceptedMinions() {

        minionsService.findAllAcceptedMinions()
    }

    //TODO подумтаь какую сюда роль или перечень ролей для каждого из блоков страницы
    @PreAuthorize("hasAnyRole('ROLE_ROOT', 'ROLE_MAIN_MINIONS_COUNTS_STATUS')")
    @MessageMapping('/minions-all-data')
    findAllMinions() {

        minionsService.findAndSendAllMinionsByStatuses()
        minionsService.findAndSendAllAcceptedMinions()
        minionsService.getAndSendCountsOfMinionsByStatus()
        minionsService.getAndSendCountsOfMinionsByGroup()
    }

    @PreAuthorize("hasAnyRole('ROLE_ROOT', 'ROLE_GROUPED_MINIONS')")
    @RequestMapping('/grouped-minions')
    def findAllGroupedMinions() {

        minionDetailsService.findAllGroupedMinions()
    }

    @PreAuthorize("hasAnyRole('ROLE_ROOT', 'ROLE_SHOW_MINION_DETAILS')")
    @RequestMapping(value = '/minion-details')
    def findMinionDetails(@RequestParam(value = "name", required = true) String minionName) {

        minionDetailsService.findMinionDetails(minionName)
    }

    @PreAuthorize("hasAnyRole('ROLE_ROOT', 'ROLE_EDIT_GROUPS_OF_MINION')")
    @RequestMapping(value = '/change-minion-groups', method = RequestMethod.PUT)
    changeMinionGroups(@RequestBody EditMinion editMinion) {

        minionCRUDService.updateMinion(editMinion)
    }
}
