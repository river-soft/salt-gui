package org.riversoft.salt.gui.model.view

import org.riversoft.salt.gui.domain.Minion

class MinionViewModel {

    String id
    String name
    String groups

    MinionViewModel(Minion minion) {
        this.id = minion.id
        this.name = minion.name
        this.groups = minion.groups.collect { it.name }.join(", ")
    }
}
