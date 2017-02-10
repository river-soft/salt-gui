package org.riversoft.salt.gui.service

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.domain.MinionGroup
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
     * Создание списка групп миньонов
     * @param groupNames - перечень названий групп миньонов
     * @return список объектов MinionGroup
     * @see MinionGroup
     */
    List<MinionGroupViewModel> findAllMinionsGroups() {

        List<MinionGroupViewModel> minionGroupViewModels = minionGroupRepository.findAll().collect {
            new MinionGroupViewModel(it)
        }

        return minionGroupViewModels
    }


}
