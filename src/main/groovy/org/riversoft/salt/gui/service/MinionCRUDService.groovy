package org.riversoft.salt.gui.service

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.domain.Minion
import org.riversoft.salt.gui.domain.MinionGroup
import org.riversoft.salt.gui.exception.SaltScriptAlreadyExistException
import org.riversoft.salt.gui.model.CreateMinion
import org.riversoft.salt.gui.repository.MinionGroupRepository
import org.riversoft.salt.gui.repository.MinionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Slf4j
@Service
class MinionCRUDService {

    //region injection

    @Autowired
    private MinionRepository minionRepository

    @Autowired
    private MinionGroupRepository minionGroupRepository

    //endregion

    /**
     * Создание миньона
     * @param createMinion - модель создания миньона
     * @param minionGroups - перечень объектов групп миньонов
     * @return объект Minion
     * @see Minion
     */
    Minion createMinion(CreateMinion createMinion, List<MinionGroup> minionGroups) {

        Minion minion = minionRepository.findByName(createMinion.name)

        if (minion) {
            log.error("Minion with name [${createMinion.name}] already exist.")
            throw new SaltScriptAlreadyExistException("Minion with name [${createMinion.name}] already exist.",
                    412, "Minion with name [${createMinion.name}] already exist.")
        }

        log.debug("Start creating minion with name [${createMinion.name}].")

        minion = new Minion(name: createMinion.name, groups: minionGroups)
        minionRepository.save(minion)

        log.debug("Successfully created minion with name [${minion.name}].")

        for (MinionGroup minionGroup : minionGroups) {

            log.debug("Adding minion [${minion.name}] to group [${minionGroup.name}].")

            minionGroup.minions.add(minion)
            minionGroupRepository.save(minionGroup)

            log.debug("Successfully added minion [${minion.name}] to group [${minionGroup.name}].")
        }

        return minion
    }

    /**
     * Получение миньона по имени
     * @param minionName - имя меньона
     * @return
     */
    Minion getMinionByName(String minionName) {

        Minion minion = minionRepository.findByName(minionName)

        if (minion) {
            log.error("Minion with name [${minionName}] already exist.")
            throw new SaltScriptAlreadyExistException("Minion with name [${minionName}] already exist.")
        }

        minion
    }

    /**
     * Поиск миньона по имени
     * @param minionName - имя меньона
     * @return
     */
    Minion findMinionByName(String minionName) {

        Minion minion = minionRepository.findByName(minionName)

        minion
    }

    /**
     * Создание списка групп миньонов
     * @param groupNames - перечень названий групп миньонов
     * @return список объектов MinionGroup
     * @see MinionGroup
     */
    List<MinionGroup> createMinionGroups(String[] groupNames) {

        List<MinionGroup> minionGroups = []

        for (String groupName : groupNames) {

            minionGroups.add(createMinionGroup(groupName))
        }

        return minionGroups
    }

    /**
     * Создание группы миньонов
     * @param groupName - название группы миньонов
     * @return объект MinionGroup
     * @see MinionGroup
     */
    MinionGroup createMinionGroup(String groupName) {

        if (!groupName) {
            log.error("Illegal parameter groupName [${groupName}]")
            throw new IllegalArgumentException("Illegal parameter groupName [${groupName}]")
        }

        MinionGroup minionGroup = minionGroupRepository.findByName(groupName)

        if (!minionGroup) {

            log.debug("Start creating minion group wiht name [${groupName}].")

            minionGroup = new MinionGroup(name: groupName)
            minionGroupRepository.save(minionGroup)

            log.debug("Successfully created minion group with name [${minionGroup.name}].")
        }

        return minionGroup
    }

    /**
     * Обновление миньона и его групп
     * @param
     * @return
     */
    def updateMinion(def editMinion) {

        //TODO implementation
    }

    /**
     * Удаление миньона
     * @param объект Minion
     */
    def deleteMinion(Minion minion) {

        log.debug("Start deleting minion with name [${minion.name}].")

        String deletedMinionMessage = "Finish deleting minion with name [${minion.name}] and id ${minion.id}"


        for (MinionGroup group : minion.groups) {

            group.minions.removeAll { it.id == minion.id }

            minionGroupRepository.save(group)
        }

        minionRepository.delete(minion.id)

        log.debug(deletedMinionMessage)
    }

}
