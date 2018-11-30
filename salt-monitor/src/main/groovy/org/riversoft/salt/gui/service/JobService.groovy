package org.riversoft.salt.gui.service

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.domain.Job
import org.riversoft.salt.gui.domain.Minion
import org.riversoft.salt.gui.repository.JobRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Slf4j
@Service
class JobService {

    @Autowired private JobRepository jobRepository

    /**
     * Получение миньона по имени
     * @param minionName - имя меньона
     * @return объект Minion
     * @see Minion
     */
    Job getJobByJid(String jid) {

        Job job = jobRepository.findOne(jid)

        if (!job) {
            log.error("Job with jid [${jid}] not found.")
            throw new Exception("Job with jid [${jid}] not found.")
        }

        job
    }
}
