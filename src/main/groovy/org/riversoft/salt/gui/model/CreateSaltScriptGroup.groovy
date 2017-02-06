package org.riversoft.salt.gui.model

import groovy.transform.ToString

@ToString(includePackage = false, includeNames = true)
class CreateSaltScriptGroup {

    String group
    List<CreateSaltScript> scripts = []
}
