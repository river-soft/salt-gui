package org.riversoft.salt.gui.service

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.domain.SaltScript
import org.riversoft.salt.gui.domain.SaltScriptGroup
import org.riversoft.salt.gui.exception.SaltScriptAlreadyExistException
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
     * Создание группы для скриптов
     * @param groupName - название группы
     * @return объект SaltScriptGroupViewModel
     * @see SaltScriptGroupViewModel
     */
    SaltScriptGroupViewModel createSaltScriptGroupAndScripts(CreateSaltScriptGroup createSaltScriptGroup) {


        //region проверка на наличие скриптов с одинаковыми названиями

        //TODO подумать как улучшить или потом уберем?

        boolean isEqualsScripts = false

        for (CreateSaltScript createSaltScript : createSaltScriptGroup.scripts) {

            int equalsScriptsCount = createSaltScriptGroup.scripts.findAll { it.name == createSaltScript.name }.size()
            //todo может вытаскивтаь имена одинаковых названий?
            if (equalsScriptsCount > 1) {
                isEqualsScripts = true
            }
        }

        if (isEqualsScripts) {
            log.error("The are scripts with equals names in list.")
            throw new SaltScriptAlreadyExistException("The are scripts with equals names in list.")
        }

        //endregion


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

            SaltScript saltScript = saltScriptRepository.findByName(createSaltScript.name)
            if (saltScript) {
                log.error("Salt script with name [${createSaltScript.name}] already exist.")
                throw new SaltScriptAlreadyExistException("Salt script with name [${createSaltScript.name}] already exist.",
                        412, "Salt script with name [${createSaltScript.name}] already exist.")
            }

            //создание sls файла на сервере salt
            String filePath = saltScriptFileService.createSaltScriptSlsFile(createSaltScript.name, createSaltScript.content)

            log.debug("Start creating salt script with name [${createSaltScript.name}].")

            saltScript = new SaltScript(name: createSaltScript.name, filePath: filePath, group: saltScriptGroup)
            saltScriptRepository.save(saltScript)

            log.debug("Successfully created salt script with name [${createSaltScript.name}].")

            saltScriptGroup.scriptList.add(saltScript)
        }

        saltScriptGroupRepository.save(saltScriptGroup)

        new SaltScriptGroupViewModel(saltScriptGroup)
    }

    /**
     * Поиск скрипта по его id
     * @param id - уникальный номер скрипта
     * @return объект SaltScriptViewModel
     * @see org.riversoft.salt.gui.model.view.SaltScriptViewModel
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

            SaltScriptGroup saltScriptGroup = saltScriptGroupRepository.findOne(saltScript.group.name)

            saltScriptGroup.scriptList.removeAll { it.id == saltScript.id }

            saltScriptGroupRepository.save(saltScriptGroup)

            SaltScriptGroup newSaltScriptGroup = saltScriptGroupRepository.findOne(editSaltScript.group)

            if (!newSaltScriptGroup) {

                log.debug("Start creating salt script group wiht name [${editSaltScript.group}].")

                newSaltScriptGroup = new SaltScriptGroup(name: editSaltScript.group)
                saltScriptGroupRepository.save(saltScriptGroup)

                log.debug("Successfully created salt script group with name [${editSaltScript.group}].")
            }

            saltScript.group = newSaltScriptGroup

            saltScriptRepository.save(saltScript)

            newSaltScriptGroup.scriptList.add(saltScript)
            saltScriptGroupRepository.save(newSaltScriptGroup)
        }

        String newFileName = null

        if (saltScript.name != editSaltScript.name) {

            saltScript.name = editSaltScript.name
            saltScriptRepository.save(saltScript)

            newFileName = editSaltScript.name
        }

        saltScript.filePath = saltScriptFileService.updateSaltScriptSlsFile(saltScript.filePath, editSaltScript.content, newFileName)
        saltScriptRepository.save(saltScript)

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
