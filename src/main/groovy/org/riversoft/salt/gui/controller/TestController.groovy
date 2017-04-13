package org.riversoft.salt.gui.controller

import groovy.util.logging.Slf4j
import org.apache.http.auth.AUTH
import org.riversoft.salt.gui.AuthModule
import org.riversoft.salt.gui.calls.modules.State
import org.riversoft.salt.gui.calls.modules.Test
import org.riversoft.salt.gui.client.SaltClient
import org.riversoft.salt.gui.datatypes.target.Glob
import org.riversoft.salt.gui.datatypes.target.Target
import org.riversoft.salt.gui.enums.Permission
import org.riversoft.salt.gui.model.CreateMinion
import org.riversoft.salt.gui.repository.MinionGroupRepository
import org.riversoft.salt.gui.repository.SaltScriptGroupRepository
import org.riversoft.salt.gui.repository.SaltScriptRepository
import org.riversoft.salt.gui.repository.UserRepository
import org.riversoft.salt.gui.results.Result
import org.riversoft.salt.gui.service.MinionCRUDService
import org.riversoft.salt.gui.service.MinionGroupService
import org.riversoft.salt.gui.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.MessageSource
import org.springframework.context.support.MessageSourceResourceBundle
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PathVariable
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
    UserService userService

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

        for (int i = 0; i < 50; i++) {

            minionCRUDService.createMinion(new CreateMinion(name: "minion${i}${i}"), [group])
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ROOT')")
    @RequestMapping("/create-user/{user}")
    createUser(@PathVariable('user') String user) {
        switch (user) {
            case 'reviewer':
                userService.createUser(user, user, 'Reviewer', 'Zytvjueybxtujdsgjkyznm', Permission.reviever())
                break
            case 'scriptMaster':
                userService.createUser(user, user, 'ScriptMaster', 'Crhbgnsvjtdct', Permission.scriptMaster())
                break
            case 'minionMaster':
                userService.createUser(user, user, 'minionMaster', 'Herjdjlbntkmvbymjyjd', Permission.minionMaster())
                break
            case 'executor':
                userService.createUser(user, user, 'executor', 'Pfgeoedctxnjldbuftncz', Permission.executor())
                break
            default:
                return
        }
    }
}
