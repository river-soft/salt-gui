package org.riversoft.salt.gui.service

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.domain.Minion
import org.riversoft.salt.gui.domain.MinionGroup
import org.riversoft.salt.gui.exception.MinionNotFoundException
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

    @Autowired
    MinionGroupService minionGroupService

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

        minion = new Minion(name: createMinion.name.trim(), groups: minionGroups)
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
     * @return объект Minion
     * @see Minion
     */
    Minion getMinionByName(String minionName) {

        Minion minion = minionRepository.findByName(minionName)

        if (!minion) {
            log.error("Minion with name [${minionName}] not found.")
            throw new MinionNotFoundException("Minion with name [${minionName}] not found.")
        }

        minion
    }

    /**
     * Поиск миньона по имени
     * @param minionName - имя меньона
     * @return объект Minion
     * @see Minion
     */
    Minion findMinionByName(String minionName) {

        minionRepository.findByName(minionName)
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

            minionGroups.add(minionGroupService.createMinionGroup(groupName))
        }

        return minionGroups
    }

    /**
     * Обновление миньона и его групп
     * @param
     * @return
     */
    def updateMinion(def editMinion) {

        //TODO implementation смена групп миньона
    }

    /**
     * Удаление миньона
     * @param объект Minion
     */
    def deleteMinion(Minion minion) {

        log.debug("Start deleting minion with name [${minion.name}].")

        String deletedMinionMessage = "Finish deleting minion with name [${minion.name}] and id х${minion.id}ъ."

        for (MinionGroup group : minion.groups) {

            group.minions.removeAll { it.id == minion.id }

            minionGroupRepository.save(group)
        }

        minionRepository.delete(minion.id)

        log.debug(deletedMinionMessage)
    }

}
