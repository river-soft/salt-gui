package org.riversoft.salt.gui.repository

import org.riversoft.salt.gui.domain.JobResultItem
import org.springframework.data.mongodb.repository.MongoRepository

interface JobResultItemRepository extends MongoRepository<JobResultItem, String> {

//    List<JobResult> findAllByMinionIdAndJobJid(String minionId, String jobId)
}