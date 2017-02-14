package org.riversoft.salt.gui.service

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.domain.Minion
import org.riversoft.salt.gui.domain.MinionGroup
import org.riversoft.salt.gui.exception.MinionGroupNotFoundException
import org.riversoft.salt.gui.model.view.MinionGroupSimpleViewModel
import org.riversoft.salt.gui.repository.MinionGroupRepository
import org.riversoft.salt.gui.repository.MinionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Slf4j
@Service
class MinionGroupService {

    //region injection

    @Autowired
    private MinionGroupRepository minionGroupRepository

    @Autowired
    private MinionRepository minionRepository

    //endregion

    /**
     * Получение списка групп миньонов
     * @return список объектов MinionGroupSimpleViewModel
     * @see MinionGroupSimpleViewModel
     */
    List<MinionGroupSimpleViewModel> findAllMinionsGroups() {

        List<MinionGroupSimpleViewModel> minionGroupViewModels = minionGroupRepository.findAll().collect {
            new MinionGroupSimpleViewModel(it)
        }

        return minionGroupViewModels
    }

    /**
     * Создание группы миньонов
     * @param groupName - название группы миньонов
     * @return объект MinionGroup
     * @see org.riversoft.salt.gui.domain.MinionGroup
     */
    MinionGroup createMinionGroup(String groupName) {

        if (!groupName) {
            log.error("Illegal parameter groupName [${groupName}]")
            throw new IllegalArgumentException("Illegal parameter groupName [${groupName}]")
        }

        MinionGroup minionGroup = minionGroupRepository.findByName(groupName)

        if (!minionGroup) {

            log.debug("Start creating minion group wiht name [${groupName}].")

            minionGroup = new MinionGroup(name: groupName.trim())
            minionGroupRepository.save(minionGroup)

            log.debug("Successfully created minion group with name [${minionGroup.name}].")
        }

        return minionGroup
    }

    /**
     * Редактирование группы миньона
     * @param id - уникальный номер группы миньона
     * @param name -  название группы миньона
     * @return объект MinionGroup
     */
    MinionGroup updateMinionGroup(String id, String name) {

        MinionGroup minionGroup = minionGroupRepository.findOne(id)
        if (!minionGroup) {
            log.error("MinionGroup with id [${id}] not found.")
            throw new MinionGroupNotFoundException("MinionGroup with id [${id}] not found.")
        }

        if (minionGroup.name != name) {

            String oldName = minionGroup.name

            log.debug("Start updating MinionGroup name, from old name [${oldName}] to [${name}].")

            minionGroup.name = name

            minionGroupRepository.save(minionGroup)

            log.debug("Successfully updated MinionGroup name to [${minionGroup.name}].")
        }

        minionGroup
    }

    /**
     * Удаление группы миньона
     * @param id - уникальный номер группы миньона
     */
    def deleteMinionGroup(String id) {

        MinionGroup minionGroup = minionGroupRepository.findOne(id)
        if (!minionGroup) {
            log.error("MinionGroup with id [${id}] not found.")
            throw new MinionGroupNotFoundException("MinionGroup with id [${id}] not found.")
        }

//            log.warn("Can't delete, MinionGroup with name [${minionGroup.name}] have [${minionsInGroup}] minions.")
//            return minionGroup.minions.collect { new MinionViewModel(it) }

        log.debug("Start deleting MinionGroup with name [${minionGroup.name}] and id [${minionGroup.id}].")
        String deletedMinionGroupName = minionGroup.name

        List<Minion> minions = minionGroup.minions

        for (Minion minion : minions) {

            minion.groups.removeAll { it.name == deletedMinionGroupName }

            if (minion.groups.size() == 0) {

                MinionGroup newMinionGroup = createMinionGroup("default")
                minion.groups.add(newMinionGroup)

                newMinionGroup.minions.add(minion)
                minionGroupRepository.save(newMinionGroup)

            }

            minionRepository.save(minion)
        }

        minionGroupRepository.delete(minionGroup.id)

        log.debug("Successfully deleted MinionGroup with name [${deletedMinionGroupName}].")
    }
}
