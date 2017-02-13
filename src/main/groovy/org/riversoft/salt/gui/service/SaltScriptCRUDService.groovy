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

@Slf4j
@Service
class SaltScriptCRUDService {

    //region injection

    @Autowired
    private SaltScriptRepository saltScriptRepository

    @Autowired
    private SaltScriptFileService saltScriptFileService

    @Autowired
    private SaltScriptGroupRepository saltScriptGroupRepository

    @Value('${salt.scripts.default_group}')
    private String defaultGroup

    //endregion

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

        //создание sls файла на сервере salt
        String filePath = saltScriptFileService.createSaltScriptSlsFile(createSaltScript.name.trim(), createSaltScript.content)

        log.debug("Start creating salt script with name [${createSaltScript.name}].")

        saltScript = new SaltScript(name: createSaltScript.name.trim(), filePath: filePath, group: saltScriptGroup)
        saltScriptRepository.save(saltScript)

        log.debug("Successfully created salt script with name [${createSaltScript.name}].")

        log.debug("Adding script [${saltScript.name}] to group [${saltScriptGroup.name}].")

        saltScriptGroup.scriptList.add(saltScript)
        saltScriptGroupRepository.save(saltScriptGroup)

        log.debug("Successfully added script [${saltScript.name}] to group [${saltScriptGroup.name}].")

        return saltScriptGroup
    }

    /**
     * Создание группы скриптов
     * @param groupName - название группы скриптов
     * @return объект модели SaltScriptGroup
     * @see SaltScriptGroup
     */
    SaltScriptGroup createScriptGroup(String groupName) {

        SaltScriptGroup saltScriptGroup = null

        if (groupName) {

            saltScriptGroup = saltScriptGroupRepository.findOne(groupName)

            if (!saltScriptGroup) {

                log.debug("Start creating salt script group wiht name [${groupName}].")

                saltScriptGroup = new SaltScriptGroup(name: groupName.trim())
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

        SaltScriptGroup saltScriptGroup = createScriptGroup(createSaltScriptGroup.group)

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

        String fileContent = saltScriptFileService.readSaltScriptSlsFile(saltScript.filePath)

        new SaltScriptViewModel(saltScript, fileContent)
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
            SaltScriptGroup saltScriptGroup = saltScriptGroupRepository.findOne(saltScript.group.name.trim())
            if (!saltScriptGroup) {
                log.error("SaltScriptGroup by name [${saltScript.group?.name}] not found.")
                throw new SaltScriptGroupNotFoundException("SaltScriptGroup by name [${saltScript.group?.name}] not found.")
            }

            saltScriptGroup.scriptList.removeAll { it.id == saltScript.id }

            saltScriptGroupRepository.save(saltScriptGroup)

            // создание новой группы скриптов
            SaltScriptGroup newSaltScriptGroup = createScriptGroup(editSaltScript.group.trim())

            log.debug("Updating salt script with name [${saltScript.name}].")

            saltScript.group = newSaltScriptGroup

            log.debug("Adding script [${saltScript.name}] to group [${newSaltScriptGroup.name}].")

            newSaltScriptGroup.scriptList.add(saltScript)
            saltScriptGroupRepository.save(newSaltScriptGroup)

            log.debug("Successfully added script [${saltScript.name}] to group [${newSaltScriptGroup.name}].")
        }

        String newFileName = null

        if (saltScript.name != editSaltScript.name) {

            log.debug("Updating name of salt script from [${saltScript.name}] to [${editSaltScript.name}].")

            saltScript.name = editSaltScript.name.trim()

            newFileName = editSaltScript.name.trim()
        }

        saltScript.filePath = saltScriptFileService.updateSaltScriptSlsFile(saltScript.filePath, editSaltScript.content, newFileName)
        saltScriptRepository.save(saltScript)

        log.debug("Successfully updated salt script [${saltScript.name}].")

        new SaltScriptGroupViewModel(saltScript.group)
    }

    /**
     * Удаление сприпта
     * @param scriptId - уникальный идентификатор скрипта
     */
    def deleteSaltScript(String scriptId) {

        SaltScript saltScript = saltScriptRepository.findOne(scriptId)
        if (!saltScript) {
            log.error("SaltScript by id [${scriptId}] not found.")
            throw new SaltScriptNotFoundException("SaltScript by id [${scriptId}] not found.")
        }

        saltScriptFileService.deleteSaltScriptSlsFile(saltScript.filePath)

        SaltScriptGroup saltScriptGroup = saltScriptGroupRepository.findOne(saltScript.group.name)

        log.debug("Start deleting script with name [${saltScript.name}] from group [${saltScriptGroup.name}].")

        saltScriptGroup.scriptList.removeAll { it.id == saltScript.id }

        saltScriptGroupRepository.save(saltScriptGroup)

        log.debug("Successfully deleted script with name [${saltScript.name}] from group [${saltScriptGroup.name}].")

        log.debug("Start deleting script with name [${saltScript.name}].")

        String deletedScriptName = saltScript.name

        saltScriptRepository.delete(saltScript.id)

        log.debug("Successfully deleted script with name [${deletedScriptName}].")
    }

}
