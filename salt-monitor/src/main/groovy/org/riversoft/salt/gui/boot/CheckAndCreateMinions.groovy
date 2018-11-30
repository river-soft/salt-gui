package org.riversoft.salt.gui.boot

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.domain.Minion
import org.riversoft.salt.gui.domain.MinionGroup
import org.riversoft.salt.gui.model.CreateMinion
import org.riversoft.salt.gui.model.view.MinionViewModel
import org.riversoft.salt.gui.service.MinionCRUDService
import org.riversoft.salt.gui.service.MinionGroupService
import org.riversoft.salt.gui.service.MinionsSaltService
import org.riversoft.salt.gui.service.MinionsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Slf4j
@Component
@CompileStatic
class CheckAndCreateMinions implements CommandLineRunner {

    //region injection

    @Value('${salt.minion.default_group:default}')
    private String defaultGroupForMinion

    @Autowired
    private MinionsService minionsService

    @Autowired
    private MinionCRUDService minionCRUDService

    @Autowired
    private MinionsSaltService minionsSaltService

    @Autowired
    private MinionGroupService minionGroupService

    //endregion

    @Override
    void run(String... args) throws Exception {

        def acceptedMinions = minionsSaltService.getAllAcceptedMinions()

        List<MinionViewModel> minionsInDb = minionsService.findAllAcceptedMinions()

        def minionsNamesInDb = minionsInDb.collect { it.name }

        for (String minionName : acceptedMinions) {

            if (!minionsNamesInDb.contains(minionName)) {

                log.debug("Accepted Minion on salt master with name [${minionName}] not present in database.")

                log.debug("Start creating minion with name [${minionName}] in database.")

                MinionGroup minionGroup = minionGroupService.createMinionGroup(defaultGroupForMinion)

                Minion minion = minionCRUDService.createMinion(new CreateMinion(name: minionName), [minionGroup])

                log.debug("Finish creating minion with name [${minion.name}] in database.")
            }
        }

    }
}
