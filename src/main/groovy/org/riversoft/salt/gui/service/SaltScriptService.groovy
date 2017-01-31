package org.riversoft.salt.gui.service

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.domain.SaltScript
import org.riversoft.salt.gui.repository.SaltScriptRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Slf4j
@Service
class SaltScriptService {

    @Autowired
    SaltScriptRepository scriptRepository

    /**
     * Получение списка всех скриптов salt
     * @return список скриптов объекта SaltScript
     * @see SaltScript
     */
    List<SaltScript> findAllScripts() {

        scriptRepository.findAll()
    }

    /**
     * Поиск скрипта по его имени
     * @param name - название скрипта
     * @return объекта SaltScript
     * @see SaltScript
     */
    SaltScript findScriptByName(String name) {
        scriptRepository.findOne(name)
    }

}
