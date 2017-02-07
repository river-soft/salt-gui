package org.riversoft.salt.gui.service

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.domain.SaltScript
import org.riversoft.salt.gui.domain.SaltScriptGroup
import org.riversoft.salt.gui.exception.SaltScriptNotFoundException
import org.riversoft.salt.gui.model.view.SaltScriptGroupViewModel
import org.riversoft.salt.gui.model.view.SaltScriptViewModel
import org.riversoft.salt.gui.repository.SaltScriptGroupRepository
import org.riversoft.salt.gui.repository.SaltScriptRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Slf4j
@Service
class SaltScriptService {

    @Autowired
    private SaltScriptRepository saltScriptRepository

    @Autowired
    private SaltScriptFileService saltScriptFileService

    @Autowired
    private SaltScriptGroupRepository saltScriptGroupRepository

    /**
     * Поиск скрипта по его id
     * @param id - уникальный номер скрипта
     * @return объект SaltScriptViewModel
     * @see SaltScriptViewModel
     */
    SaltScriptViewModel findScriptById(String id) {

        log.debug("Start searching script with name [${id}].")

        SaltScript saltScript = saltScriptRepository.findOne(id)
        if (!saltScript) {
            log.error("SaltScript with id [${id}] not found.")
            throw new SaltScriptNotFoundException("SaltScript with id [${id}] not found.")
        }

        log.debug("Found script with name [${saltScript.name}].")

        String fileContent = saltScriptFileService.readSaltScriptSlsFile(saltScript.filePath)

        new SaltScriptViewModel(saltScript, fileContent)
    }

    /**
     * Получение списка всех скриптов salt
     * @return список объектов SaltScriptViewModel
     * @see SaltScriptViewModel
     */
//    List<SaltScriptViewModel> findAllScripts() {
//
//        log.debug("Start searching all script.")
//
//        List<SaltScript> saltScripts = saltScriptRepository.findAll()
//
//        log.debug("Found [${saltScripts.size()}] script.")
//
//        saltScripts.collect { new SaltScriptViewModel(it.name) }
//    }

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
