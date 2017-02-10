package org.riversoft.salt.gui.service

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.domain.Minion
import org.riversoft.salt.gui.domain.MinionGroup
import org.riversoft.salt.gui.model.CreateMinion
import org.riversoft.salt.gui.model.view.MinionViewModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Slf4j
@Service
class MinionsActionService {

    //region injection

    @Autowired
    MinionCRUDService minionCRUDService

    @Autowired
    MinionsSaltService minionsSaltService

    //endregion

    /**
     * Принятие списка миньонов
     * @param minionNames - перечень имен миньонов
     * @param groups - перечень групп миньона
     */
    def acceptMinions(String[] minionNames, String[] groups) {

        log.debug("Start accepting [${minionNames.size()}] counts of minions.")

        List<MinionViewModel> acceptedMinions = []

        for (String minionName : minionNames) {
            acceptedMinions.add(acceptMinion(minionName, groups))
        }

        log.debug("Finish accepting [${minionNames.size()}] counts of minions.")

        return acceptedMinions
    }

    /**
     * Принятие миньона
     * @param minionName - имя миньона
     * @param groups - перечень групп миньона
     * @return объект MinionViewModel
     */
    def acceptMinion(String minionName, String[] groups) {

        log.debug("Start accepting minion [${minionName}].")

        minionsSaltService.acceptMinion(minionName)

        List<MinionGroup> minionGroups = minionCRUDService.createMinionGroups(groups)

        Minion minion = minionCRUDService.createMinion(new CreateMinion(name: minionName), minionGroups)

        log.debug("Finish accepting minion [${minionName}].")

        new MinionViewModel(minion)
    }

    /**
     * Отклонение миньонов
     * @param minionNames - перечень имен миньонов
     */
    def rejectMinion(String[] minionNames) {

        log.debug("Start rejecting [${minionNames.size()}] counts of minions.")

        for (String minionName : minionNames) {
            minionsSaltService.rejectMinion(minionName)
        }

        log.debug("Finish rejecting [${minionNames.size()}] counts of minions.")
    }


}
