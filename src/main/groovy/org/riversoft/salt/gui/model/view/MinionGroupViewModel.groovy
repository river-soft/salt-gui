package org.riversoft.salt.gui.model.view

import org.riversoft.salt.gui.domain.Minion
import org.riversoft.salt.gui.domain.MinionGroup

class MinionGroupViewModel {

    String id
    String group

    List<MinionSimpleViewModel> minions = []

    MinionGroupViewModel(MinionGroup minionGroup) {

        this.id = minionGroup.id
        this.group = minionGroup.name

        for (Minion minion : minionGroup.minions) {
            this.minions.add(new MinionSimpleViewModel(minion))
        }
    }
}
