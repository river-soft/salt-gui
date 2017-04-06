package org.riversoft.salt.gui.controller

import groovy.util.logging.Slf4j
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

import java.nio.file.attribute.UserPrincipal

@Slf4j
@Controller
class HomeController {

    @RequestMapping("/")
    String home() {

        log.debug("Get home page")

        return "index"
    }
}
