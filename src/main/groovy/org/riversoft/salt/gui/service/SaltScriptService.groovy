package org.riversoft.salt.gui.service

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.domain.SaltScript
import org.riversoft.salt.gui.domain.SaltScriptGroup
import org.riversoft.salt.gui.exception.SaltScriptAlreadyExistException
import org.riversoft.salt.gui.exception.SaltScriptGroupNotFoundException
import org.riversoft.salt.gui.exception.SaltScriptNotFoundException
import org.riversoft.salt.gui.model.CreateSaltScript
import org.riversoft.salt.gui.model.CreateSaltScriptGroup
import org.riversoft.salt.gui.model.EditSaltScript
import org.riversoft.salt.gui.model.view.SaltScriptGroupViewModel
import org.riversoft.salt.gui.model.view.SaltScriptViewModel
import org.riversoft.salt.gui.repository.SaltScriptGroupRepository
import org.riversoft.salt.gui.repository.SaltScriptRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

import java.text.SimpleDateFormat

@Slf4j
@Service
class SaltScriptService {

    //region injection

    @Autowired
    private SaltScriptRepository saltScriptRepository

    @Autowired
    private SaltScriptFileService saltScriptFileService

    @Autowired
    private SaltScriptGroupService saltScriptGroupService

    @Autowired
    private SaltScriptGroupRepository saltScriptGroupRepository

    //endregion

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

    /**
     * Создание скрипта
     * @param createSaltScript - модель создания скрипта
     * @param saltScriptGroup - объект группы скрипта
     * @return объект модели SaltScriptGroup
     * @see SaltScriptGroup
     */
    SaltScriptGroup createScript(CreateSaltScript createSaltScript, SaltScriptGroup saltScriptGroup) {

        SaltScript saltScript = saltScriptRepository.findByName(createSaltScript.name)
        if (saltScript) {
            log.error("Salt script with name [${createSaltScript.name}] already exist.")
            throw new SaltScriptAlreadyExistException("Salt script with name [${createSaltScript.name}] already exist.",
                    412, "Salt script with name [${createSaltScript.name}] already exist.")
        }

        log.debug("Start creating salt script with name [${createSaltScript.name}].")

        saltScript = new SaltScript(name: createSaltScript.name.trim(), content: createSaltScript.content, group: saltScriptGroup)

        saltScript.createDate = new Date()
        saltScript.lastModifiedDate = new Date()
        saltScriptRepository.save(saltScript)

        log.debug("Successfully created salt script with name [${createSaltScript.name}].")

        log.debug("Adding script [${saltScript.name}] to group [${saltScriptGroup.name}].")

        saltScriptGroup.scriptList.add(saltScript)
        saltScriptGroupRepository.save(saltScriptGroup)

        log.debug("Successfully added script [${saltScript.name}] to group [${saltScriptGroup.name}].")

        return saltScriptGroup
    }

    /**
     * Создание группы для скриптов и скриптов
     * @param createSaltScriptGroup - модель создания группы и скриптов группы
     * @return объект модели SaltScriptGroupViewModel
     * @see SaltScriptGroupViewModel
     */
    SaltScriptGroupViewModel createSaltScriptGroupAndScripts(CreateSaltScriptGroup createSaltScriptGroup) {

        log.debug("Start creating salt script group and scripts.")

        //region проверка на наличие скриптов с одинаковыми названиями

        //TODO подумать как улучшить или потом уберем?

        boolean isEqualsScripts = false
        String equalsScriptsString = ""

        for (CreateSaltScript createSaltScript : createSaltScriptGroup.scripts) {

            List<CreateSaltScript> equalsScripts = createSaltScriptGroup.scripts.findAll {
                it.name == createSaltScript.name
            }

            if (equalsScripts.size() > 1) {
                isEqualsScripts = true
                equalsScriptsString += createSaltScript.name + ","
            }
        }

        if (isEqualsScripts) {
            log.error("The are scripts with equals names [${equalsScriptsString}] in list.")
            throw new SaltScriptAlreadyExistException("The are scripts with equals names [${equalsScriptsString}] in list.")
        }

        //endregion

        SaltScriptGroup saltScriptGroup = saltScriptGroupService.createScriptGroup(createSaltScriptGroup.group)

        for (CreateSaltScript createSaltScript : createSaltScriptGroup.scripts) {

            saltScriptGroup = createScript(createSaltScript, saltScriptGroup)
        }

        log.debug("Successfully created salt script group [${saltScriptGroup.name}] " +
                "and [${saltScriptGroup.scriptList.size()}] scripts.")

        new SaltScriptGroupViewModel(saltScriptGroup)
    }

