package org.riversoft.salt.gui.model

import org.riversoft.salt.gui.domain.SaltScript
import org.riversoft.salt.gui.domain.SaltScriptGroup

class SaltScriptGroupViewModel {

    String group
    List<SaltScriptViewModel> scripts = []

    SaltScriptGroupViewModel(SaltScriptGroup saltScriptGroup) {

        this.group = saltScriptGroup.name

        for (SaltScript saltScript : saltScriptGroup.scriptList) {
            this.scripts.add(new SaltScriptViewModel(name: saltScript.name, content: saltScript.content))
        }
    }
}
