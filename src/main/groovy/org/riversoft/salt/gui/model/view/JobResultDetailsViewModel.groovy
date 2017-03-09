package org.riversoft.salt.gui.model.view

import org.riversoft.salt.gui.domain.JobResultDetail
import org.riversoft.salt.gui.utils.DateTimeParser

class JobResultDetailsViewModel {


    String id
    String cmd
    String comment
    String name
    String startTime
    boolean result
    Double duration
    String changes
    String description
    String minionName
    String jobName
    String lastModifiedDate

    JobResultDetailsViewModel(JobResultDetail jobResultDetail) {

        this.id = jobResultDetail.id
        this.cmd = jobResultDetail.cmd
        this.comment = jobResultDetail.comment
        this.name = jobResultDetail.name
        this.startTime = jobResultDetail.startTime
        this.result = jobResultDetail.result
        this.duration = jobResultDetail.duration
        this.changes = jobResultDetail.changes
        this.description = jobResultDetail.description
        this.jobName = jobResultDetail.jobResult.job.name
        this.minionName = jobResultDetail.jobResult.minion.name
        this.lastModifiedDate = DateTimeParser.dateToString(jobResultDetail.lastModifiedDate)
    }
}
