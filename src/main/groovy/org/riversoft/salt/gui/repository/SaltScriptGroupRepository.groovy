package org.riversoft.salt.gui.repository

import org.riversoft.salt.gui.domain.SaltScriptGroup
import org.springframework.data.mongodb.repository.MongoRepository

interface SaltScriptGroupRepository extends MongoRepository<SaltScriptGroup, String> {

}