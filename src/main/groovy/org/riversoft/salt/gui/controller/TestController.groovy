package org.riversoft.salt.gui.controller

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.AuthModule
import org.riversoft.salt.gui.calls.WheelResult
import org.riversoft.salt.gui.calls.modules.Test
import org.riversoft.salt.gui.calls.wheel.Key
import org.riversoft.salt.gui.client.SaltClient
import org.riversoft.salt.gui.datatypes.target.Glob
import org.riversoft.salt.gui.datatypes.target.Target
import org.riversoft.salt.gui.results.Result
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Slf4j
@RestController
class TestController {

    @Value('${salt.api.url}')
    private final String SALT_API_URL

    @Value('${salt.user}')
    private final String USER

    @Value('${salt.password}')
    private final String PASSWORD

    @RequestMapping('/test')
    def findScriptByName() {

        // Init the client
        SaltClient client = new SaltClient(URI.create(SALT_API_URL));

        // List accepted and pending minion keys
        WheelResult<Key.Names> keyResults = Key.listAll().callSync(
                client, USER, PASSWORD, AuthModule.PAM);
        Key.Names keys = keyResults.getData().getResult();


        // Ping all minions using a glob matcher
        Target<String> globTarget = new Glob("*");
        Map<String, Result<Boolean>> results = Test.ping().callSync(
                client, globTarget, USER, PASSWORD, AuthModule.PAM);

//        System.out.println("--> Ping results:\n");
//        results.forEach((minion, result) -> System.out.println(minion + " -> " + result));

        // Get the grains from a list of minions
//        Target<List<String>> minionList = new MinionList("minion1", "minion2");
//        Map<String, Result<Map<String, Object>>> grainResults = Grains.items(false)
//                .callSync(client, minionList, USER, PASSWORD, AuthModule.AUTO);
//
//        grainResults.forEach((minion, grains) -> {
//            System.out.println("\n--> Listing grains for '" + minion + "':\n");
//            String grainsOutput = grains.fold(
//                    error -> "Error: " + error.toString(),
//                    grainsMap -> grainsMap.entrySet().stream()
//                    .map(e -> e.getKey() + ": " + e.getValue())
//                    .collect(Collectors.joining("\n"))
//            );
//            System.out.println(grainsOutput);
//        });

        return ["accepted": keys.getMinions(), "unaccepted": keys.getUnacceptedMinions()/*, "testping" : results.find()*/]
    }

}
