package org.riversoft.salt.gui.service

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.AuthModule
import org.riversoft.salt.gui.calls.WheelResult
import org.riversoft.salt.gui.calls.modules.State
import org.riversoft.salt.gui.calls.wheel.Key
import org.riversoft.salt.gui.client.SaltClient
import org.riversoft.salt.gui.datatypes.target.MinionList
import org.riversoft.salt.gui.datatypes.target.Target
import org.riversoft.salt.gui.results.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Slf4j
@Service
class ExecuteScriptService {

    //region injection

    @Value('${salt.user}')
    private String USER

    @Value('${salt.password}')
    private String PASSWORD

    @Autowired
    private SaltClient saltClient

    //endregion

    /**
     * Выполнение списка скриптов для списка миньонов
     * @param minions - список миньонов
     * @param scripts - список скриптов
     * @return
     */
    def executeScripts(String[] minions, String[] scripts) {

        //TODO собрать массив названий скриптов на выполнение или же принимать прямо с фронта список из названий?

        //apply было до этого
        Target<List<String>> minionList = new MinionList(minions);

        //если для всех надо выполнить скрипт или запрос
        //Target<String> globTarget = new Glob("*");

        //был файл test
        Map<String, Result<Boolean>> results = State.apply(scripts).callSync(
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


}
