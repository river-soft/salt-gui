package org.riversoft.salt.gui.repository

import org.riversoft.salt.gui.domain.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository extends MongoRepository<User, String> {

}