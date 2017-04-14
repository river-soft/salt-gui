package org.riversoft.salt.gui.repository

import org.riversoft.salt.gui.domain.Minion
import org.springframework.data.mongodb.repository.MongoRepository

interface MinionRepository extends MongoRepository<Minion, String> {

//    List<Minion> findAllByHiddenIsFalse()

    Minion findByName(String name)

    int countByGroupsId(String id)
}