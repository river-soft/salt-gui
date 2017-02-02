package org.riversoft.salt.gui.model

import org.riversoft.salt.gui.domain.SaltScript

class SaltScriptViewModel {

    String name
    String content

    SaltScriptViewModel(SaltScript saltScript) {
        this.name = saltScript.name
        this.content = saltScript.content
    }
}
