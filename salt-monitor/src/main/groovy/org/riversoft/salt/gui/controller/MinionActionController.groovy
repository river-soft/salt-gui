package org.riversoft.salt.gui.controller

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.service.MinionsActionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Slf4j
@RestController
class MinionActionController extends BaseRestController {

    @Autowired
    private MinionsActionService minionsActionService

    @PreAuthorize("hasAnyRole('ROLE_ROOT', 'ROLE_ACCEPT_MINION')")
    @RequestMapping(value = '/accept-minions', method = RequestMethod.POST)
    acceptMinions(@RequestParam(value = "names", required = true) String[] names,
                  @RequestParam(value = "groups", required = true) String[] groups) {

        minionsActionService.acceptMinions(names, groups)
    }

    @PreAuthorize("hasAnyRole('ROLE_ROOT', 'ROLE_REJECT_MINION')")
    @RequestMapping(value = '/reject-minions', method = RequestMethod.POST)
    rejectMinions(@RequestParam(value = "names", required = true) String[] names) {

        minionsActionService.rejectMinions(names)
    }

    @PreAuthorize("hasAnyRole('ROLE_ROOT', 'ROLE_DELETE_MINION')")
    @RequestMapping(value = '/delete-minions', method = RequestMethod.POST)
    deleteMinions(@RequestParam(value = "names", required = true) String[] names) {

        minionsActionService.deleteMinions(names)
    }
}
