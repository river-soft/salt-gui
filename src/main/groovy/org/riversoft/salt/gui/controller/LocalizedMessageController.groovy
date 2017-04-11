package org.riversoft.salt.gui.controller

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Slf4j
@RestController
class LocalizedMessageController extends BaseRestController {

    @Autowired
    MessageSource messageSource

    @RequestMapping("/bundle-messages")
    getBundleMessages() {

        List<String> locales = ["ru", "ua", "en"]
        Map<String, Map<String, String>> messages = [:]

        for (String locale : locales) {
            Map<String, String> map = new HashMap<>()
            ResourceBundle resourceBundle = ResourceBundle.getBundle("messages", new Locale(locale))

            resourceBundle.keySet().findAll { it.startsWith("client") }.each {
                map.put(it, new String(messageSource.getMessage(it, null, new Locale(locale)).bytes, 'UTF-8'))
            }

            messages.put(locale, map)
        }

        messages
    }
}
