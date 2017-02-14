package org.riversoft.salt.gui.model.view

import org.riversoft.salt.gui.domain.Minion

class MinionsViewModel {

    String id
    String name

    MinionsViewModel(Minion minion) {
        this.id = minion.id
        this.name = minion.name
    }
}
