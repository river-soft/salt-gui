package org.riversoft.salt.gui.service

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.domain.MinionGroup
import org.riversoft.salt.gui.model.view.MinionGroupSimpleViewModel
import org.riversoft.salt.gui.model.view.MinionGroupViewModel
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
        //TODO implementation
    }

    /**
     * Удаление группы миньона
     * @param id - уникальный номер группы миньона
     */
    def deleteMinionGroup(String id) {
        //TODO implementation
    }
}
