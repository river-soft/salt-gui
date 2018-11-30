package org.riversoft.salt.gui.repository

import org.riversoft.salt.gui.domain.SaltScript
import org.springframework.data.mongodb.repository.MongoRepository

interface SaltScriptRepository extends MongoRepository<SaltScript, String> {

    List<SaltScript> findAllByGroupName(String group)

    SaltScript findByName(String name)

}