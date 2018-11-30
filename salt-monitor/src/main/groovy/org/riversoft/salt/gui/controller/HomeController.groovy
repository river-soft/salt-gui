package org.riversoft.salt.gui.controller

import groovy.util.logging.Slf4j
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.RequestMapping

import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@Slf4j
@Controller
class HomeController {

    @RequestMapping("/")
    String home(@CookieValue(value = 'locale', defaultValue = "") String cookieLocale,
                HttpServletResponse response,
                Locale locale) {

        if (!cookieLocale || cookieLocale != locale.language) {
            Cookie cookie = new Cookie('locale', locale.language)
            cookie.setMaxAge(3600)
            response.addCookie(cookie)
        }

        log.debug("Get home page")

        return "index"
    }
}
