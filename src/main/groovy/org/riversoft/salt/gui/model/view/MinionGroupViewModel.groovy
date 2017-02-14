package org.riversoft.salt.gui.model.view

import org.riversoft.salt.gui.domain.Minion
import org.riversoft.salt.gui.domain.MinionGroup

class MinionGroupViewModel {

    String group

    List<MinionsViewModel> minions = []

    MinionGroupViewModel(MinionGroup minionGroup) {

        this.group = minionGroup.name

        for (Minion minion : minionGroup.minions) {
            this.minions.add(new MinionsViewModel(minion))
        }
    }
}
