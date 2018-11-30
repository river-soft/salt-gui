package org.riversoft.salt.gui.repository

import org.riversoft.salt.gui.domain.JobResult
import org.springframework.data.mongodb.repository.MongoRepository

interface JobResultRepository extends MongoRepository<JobResult, String> {

//    List<JobResult> findAllByMinionIdAndJobJid(String minionId, String jobId)
    JobResult findAllByMinionIdAndJobJid(String minionId, String jobId)

    List<JobResult> findAllByJobJid(String jobId)

    List<JobResult> findAllByIdIn(String[] ids)

    List<JobResult> findAllByMinionId(String minionId)
}