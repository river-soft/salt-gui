package org.riversoft.salt.gui.service

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.domain.Minion
import org.riversoft.salt.gui.domain.MinionGroup
import org.riversoft.salt.gui.exception.SaltScriptAlreadyExistException
import org.riversoft.salt.gui.model.CreateMinion
import org.riversoft.salt.gui.repository.MinionGroupRepository
import org.riversoft.salt.gui.repository.MinionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Slf4j
@Service
class MinionCRUDService {

    //region injection

    @Autowired
    private MinionRepository minionRepository

    @Autowired
    private MinionGroupRepository minionGroupRepository

    //endregion

    /**
     * Создание миньона
     * @param createMinion - модель создания миньона
     * @param minionGroup - объект группы миньонов
     * @return объект модели MinionGroup
     * @see MinionGroup
     */
    Minion createMinion(CreateMinion createMinion, MinionGroup minionGroup) {

        Minion minion = minionRepository.findByName(createMinion.name)

        if (minion) {
            log.error("Minion with name [${createMinion.name}] already exist.")
            throw new SaltScriptAlreadyExistException("Minion with name [${createMinion.name}] already exist.",
                    412, "Minion with name [${createMinion.name}] already exist.")
        }

        log.debug("Start creating minion with name [${createMinion.name}].")

        //TODO подумать над записью списка групп миньонов
        minion = new Minion(name: createMinion.name, groups: [minionGroup])
        minionRepository.save(minion)

        log.debug("Successfully created minion with name [${createMinion.name}].")

        log.debug("Adding minion [${minion.name}] to group [${minionGroup.name}].")

        minionGroup.minions.add(minion)
        minionGroupRepository.save(minionGroup)

        log.debug("Successfully added minion [${minion.name}] to group [${minionGroup.name}].")

        return minion
    }

