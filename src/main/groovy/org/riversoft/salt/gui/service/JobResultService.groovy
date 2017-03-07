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

    def findAllJobResultsCount() {

        //TODO доделать/переделать

//        List<JobResult> results = jobResultRepository.findAll()
//
//        def resultsData = [:]
//
//        for (JobResult result : results) {
//
//            if (!result.isResult) {
//                resultsData["${result.saltScript.name}-no-connection"] = resultsData["${result.saltScript.name}-no-connection"] ? resultsData["${result.saltScript.name}-no-connection"] + 1 : 1
//
//            } else {
//
////                result.find {it.saltScript.name == "test_script"}.resultItems.findAll { it.result == false }.size()
//
//                if (result.resultItems.findAll { it.result == false && it.jobResult.saltScript.name ==  result.saltScript.name}.size() > 0) {
//
//                    resultsData["${result.saltScript.name}-false"] = resultsData["${result.saltScript.name}-false"] ? resultsData["${result.saltScript.name}-false"] + 1 : 1
//
//                } else {
//
//                    resultsData["${result.saltScript.name}-true"] = resultsData["${result.saltScript.name}-true"] ? resultsData["${result.saltScript.name}-true"] + 1 : 1
//                }
//            }
//
//            //TODO если isResult = false значит результат не пришел no connection
//            //TODO если isResult = true результ есть
//            //TODO если result.resultItems содержит хоть один false значит не выполнен
//        }
//
//        return resultsData
    }


}
