package org.riversoft.salt.gui.repository

import org.riversoft.salt.gui.domain.Minion
import org.springframework.data.mongodb.repository.MongoRepository

interface MinionRepository extends MongoRepository<Minion, String> {

    Minion findByName(String name)
}