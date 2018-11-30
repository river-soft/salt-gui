package org.riversoft.salt.gui.model.view

import org.riversoft.salt.gui.domain.MinionGroup

class MinionGroupSimpleViewModel {

    String id
    String name

    MinionGroupSimpleViewModel(MinionGroup minionGroup) {
        this.id = minionGroup.id
        this.name = minionGroup.name
    }
}
