package org.riversoft.salt.gui.service

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.domain.Minion
import org.riversoft.salt.gui.domain.MinionGroup
import org.riversoft.salt.gui.exception.MinionNotFoundException
import org.riversoft.salt.gui.exception.SaltScriptAlreadyExistException
import org.riversoft.salt.gui.model.CreateMinion
import org.riversoft.salt.gui.model.EditMinion
import org.riversoft.salt.gui.model.view.MinionViewModel
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
     * Обновление миньона т.е. его групп
     * @param editMinion - объект EditMinion
     * @return
     */
    def updateMinion(EditMinion editMinion) {

        Minion minion = minionRepository.findByName(editMinion.name)

        List<String> minionsGroups = minion.groups.collect { it.name }

        List<String> newMinionsGroups = editMinion.groups.collect { it.name }

        //добавление не достающей группы
        for (String newGroup : newMinionsGroups) {

            if (!minionsGroups.contains(newGroup)) {

                MinionGroup minionGroup = minionGroupRepository.findByName(newGroup)

                log.debug("Start adding group [${minionGroup.name}] to minion [${minion.name}].")

                minion.groups.add(minionGroup)

                minionRepository.save(minion)

                minionGroup.minions.add(minion)
                minionGroupRepository.save(minionGroup)

                log.debug("Finish adding group [${minionGroup.name}] to minion [${minion.name}].")
            }
        }

        //удаление не нужной группы
        for (String group : minionsGroups) {

            if (!newMinionsGroups.contains(group)) {

                MinionGroup minionGroup = minionGroupRepository.findByName(group)

                log.debug("Start deleting group [${minionGroup.name}] from minion [${minion.name}].")

                minion.groups.removeAll { it.name == group }

                minionRepository.save(minion)

                minionGroup.minions.removeAll { it.name == minion.name }
                minionGroupRepository.save(minionGroup)

                log.debug("Finish deleting group [${minionGroup.name}] from minion [${minion.name}].")
            }
        }

        new MinionViewModel(minion)
    }

    /**
     * Удаление миньона
     * @param объект Minion
     */
    def deleteMinion(Minion minion) {

        log.debug("Start deleting minion with name [${minion.name}].")

        String deletedMinionMessage = "Finish deleting minion with name [${minion.name}] and id [${minion.id}]."

        for (MinionGroup group : minion.groups) {

            group.minions.removeAll { it.id == minion.id }

            minionGroupRepository.save(group)
        }

        minionRepository.delete(minion.id)

        log.debug(deletedMinionMessage)
    }

}
