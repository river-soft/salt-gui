package org.riversoft.salt.gui.repository

import org.riversoft.salt.gui.domain.SaltScript
import org.springframework.data.mongodb.repository.MongoRepository

interface ScriptRepository extends MongoRepository<SaltScript, String> {
}