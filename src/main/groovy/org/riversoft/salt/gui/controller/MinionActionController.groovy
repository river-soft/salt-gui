package org.riversoft.salt.gui.controller

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.service.MinionsActionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Slf4j
@RestController
class MinionActionController extends BaseRestController {

    @Autowired
    MinionsActionService minionsActionService

    @RequestMapping(value = '/accept-minions', method = RequestMethod.POST)
    acceptedMinions(@RequestParam(value = "names", required = true) String[] names,
                    @RequestParam(value = "groups", required = true) String[] groups) {

        minionsActionService.acceptMinions(names, groups)
    }
}
