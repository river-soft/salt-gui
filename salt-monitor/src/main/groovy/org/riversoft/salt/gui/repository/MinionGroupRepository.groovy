package org.riversoft.salt.gui.repository

import org.riversoft.salt.gui.domain.MinionGroup
import org.springframework.data.mongodb.repository.MongoRepository

interface MinionGroupRepository extends MongoRepository<MinionGroup, String> {

    MinionGroup findByName(String name)
}