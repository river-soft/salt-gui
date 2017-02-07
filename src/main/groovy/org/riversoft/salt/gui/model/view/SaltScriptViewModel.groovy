package org.riversoft.salt.gui.model.view

import org.riversoft.salt.gui.domain.SaltScript

class SaltScriptViewModel {

    String id
    String name
    String content

    SaltScriptViewModel(SaltScript saltScript) {
        this.id = saltScript.id
        this.name = saltScript.name
    }

    SaltScriptViewModel(SaltScript saltScript, String fileContent) {
        this.id = saltScript.id
        this.name = saltScript.name
        this.content = fileContent
    }
}