    /**
     * Создание группы миньонов
     * @param groupName - название группы миньонов
     * @return объект модели MinionGroup
     * @see MinionGroup
     */
    MinionGroup createMinionGroup(String groupName) {

        if (!groupName) {
            log.error("Illegal parameter groupName [${groupName}]")
            throw new IllegalArgumentException("Illegal parameter groupName [${groupName}]")
        }

        MinionGroup minionGroup = minionGroupRepository.findByName(groupName)

        if (!minionGroup) {

            log.debug("Start creating minion group wiht name [${groupName}].")

            minionGroup = new MinionGroup(name: groupName)
            minionGroupRepository.save(minionGroup)

            log.debug("Successfully created minion group with name [${minionGroup.name}].")
        }

        return minionGroup
    }
//
//    /**
//     * Создание группы для скриптов и скриптов
//     * @param createSaltScriptGroup - модель создания группы и скриптов группы
//     * @return объект модели SaltScriptGroupViewModel
//     * @see SaltScriptGroupViewModel
//     */
//    SaltScriptGroupViewModel createMinionGroupAndMinions(CreateSaltScriptGroup createSaltScriptGroup) {
//
//        log.debug("Start creating salt script group and scripts.")
//
//        //region проверка на наличие скриптов с одинаковыми названиями
//
//        //TODO подумать как улучшить или потом уберем?
//
//        boolean isEqualsScripts = false
//        String equalsScriptsString = ""
//
//        for (CreateSaltScript createSaltScript : createSaltScriptGroup.scripts) {
//
//            List<CreateSaltScript> equalsScripts = createSaltScriptGroup.scripts.findAll {
//                it.name == createSaltScript.name
//            }
//
//            if (equalsScripts.size() > 1) {
//                isEqualsScripts = true
//                equalsScriptsString += createSaltScript.name + ","
//            }
//        }
//
//        if (isEqualsScripts) {
//            log.error("The are scripts with equals names [${equalsScriptsString}] in list.")
//            throw new SaltScriptAlreadyExistException("The are scripts with equals names [${equalsScriptsString}] in list.")
//        }
//
//        //endregion
//
//        SaltScriptGroup saltScriptGroup = createScriptGroup(createSaltScriptGroup.group)
//
//        for (CreateSaltScript createSaltScript : createSaltScriptGroup.scripts) {
//
//            saltScriptGroup = createScript(createSaltScript, saltScriptGroup)
//        }
//
//        log.debug("Successfully created salt script group [${saltScriptGroup.name}] " +
//                "and [${saltScriptGroup.scriptList.size()}] scripts.")
//
//        new SaltScriptGroupViewModel(saltScriptGroup)
//    }
//
//    /**
//     * Поиск скрипта по его id
//     * @param id - уникальный номер скрипта
//     * @return объект SaltScriptViewModel
//     * @see SaltScriptViewModel
//     */
//    SaltScriptViewModel findMinionById(String id) {
//
//        log.debug("Start searching script with id [${id}].")
//
//        SaltScript saltScript = saltScriptRepository.findOne(id)
//        if (!saltScript) {
//            log.error("SaltScript with id [${id}] not found.")
//            throw new SaltScriptNotFoundException("SaltScript with id [${id}] not found.")
//        }
//
//        log.debug("Found script with name [${saltScript.name}].")
//
//        String fileContent = saltScriptFileService.readSaltScriptSlsFile(saltScript.filePath)
//
//        new SaltScriptViewModel(saltScript, fileContent)
//    }
//
//    /**
//     * Обновление скрипта и группы скрипта
//     * @param editSaltScript - объект модели EditSaltScript
//     * @return объект модели SaltScriptGroupViewModel
//     * @see SaltScriptGroupViewModel
//     */
//    SaltScriptGroupViewModel updateMinion(EditSaltScript editSaltScript) {
//
//        SaltScript saltScript = saltScriptRepository.findOne(editSaltScript.id)
//        if (!saltScript) {
//            log.error("SaltScript by id [${editSaltScript.id}] not found.")
//            throw new SaltScriptNotFoundException("SaltScript by id [${editSaltScript.id}] not found.")
//        }
//
//        if (editSaltScript.group != saltScript.group?.name) {
//
//            // удаление скрипта из его предыдущей группы
//            SaltScriptGroup saltScriptGroup = saltScriptGroupRepository.findOne(saltScript.group.name)
//            if (!saltScriptGroup) {
//                log.error("SaltScriptGroup by name [${saltScript.group?.name}] not found.")
//                throw new SaltScriptGroupNotFoundException("SaltScriptGroup by name [${saltScript.group?.name}] not found.")
//            }
//
//            saltScriptGroup.scriptList.removeAll { it.id == saltScript.id }
//
//            saltScriptGroupRepository.save(saltScriptGroup)
//
//            // создание новой группы скриптов
//            SaltScriptGroup newSaltScriptGroup = createScriptGroup(editSaltScript.group)
//
//            log.debug("Updating salt script with name [${saltScript.name}].")
//
//            saltScript.group = newSaltScriptGroup
//
//            log.debug("Adding script [${saltScript.name}] to group [${newSaltScriptGroup.name}].")
//
//            newSaltScriptGroup.scriptList.add(saltScript)
//            saltScriptGroupRepository.save(newSaltScriptGroup)
//
//            log.debug("Successfully added script [${saltScript.name}] to group [${newSaltScriptGroup.name}].")
//        }
//
//        String newFileName = null
//
//        if (saltScript.name != editSaltScript.name) {
//
//            log.debug("Updating name of salt script from [${saltScript.name}] to [${editSaltScript.name}].")
//
//            saltScript.name = editSaltScript.name
//
//            newFileName = editSaltScript.name
//        }
//
//        saltScript.filePath = saltScriptFileService.updateSaltScriptSlsFile(saltScript.filePath, editSaltScript.content, newFileName)
//        saltScriptRepository.save(saltScript)
//
//        log.debug("Successfully updated salt script [${saltScript.name}].")
//
//        new SaltScriptGroupViewModel(saltScript.group)
//    }
//
//    /**
//     * Удаление сприпта
//     * @param scriptId - уникальный идентификатор скрипта
//     */
//    def deleteMinion(String scriptId) {
//
//        SaltScript saltScript = saltScriptRepository.findOne(scriptId)
//        if (!saltScript) {
//            log.error("SaltScript by id [${scriptId}] not found.")
//            throw new SaltScriptNotFoundException("SaltScript by id [${scriptId}] not found.")
//        }
//
//        saltScriptFileService.deleteSaltScriptSlsFile(saltScript.filePath)
//
//        SaltScriptGroup saltScriptGroup = saltScriptGroupRepository.findOne(saltScript.group.name)
//
//        log.debug("Start deleting script with name [${saltScript.name}] from group [${saltScriptGroup.name}].")
//
//        saltScriptGroup.scriptList.removeAll { it.id == saltScript.id }
//
//        saltScriptGroupRepository.save(saltScriptGroup)
//
//        log.debug("Successfully deleted script with name [${saltScript.name}] from group [${saltScriptGroup.name}].")
//
//        log.debug("Start deleting script with name [${saltScript.name}].")
//
//        String deletedScriptName = saltScript.name
//
//        saltScriptRepository.delete(saltScript.id)
//
//        log.debug("Successfully deleted script with name [${deletedScriptName}].")
//    }

}
