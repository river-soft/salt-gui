package org.riversoft.salt.gui.controller

import groovy.util.logging.Slf4j
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Slf4j
@RestController
class TestController {

    @RequestMapping('/test')
    def test(){
        def test = ["set":"sgdg", "sgsg":"sggd"]
        return test
    }
}
