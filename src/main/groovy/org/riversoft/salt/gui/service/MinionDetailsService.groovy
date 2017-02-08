package org.riversoft.salt.gui.service

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.AuthModule
import org.riversoft.salt.gui.calls.modules.Grains
import org.riversoft.salt.gui.client.SaltClient
import org.riversoft.salt.gui.datatypes.target.MinionList
import org.riversoft.salt.gui.datatypes.target.Target
import org.riversoft.salt.gui.results.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Slf4j
@Service
class MinionDetailsService {

    @Value('${salt.user}')
    private String USER

    @Value('${salt.password}')
    private String PASSWORD

    @Value('${minion.details.properties}')
    private String[] properties

    @Autowired
    SaltClient saltClient

    def findMinionDetails(List<String> minionsNames) {

        // Set targets
        Target<List<String>> minionList = new MinionList(minionsNames);

        // call Grains.item
        Map<String, Result<Map<String, Object>>> grainResults = Grains.item(false, properties)
                .callSync(saltClient, minionList, USER, PASSWORD, AuthModule.PAM);

        // get result of Grains.item
        def result = grainResults.collect { ["${it.key}": it.value?.xor?.right()?.value] }

        return result
    }

}
