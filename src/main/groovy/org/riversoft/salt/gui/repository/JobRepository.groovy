package org.riversoft.salt.gui.repository

import org.riversoft.salt.gui.domain.Job
import org.springframework.data.mongodb.repository.MongoRepository

interface JobRepository extends MongoRepository<Job, String> {

    List<Job> findAllByDoneFalse()
}