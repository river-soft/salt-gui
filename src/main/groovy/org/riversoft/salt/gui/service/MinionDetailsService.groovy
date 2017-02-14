package org.riversoft.salt.gui.service

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.AuthModule
import org.riversoft.salt.gui.calls.modules.Grains
import org.riversoft.salt.gui.client.SaltClient
import org.riversoft.salt.gui.datatypes.target.MinionList
import org.riversoft.salt.gui.datatypes.target.Target
import org.riversoft.salt.gui.domain.MinionGroup
import org.riversoft.salt.gui.model.view.MinionGroupViewModel
import org.riversoft.salt.gui.repository.MinionGroupRepository
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

    @Value('${salt.minion.details.properties}')
    private String[] properties

    @Autowired
    SaltClient saltClient

    @Autowired
    MinionGroupRepository minionGroupRepository

    /**
     * Получение списка миньонов сгрупированных по группам
     * @return список объектов MinionGroupViewModel
     * @see MinionGroupViewModel
     */
    List<MinionGroupViewModel> findAllGroupedMinions() {

        log.debug("Start searching grouped minions.")

        List<MinionGroup> minionsGroups = minionGroupRepository.findAll()

        log.debug("Found [${minionsGroups.size()}] groups and " +
                "[${minionsGroups.size() ? minionsGroups.sum { it.minions.size() } : 0}] minions.")

        minionsGroups.collect { new MinionGroupViewModel(it) }
    }


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
