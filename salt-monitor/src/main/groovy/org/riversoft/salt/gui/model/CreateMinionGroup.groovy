package org.riversoft.salt.gui.model

import groovy.transform.ToString

@ToString(includePackage = false, includeNames = true)
class CreateMinionGroup {

    String group
    List<CreateMinion> minions = []
}
