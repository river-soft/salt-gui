package org.riversoft.salt.gui.model.view

import org.riversoft.salt.gui.domain.JobResult

class JobResultViewModel {

    String id
    String jid
    String minionName
    String minionGroups
    boolean isResult
    String status

    JobResultViewModel(JobResult jobResult) {
        this.id = jobResult.id
        this.isResult = jobResult.isResult
        this.minionName = jobResult.minion.name
        this.jid = jobResult.job.jid
        this.minionGroups = jobResult.minion.groups.collect { it.name }.join(",")

        if (!jobResult.isResult) {
            this.status = "no connected"
        } else {
            if (jobResult.jobResultDetails.findAll { !it.result }.size() > 0) {
                this.status = "false"
            } else {
                this.status = "true"
            }
        }
    }
}
