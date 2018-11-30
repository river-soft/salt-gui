package org.riversoft.salt.gui.model.view

import org.riversoft.salt.gui.domain.Minion

class MinionSimpleViewModel {

    String id
    String name

    MinionSimpleViewModel(Minion minion) {
        this.id = minion.id
        this.name = minion.name
    }
}
