package org.riversoft.salt.gui.service

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.domain.SaltScript
import org.riversoft.salt.gui.domain.SaltScriptGroup
import org.riversoft.salt.gui.exception.SaltScriptNotFoundException
import org.riversoft.salt.gui.model.CreateSaltScript
import org.riversoft.salt.gui.model.CreateSaltScriptGroup
import org.riversoft.salt.gui.model.view.SaltScriptGroupViewModel
import org.riversoft.salt.gui.model.view.SaltScriptViewModel
import org.riversoft.salt.gui.repository.SaltScriptGroupRepository
import org.riversoft.salt.gui.repository.SaltScriptRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Slf4j
@Service
class SaltScriptGroupService {

    @Autowired
    private SaltScriptService saltScriptService

    @Autowired
    private SaltScriptFileService saltScriptFileService

    @Autowired
    private SaltScriptRepository saltScriptRepository

    @Autowired
    private SaltScriptGroupRepository saltScriptGroupRepository

    @Value('${salt.scripts.default_group}')
    private String defaultGroup

    /**
     * Получение списка всех скриптов salt
     * @return список объектов SaltScriptViewModel
     * @see SaltScriptViewModel
     */
    List<SaltScriptViewModel> findAllScripts() {

        log.debug("Start searching all script.")

        List<SaltScript> saltScripts = scriptRepository.findAll()

        log.debug("Found [${saltScripts.size()}] script.")

        saltScripts.collect { new SaltScriptViewModel(it) }
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

        new SaltScriptViewModel(saltScript)
    }

    /**
     * Создание группы для скриптов
     * @param groupName - название группы
     * @return объект SaltScriptGroupViewModel
     * @see SaltScriptGroupViewModel
     */
    SaltScriptGroupViewModel createSaltScriptGroup(CreateSaltScriptGroup createSaltScriptGroup) {

        SaltScriptGroup saltScriptGroup = null

        if (createSaltScriptGroup.group) {

            saltScriptGroup = saltScriptGroupRepository.findOne(createSaltScriptGroup.group)

            if (!saltScriptGroup) {

                log.debug("Start creating salt script group wiht name [${createSaltScriptGroup.group}].")

                saltScriptGroup = new SaltScriptGroup(name: createSaltScriptGroup.group)
                saltScriptGroupRepository.save(saltScriptGroup)

                log.debug("Successfully created salt script group with name [${saltScriptGroup.name}].")
            }

        } else {

            saltScriptGroup = saltScriptGroupRepository.findOne(defaultGroup)

            if (!saltScriptGroup) {

                log.debug("Start creating default salt script group with name [${defaultGroup}].")

                saltScriptGroup = new SaltScriptGroup(name: "default")
                saltScriptGroupRepository.save(saltScriptGroup)

                log.debug("Successfully created default salt script group with name [${defaultGroup}].")
            }
        }

        for (CreateSaltScript createSaltScript : createSaltScriptGroup.scripts) {

            //TODO проверка существует ли такой файл уже в БД
            SaltScript saltScript = saltScriptRepository.findOne(createSaltScript.name)
            if (saltScript) {
                log.error("Salt script with name [${createSaltScript.name}] already exist.")
                throw new SaltScriptNotFoundException("Salt script with name [${createSaltScript.name}] already exist.")
            }

            //создание sls файла на сервере salt
            String filePath = saltScriptFileService.createSaltScriptSlsFile(createSaltScript.name, createSaltScript.content)

            log.trace("Start creating salt script with name [${createSaltScript.name}].")

            saltScript = new SaltScript(name: createSaltScript.name, filePath: filePath, group: saltScriptGroup)
            saltScriptRepository.save(saltScript)

            log.trace("Successfully created salt script with name [${createSaltScript.name}].")

            saltScriptGroup.scriptList.add(saltScript)
        }

        saltScriptGroupRepository.save(saltScriptGroup)

        new SaltScriptGroupViewModel(saltScriptGroup)
    }

}
