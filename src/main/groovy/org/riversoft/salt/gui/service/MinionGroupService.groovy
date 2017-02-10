package org.riversoft.salt.gui.service

import groovy.util.logging.Slf4j
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
     * @return список объектов MinionGroupViewModel
     * @see MinionGroupViewModel
     */
    List<MinionGroupViewModel> findAllMinionsGroups() {

        List<MinionGroupViewModel> minionGroupViewModels = minionGroupRepository.findAll().collect {
            new MinionGroupViewModel(it)
        }

        return minionGroupViewModels
    }


}