    /**
     * Поиск скрипта по его id
     * @param id - уникальный номер скрипта
     * @return объект SaltScriptViewModel
     * @see SaltScriptViewModel
     */
    SaltScriptViewModel findScriptById(String id) {

        log.debug("Start searching script with id [${id}].")

        SaltScript saltScript = saltScriptRepository.findOne(id)
        if (!saltScript) {
            log.error("SaltScript with id [${id}] not found.")
            throw new SaltScriptNotFoundException("SaltScript with id [${id}] not found.")
        }

        log.debug("Found script with name [${saltScript.name}].")

        new SaltScriptViewModel(saltScript)
    }

    /**
     * Обновление скрипта и группы скрипта
     * @param editSaltScript - объект модели EditSaltScript
     * @return объект модели SaltScriptGroupViewModel
     * @see SaltScriptGroupViewModel
     */
    SaltScriptGroupViewModel updateSaltScript(EditSaltScript editSaltScript) {

        SaltScript saltScript = saltScriptRepository.findOne(editSaltScript.id)
        if (!saltScript) {
            log.error("SaltScript by id [${editSaltScript.id}] not found.")
            throw new SaltScriptNotFoundException("SaltScript by id [${editSaltScript.id}] not found.")
        }

        if (editSaltScript.group != saltScript.group?.name) {

            // удаление скрипта из его предыдущей группы
            SaltScriptGroup saltScriptGroup = saltScriptGroupRepository.findByName(saltScript.group.name.trim())
            if (!saltScriptGroup) {
                log.error("SaltScriptGroup by name [${saltScript.group?.name}] not found.")
                throw new SaltScriptGroupNotFoundException("SaltScriptGroup by name [${saltScript.group?.name}] not found.")
            }

            saltScriptGroup.scriptList.removeAll { it.id == saltScript.id }

            saltScriptGroupRepository.save(saltScriptGroup)

            // создание новой группы скриптов
            SaltScriptGroup newSaltScriptGroup = saltScriptGroupService.createScriptGroup(editSaltScript.group.trim())

            log.debug("Updating salt script with name [${saltScript.name}].")

            saltScript.group = newSaltScriptGroup

            log.debug("Adding script [${saltScript.name}] to group [${newSaltScriptGroup.name}].")

            newSaltScriptGroup.scriptList.add(saltScript)
            saltScriptGroupRepository.save(newSaltScriptGroup)

            log.debug("Successfully added script [${saltScript.name}] to group [${newSaltScriptGroup.name}].")
        }

        if (saltScript.name != editSaltScript.name) {

            log.debug("Updating name of salt script from [${saltScript.name}] to [${editSaltScript.name}].")

            saltScript.name = editSaltScript.name.trim()
        }

        saltScript.lastModifiedDate = new Date()
        saltScript.content = editSaltScript.content

        saltScriptRepository.save(saltScript)

        log.debug("Successfully updated salt script [${saltScript.name}].")

        new SaltScriptGroupViewModel(saltScript.group)
    }

    /**
     * Удаление скрипта
     * @param scriptId - уникальный идентификатор скрипта
     */
    def deleteSaltScript(String scriptId) {

        SaltScript saltScript = saltScriptRepository.findOne(scriptId)
        if (!saltScript) {
            log.error("SaltScript by id [${scriptId}] not found.")
            throw new SaltScriptNotFoundException("SaltScript by id [${scriptId}] not found.")
        }

        SaltScriptGroup saltScriptGroup = saltScriptGroupRepository.findByName(saltScript.group.name)

        log.debug("Start deleting script with name [${saltScript.name}] from group [${saltScriptGroup.name}].")

        saltScriptGroup.scriptList.removeAll { it.id == saltScript.id }

        saltScriptGroupRepository.save(saltScriptGroup)

        log.debug("Successfully deleted script with name [${saltScript.name}] from group [${saltScriptGroup.name}].")

        log.debug("Start deleting script with name [${saltScript.name}].")

        String deletedScriptName = saltScript.name

        saltScriptRepository.delete(saltScript.id)

        log.debug("Successfully deleted script with name [${deletedScriptName}].")
    }

    /**
     * Получение скрипта по его названию
     * @param name - название скрипта
     * @return объект SaltScript
     * @see SaltScript
     */
    SaltScript getSaltScriptByName(String name) {

        SaltScript saltScript = saltScriptRepository.findByName(name)
        if (!saltScript) {
            log.error("SaltScript with name [${name}] not found.")
            throw new SaltScriptNotFoundException("SaltScript with name [${name}] not found.")
        }

        saltScript
    }

}
