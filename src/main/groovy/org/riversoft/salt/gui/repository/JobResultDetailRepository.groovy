package org.riversoft.salt.gui.repository

import org.riversoft.salt.gui.domain.JobResultDetail
import org.springframework.data.mongodb.repository.MongoRepository

interface JobResultDetailRepository extends MongoRepository<JobResultDetail, String> {

//    List<JobResult> findAllByMinionIdAndJobJid(String minionId, String jobId)
}