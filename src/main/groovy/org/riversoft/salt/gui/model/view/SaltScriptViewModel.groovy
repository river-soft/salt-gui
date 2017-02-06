package org.riversoft.salt.gui.model.view

class SaltScriptViewModel {

    String name
    String content

    SaltScriptViewModel(String fileName, String fileContent = "") {
        this.name = fileName
        this.content = fileContent
    }
}
