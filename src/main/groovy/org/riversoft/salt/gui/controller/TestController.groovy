package org.riversoft.salt.gui.controller

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.AuthModule
import org.riversoft.salt.gui.calls.WheelResult
import org.riversoft.salt.gui.calls.modules.Grains
import org.riversoft.salt.gui.calls.wheel.Key
import org.riversoft.salt.gui.client.SaltClient
import org.riversoft.salt.gui.datatypes.target.Glob
import org.riversoft.salt.gui.datatypes.target.MinionList
import org.riversoft.salt.gui.datatypes.target.Target
import org.riversoft.salt.gui.domain.SaltScript
import org.riversoft.salt.gui.domain.SaltScriptGroup
import org.riversoft.salt.gui.repository.SaltScriptGroupRepository
import org.riversoft.salt.gui.repository.SaltScriptRepository
import org.riversoft.salt.gui.results.Result
import org.riversoft.salt.gui.service.SaltScriptService
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
    SaltScriptService saltScriptService

    @RequestMapping('/test')
    @ResponseBody
    def findScriptByName() {

        // Init the client
        SaltClient client = new SaltClient(URI.create(SALT_API_URL));

        // List accepted and pending minion keys
        WheelResult<Key.Names> keyResults = Key.listAll().callSync(
                client, USER, PASSWORD, AuthModule.PAM);
        Key.Names keys = keyResults.getData().getResult();

        // Ping all minions using a glob matcher
//        Target<String> globTarget = new Glob("*");
//        Map<String, Result<Boolean>> results1 = Test.ping().callSync(
//                client, globTarget, USER, PASSWORD, AuthModule.PAM);

        // CMD RUN
//        Target<List<String>> minionList = new MinionList("minion1");
//        Map<String, Result<Boolean>> results = Cmd.run("uname -a").callSync(
//                client, minionList, USER, PASSWORD, AuthModule.PAM);

        //STATE APPLY

//        String script = "{run echo comand from cmd: {cmd.run: [{name:uname -a}]}}"
        String script = "run echo comand from cmd:\n" +
                "  cmd.run:\n" +
                "    - name: uname -a"

        //apply было до этого
//        Target<List<String>> minionList = new MinionList("minion1");
//        //был файл test
//        Map<String, Result<Boolean>> results = State.apply([script]).callSync(
//                client, minionList, USER, PASSWORD, AuthModule.PAM);


        // Get the grains from a list of minions
        Target<List<String>> minionList1 = new MinionList("minion1", "minion1");
//        Map<String, Result<Map<String, Object>>> grainResults = Grains.items(false)
//                .callSync(client, minionList1, USER, PASSWORD, AuthModule.PAM);

        Target<String> globTarget = new Glob("*");

        Map<String, Result<Map<String, Object>>> grainResults = Grains.item(false, "cpu_model", "ipv4", "os")
                .callSync(client, globTarget/*minionList1*/, USER, PASSWORD, AuthModule.PAM);

        //grainResults.collect { e -> [ "${e.key}" : e.value]}

//        for (def grains : grainResults) {
//            test.push(grains.collect { ["${it.key}": it.value] })
//        }
//
//        String grainsOutput = grains.fold(
//                error -> "Error: " + error.toString(),
//                grainsMap -> grainsMap.entrySet().stream()
//                .map(e -> e.getKey() + ": " + e.getValue())
//                .collect(Collectors.joining("\n"))
//        );

        def test = grainResults.collect { ["${it.key}": it.value.xor.right().value] }


        return test
        //["accepted": keys.getMinions(), "unaccepted": keys.getUnacceptedMinions()/*, "testping" : results.find()*/]
    }

    @RequestMapping('/generate')
    def generateScripts() {

        int i = 1

        for (i; i <= 5; i++) {

            SaltScriptGroup saltScriptGroup = new SaltScriptGroup(name: "group${i}")
            saltScriptGroupRepository.save(saltScriptGroup)

            SaltScript saltScript = new SaltScript(name: "script${i}", content: "script number ${i} for execution of some thing ${i}", group: saltScriptGroup)
            saltScriptRepository.save(saltScript)

            SaltScript saltScript1 = new SaltScript(name: "script${i}0", content: "script number ${i} for execution of some thing ${i}", group: saltScriptGroup)
            saltScriptRepository.save(saltScript1)


            saltScriptGroup.scriptList.add(saltScript)
            saltScriptGroup.scriptList.add(saltScript1)
            saltScriptGroupRepository.save(saltScriptGroup)

        }
    }

}
