package org.riversoft.salt.gui.controller

import groovy.util.logging.Slf4j
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Slf4j
@Controller
class HomeController {

    @RequestMapping("/")
    String home() {

        log.debug("Get home page")

        return "index"
    }
}
