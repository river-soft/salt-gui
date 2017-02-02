package org.riversoft.salt.gui.service

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.domain.SaltScript
import org.riversoft.salt.gui.domain.SaltScriptGroup
import org.riversoft.salt.gui.model.SaltScriptGroupViewModel
import org.riversoft.salt.gui.repository.SaltScriptGroupRepository
import org.riversoft.salt.gui.repository.SaltScriptRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Slf4j
@Service
class SaltScriptService {

    @Autowired
    SaltScriptRepository scriptRepository

    @Autowired
    SaltScriptGroupRepository saltScriptGroupRepository

    /**
     * Получение списка всех скриптов salt
     * @return список объектов SaltScript
     * @see SaltScript
     */
    List<SaltScript> findAllScripts() {

        //TODO переделать на view model
        scriptRepository.findAll()
    }

    /**
     * Получение списка скриптов сгрупированных по группам
     * @return список объектов SaltScriptGroupViewModel
     * @see SaltScriptGroupViewModel
     */
    List<SaltScriptGroupViewModel> findAllGroupedScripts() {

        List<SaltScriptGroup> saltScriptGroups = saltScriptGroupRepository.findAll()

        saltScriptGroups.collect { new SaltScriptGroupViewModel(it) }
    }

    /**
     * Поиск скрипта по его имени
     * @param name - название скрипта
     * @return объект SaltScript
     * @see SaltScript
     */
    SaltScript findScriptByName(String name) {

        //TODO переделать на view model
        scriptRepository.findOne(name)
    }

}
