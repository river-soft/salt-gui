package org.riversoft.salt.gui.controller

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.exception.BasicSaltGuiException
import org.riversoft.salt.gui.service.LocaleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

import java.lang.reflect.UndeclaredThrowableException

@Slf4j
@RestController
abstract class BaseRestController {

    @Autowired
    MessageSource messageSource

    @Autowired
    LocaleService localeService

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception)
    def errorHandler(Exception e) {

        e = (e instanceof UndeclaredThrowableException) ? e.getCause() : e

        log.error(e.message, e)

        def localizedMessage = e.localizedMessage
        if (e instanceof BasicSaltGuiException) {

            try {

                localizedMessage = localeService.getMessage(e.localizedKey, e.params)

            } catch (Exception ex) {

                log.warn(ex.message)
            }
        }

        [
                timestamp: new Date(),
                status   : HttpStatus.INTERNAL_SERVER_ERROR.value(),
                error    : e.message,
                exception: e.class.toString(),
                message  : localizedMessage,
                code     : e instanceof BasicSaltGuiException ? e.code : 0
        ]
    }
}
