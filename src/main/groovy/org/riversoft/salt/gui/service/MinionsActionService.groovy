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
    private MinionCRUDService minionCRUDService

    @Autowired
    private MinionsSaltService minionsSaltService

    @Autowired
    private MinionGroupService minionGroupService

    //endregion

    /**
     * Принятие списка миньонов
     * @param minionNames - перечень имен миньонов
     * @param groups - перечень групп миньона.
     * @return список объектов MinionViewModel
     * @see MinionViewModel
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
     * @see MinionViewModel
     */
    def acceptMinion(String minionName, String[] groups) {

        log.debug("Start accepting minion [${minionName}].")

        minionsSaltService.acceptMinion(minionName)

        List<MinionGroup> minionGroups = minionGroupService.createMinionGroups(groups)

        Minion minion = minionCRUDService.createMinion(new CreateMinion(name: minionName.trim()), minionGroups)

        log.debug("Finish accepting minion [${minionName}].")

        new MinionViewModel(minion)
    }

    /**
     * Отклонение списка миньонов
     * @param minionNames - перечень имен миньонов
     */
    def rejectMinions(String[] minionNames) {

        log.debug("Start rejecting [${minionNames.size()}] counts of minions.")

        for (String minionName : minionNames) {
            minionsSaltService.rejectMinion(minionName)
        }

        log.debug("Finish rejecting [${minionNames.size()}] counts of minions.")
    }

    /**
     * Удаление списка миньонов
     * @param minionNames - перечень имен миньонов
     */
    def deleteMinions(String[] minionNames) {

        log.debug("Start deleting [${minionNames.size()}] counts of minions.")

        for (String minionName : minionNames) {
            deleteMinion(minionName)
        }

        log.debug("Finish deleting [${minionNames.size()}] counts of minions.")
    }

    /**
     * Удаление миньона
     * @param minionName - имя миньона
     */
    def deleteMinion(String minionName) {

        log.debug("Start deleting minion [${minionName}].")

        minionsSaltService.deleteMinion(minionName)

        Minion minion = minionCRUDService.findMinionByName(minionName)
        if (minion) {
            minionCRUDService.deleteMinion(minion)
        }

        log.debug("Finish deleting minion [${minionName}].")
    }
}
