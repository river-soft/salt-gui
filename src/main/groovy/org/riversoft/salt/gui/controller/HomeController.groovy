package org.riversoft.salt.gui.controller

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.service.LocaleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.i18n.SessionLocaleResolver

import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.nio.file.attribute.UserPrincipal

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
