package org.riversoft.salt.gui.controller

import groovy.util.logging.Slf4j
import org.apache.http.auth.AUTH
import org.riversoft.salt.gui.AuthModule
import org.riversoft.salt.gui.calls.modules.State
import org.riversoft.salt.gui.calls.modules.Test
import org.riversoft.salt.gui.client.SaltClient
import org.riversoft.salt.gui.datatypes.target.Glob
import org.riversoft.salt.gui.datatypes.target.Target
import org.riversoft.salt.gui.model.CreateMinion
import org.riversoft.salt.gui.repository.MinionGroupRepository
import org.riversoft.salt.gui.repository.SaltScriptGroupRepository
import org.riversoft.salt.gui.repository.SaltScriptRepository
import org.riversoft.salt.gui.results.Result
import org.riversoft.salt.gui.service.MinionCRUDService
import org.riversoft.salt.gui.service.MinionGroupService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.MessageSource
import org.springframework.context.support.MessageSourceResourceBundle
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

import java.nio.charset.Charset

@Slf4j
@RestController
class TestController {

    @Value('${salt.api.url}')
    private String SALT_API_URL

    @Value('${salt.user}')
    private String USER

    @Value('${salt.password}')
    private String PASSWORD

    @Autowired
    SaltScriptRepository saltScriptRepository

    @Autowired
    SaltScriptGroupRepository saltScriptGroupRepository

    @Autowired
    MinionCRUDService minionCRUDService

    @Autowired
    MinionGroupRepository minionGroupRepository

    @Autowired
    MinionGroupService minionGroupService

    @Autowired
    SaltClient saltClient

    @Autowired
    MessageSource messageSource

    @RequestMapping('/ping')
    @ResponseBody
    def findScriptByName() {

        // Ping all minions using a glob matcher
        Target<String> globTarget = new Glob("*");
        Map<String, Result<Boolean>> results1 = Test.ping().callSync(
                saltClient, globTarget, USER, PASSWORD, AuthModule.PAM);

        // CMD RUN
//        Target<List<String>> minionList = new MinionList("minion1");
//        Map<String, Result<Boolean>> results = Cmd.run("uname -a").callSync(
//                client, minionList, USER, PASSWORD, AuthModule.PAM);

    }

    @RequestMapping('/minionsss')
    def minionsssss() {

        def group = minionGroupService.createMinionGroup('default')

        for(int i = 0; i < 50; i++) {

            minionCRUDService.createMinion(new CreateMinion(name: "minion${i}${i}"), [group])
        }
    }

    @RequestMapping("/bundle-messages")
    getBundleMessages() {

        List<String> locales = ["ru", "ua", "en"]
        Map<String, Map<String, String>> messages = [:]

        for(String locale: locales) {
            Map<String, String> map = new HashMap<>()
            ResourceBundle resourceBundle = ResourceBundle.getBundle("messages", new Locale(locale))

            resourceBundle.keySet().findAll {it.startsWith("client")}.each {
                map.put(it, new String(messageSource.getMessage(it, null, new Locale(locale)).bytes, 'UTF-8'))
            }

            messages.put(locale, map)
        }

        messages
    }
}
