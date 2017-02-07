package org.riversoft.salt.gui.model.view

import org.riversoft.salt.gui.domain.SaltScript

class SaltScriptViewModel {

    String id
    String name

    SaltScriptViewModel(SaltScript saltScript) {
        this.id = saltScript.id
        this.name = saltScript.name
    }
}
