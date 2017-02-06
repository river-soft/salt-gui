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
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Slf4j
@Service
class SaltScriptService {

    @Autowired
    private SaltScriptRepository scriptRepository

    @Autowired
    private SaltScriptFileService saltScriptFileService

    @Autowired
    private SaltScriptGroupRepository saltScriptGroupRepository

    @Value('${salt.scripts.directory}')
    private String scriptsDirectory

    /**
     * Получение списка всех скриптов salt
     * @return список объектов SaltScriptViewModel
     * @see SaltScriptViewModel
     */
    List<SaltScriptViewModel> findAllScripts() {

        log.debug("Start searching all script.")

        List<SaltScript> saltScripts = scriptRepository.findAll()

        log.debug("Found [${saltScripts.size()}] script.")

        saltScripts.collect { new SaltScriptViewModel(it.name) }
    }

    /**
     * Получение списка скриптов сгрупированных по группам
     * @return список объектов SaltScriptGroupViewModel
     * @see SaltScriptGroupViewModel
     */
    List<SaltScriptGroupViewModel> findAllGroupedScripts() {

        log.debug("Start searching grouped script.")

        List<SaltScriptGroup> saltScriptGroups = saltScriptGroupRepository.findAll()

        log.debug("Found [${saltScriptGroups.size()}] groups and [${saltScriptGroups.sum { it.scriptList.size() }}] script.")

        saltScriptGroups.collect { new SaltScriptGroupViewModel(it) }
    }

    /**
     * Поиск скрипта по его имени
     * @param name - название скрипта
     * @return объект SaltScriptViewModel
     * @see SaltScriptViewModel
     */
    SaltScriptViewModel findScriptByName(String name) {

        log.debug("Start searching script with name [${name}].")

        SaltScript saltScript = scriptRepository.findOne(name)
        if (!saltScript) {
            log.error("SaltScript with name [${name}] not found.")
            throw new SaltScriptNotFoundException("SaltScript with name [${name}] not found.")
        }

        log.debug("Found script with name [${name}].")

        String fileContent = saltScriptFileService.readSaltScriptSlsFile(saltScript.filePath)

        new SaltScriptViewModel(saltScript.name, fileContent)
    }
}
