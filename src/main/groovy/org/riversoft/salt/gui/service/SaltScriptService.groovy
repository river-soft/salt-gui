package org.riversoft.salt.gui.service

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.domain.SaltScriptGroup
import org.riversoft.salt.gui.model.view.SaltScriptGroupViewModel
import org.riversoft.salt.gui.repository.SaltScriptGroupRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Slf4j
@Service
class SaltScriptService {

    @Autowired
    private SaltScriptGroupRepository saltScriptGroupRepository

    /**
     * Получение списка скриптов сгрупированных по группам
     * @return список объектов SaltScriptGroupViewModel
     * @see SaltScriptGroupViewModel
     */
    List<SaltScriptGroupViewModel> findAllGroupedScripts() {

        log.debug("Start searching grouped script.")

        List<SaltScriptGroup> saltScriptGroups = saltScriptGroupRepository.findAll()

        log.debug("Found [${saltScriptGroups.size()}] groups and " +
                "[${saltScriptGroups.size() ? saltScriptGroups.sum { it.scriptList.size() } : 0}] script.")

        saltScriptGroups.collect { new SaltScriptGroupViewModel(it) }
    }
}
