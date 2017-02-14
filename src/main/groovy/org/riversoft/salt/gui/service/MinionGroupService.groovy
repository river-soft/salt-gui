package org.riversoft.salt.gui.service

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.domain.MinionGroup
import org.riversoft.salt.gui.exception.MinionGroupNotFoundException
import org.riversoft.salt.gui.model.view.MinionGroupSimpleViewModel
import org.riversoft.salt.gui.model.view.MinionGroupViewModel
import org.riversoft.salt.gui.model.view.MinionViewModel
import org.riversoft.salt.gui.repository.MinionGroupRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Slf4j
@Service
class MinionGroupService {

    //region injection

    @Autowired
    private MinionGroupRepository minionGroupRepository

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


    //TODO подумать может это не надо
    List<MinionGroupSimpleViewModel> findAllMinionGroupsByMinion(String name) {



//        List<MinionGroupSimpleViewModel> minionGroupViewModels = minionGroupRepository.findAllByMinionName(name).collect {
//            new MinionGroupSimpleViewModel(it)
//        }
//
//        return minionGroupViewModels


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
            log.error("MinionGroup by id [${id}] not found.")
            throw new MinionGroupNotFoundException("MinionGroup by id [${id}] not found.")
        }

        String oldName = minionGroup.name

        if (minionGroup.name != name) {

            log.debug("Start updating MinionGroup name, from old name [${oldName}] to [${name}].")

            minionGroup.name = name

            minionGroupRepository.save(minionGroup)

            log.debug("Successfully updated MinionGroup name to [${minionGroup.name}].")
        }

        //TODO implementation

        minionGroup
    }

    /**
     * Удаление группы миньона
     * @param id - уникальный номер группы миньона
     */
    def deleteMinionGroup(String id) {

        MinionGroup minionGroup = minionGroupRepository.findOne(id)
        if (!minionGroup) {
            log.error("MinionGroup by id [${id}] not found.")
            throw new MinionGroupNotFoundException("MinionGroup by id [${id}] not found.")
        }

        int minionsInGroup = minionGroup.minions.size()

        if (minionsInGroup > 0) {

            //TODO что надо возвращать при попытке удаления но если есть миньоны у группы ?
            log.warn("Can't delete, MinionGroup with name [${minionGroup.name}] have [${minionsInGroup}] minions.")
            return minionGroup.minions.collect { new MinionViewModel(it) }

        } else {

            log.debug("Start deleting MinionGroup with name [${minionGroup.name}] and id [${minionGroup.id}].")

            String deletedMinionGroupName = minionGroup.name

            minionGroupRepository.delete(minionGroup.id)

            log.debug("Successfully deleted MinionGroup with name [${deletedMinionGroupName}].")
        }
    }
}
