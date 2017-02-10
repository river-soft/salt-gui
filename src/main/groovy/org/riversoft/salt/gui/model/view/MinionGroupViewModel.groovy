package org.riversoft.salt.gui.model.view

import org.riversoft.salt.gui.domain.MinionGroup

class MinionGroupViewModel {

    String id
    String name

    MinionGroupViewModel(MinionGroup minionGroup) {
        this.id = minionGroup.id
        this.name = minionGroup.name
    }
}
