package org.riversoft.salt.gui.service

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.domain.SaltScript
import org.riversoft.salt.gui.domain.SaltScriptGroup
import org.riversoft.salt.gui.exception.SaltScriptAlreadyExistException
import org.riversoft.salt.gui.model.CreateSaltScript
import org.riversoft.salt.gui.model.CreateSaltScriptGroup
import org.riversoft.salt.gui.model.view.SaltScriptGroupViewModel
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
    private SaltScriptRepository saltScriptRepository

    @Autowired
    private SaltScriptFileService saltScriptFileService

    @Autowired
    private SaltScriptGroupRepository saltScriptGroupRepository

    @Value('${salt.scripts.default_group}')
    private String defaultGroup

    /**
     * Создание группы для скриптов
     * @param groupName - название группы
     * @return объект SaltScriptGroupViewModel
     * @see SaltScriptGroupViewModel
     */
    SaltScriptGroupViewModel createSaltScriptGroupAndScripts(CreateSaltScriptGroup createSaltScriptGroup) {

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

            SaltScript saltScript = saltScriptRepository.findOne(createSaltScript.name)
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

}
