package org.riversoft.salt.gui.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Service

@Service
class LocaleService {

    @Autowired
    private MessageSource messageSource

    String getMessage(String id, Object[] params = null) {
        Locale locale = LocaleContextHolder.getLocale()
        return messageSource.getMessage(id, params, locale)
    }
}
