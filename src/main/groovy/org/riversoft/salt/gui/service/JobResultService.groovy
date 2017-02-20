package org.riversoft.salt.gui.service

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.domain.JobResult
import org.riversoft.salt.gui.domain.Minion
import org.riversoft.salt.gui.repository.JobResultRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Slf4j
@Service
class JobResultService {

    //region injection

    @Autowired
    private JobResultRepository jobResultRepository

    //endregion

}
