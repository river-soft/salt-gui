package org.riversoft.salt.gui.controller

import groovy.util.logging.Slf4j
import org.apache.http.auth.AUTH
import org.riversoft.salt.gui.AuthModule
import org.riversoft.salt.gui.calls.modules.State
import org.riversoft.salt.gui.calls.modules.Test
import org.riversoft.salt.gui.client.SaltClient
import org.riversoft.salt.gui.datatypes.target.Glob
import org.riversoft.salt.gui.datatypes.target.MinionList
import org.riversoft.salt.gui.datatypes.target.Target
import org.riversoft.salt.gui.repository.MinionGroupRepository
import org.riversoft.salt.gui.repository.SaltScriptGroupRepository
import org.riversoft.salt.gui.repository.SaltScriptRepository
import org.riversoft.salt.gui.results.Result
import org.riversoft.salt.gui.service.MinionCRUDService
import org.riversoft.salt.gui.service.MinionGroupService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

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

    @RequestMapping("/execute-script")
    def executeScript() {

        //apply было до этого
        Target<List<String>> minionList = new MinionList("minion1");

        //если для всех надо выполнить скрипт или запрос
        //Target<String> globTarget = new Glob("*");

        //был файл test
        Map<String, Result<Boolean>> results = State.apply(["test_script"]).callSync(
                saltClient, minionList, USER, PASSWORD, AuthModule.PAM);

//        LocalAsyncResult< Map<String, Result<Boolean>>> results = State.apply(["test_script"]).callAsync(
//                saltClient, minionList, USER, PASSWORD, AuthModule.PAM);

        //results.values().xor[0].right().value.find().value

//        comment
//        name
//        startTime
//        result
//        duration
//        runNum
//        changes

        def result = results.values().xor[0].right().value.find()

        log.debug("comment : ${result.value['comment']}, name: ${result.value['name']}, startTime: ${result.value['startTime']}")

//        return ["comment": result.value["comment"], "name": result.value["name"], "startTime": result.value["startTime"]]
    }

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

}
