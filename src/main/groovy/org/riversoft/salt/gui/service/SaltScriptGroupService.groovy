package org.riversoft.salt.gui.service

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.domain.SaltScriptGroup
import org.riversoft.salt.gui.exception.SaltScriptGroupNotFoundException
import org.riversoft.salt.gui.repository.SaltScriptGroupRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Slf4j
@Service
class SaltScriptGroupService {

    //region injection

    @Value('${salt.scripts.default_group:default}')
    private String defaultScriptGroup

    @Autowired
    private SaltScriptGroupRepository saltScriptGroupRepository

    //endregion

    /**
     * Создание группы скриптов
     * @param groupName - название группы скриптов
     * @return объект модели SaltScriptGroup
     * @see SaltScriptGroup
     */
    SaltScriptGroup createScriptGroup(String groupName) {

        SaltScriptGroup saltScriptGroup = null

        if (groupName) {

            saltScriptGroup = saltScriptGroupRepository.findByName(groupName)

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
     * Редактирование группы скриптов
     * @param id - уникальный номер группы скриптов
     * @param name -  название группы скриптов
     * @return объект SaltScriptGroup
     */
    SaltScriptGroup updateSaltScriptGroup(String id, String name) {

        SaltScriptGroup saltScriptGroup = saltScriptGroupRepository.findOne(id)
        if (!saltScriptGroup) {
            log.error("SaltScriptGroup with id [${id}] not found.")
            throw new SaltScriptGroupNotFoundException("SaltScriptGroup with id [${id}] not found.")
        }

        if (saltScriptGroup.name != name) {

            String oldName = saltScriptGroup.name

            log.debug("Start updating SaltScriptGroup name, from old name [${oldName}] to [${name}].")

            saltScriptGroup.name = name

            saltScriptGroupRepository.save(saltScriptGroup)

            log.debug("Successfully updated SaltScriptGroup name to [${saltScriptGroup.name}].")
        }

        saltScriptGroup
    }

    /**
     * Удаление группы скриптов
     * @param id - уникальный номер группы скриптов
     */
    def deleteSaltScriptGroup(String id) {

        SaltScriptGroup saltScriptGroup = saltScriptGroupRepository.findOne(id)
        if (!saltScriptGroup) {
            log.error("SaltScriptGroup with id [${id}] not found.")
            throw new SaltScriptGroupNotFoundException("SaltScriptGroup with id [${id}] not found.")
        }

        log.debug("Start deleting SaltScriptGroup with name [${saltScriptGroup.name}].")
        String deletedMinionGroupName = saltScriptGroup.name

        if (deletedMinionGroupName == defaultScriptGroup) {
            log.debug("No permission to delete [${deletedMinionGroupName}] group for scripts.")
            throw new Exception("No permission to delete [${deletedMinionGroupName}] group for scripts.")
        }

        int saltScriptsCount = saltScriptGroup.scriptList.size()

        if (saltScriptsCount > 0) {
            throw new Exception("Can't delete SaltScriptGroup [${saltScriptGroup.name}], group " +
                    "has [${saltScriptsCount}] scripts.")
        }

        saltScriptGroupRepository.delete(saltScriptGroup.id)

        log.debug("Successfully deleted SaltScriptGroup with name [${deletedMinionGroupName}].")
    }
}
