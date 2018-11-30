package org.riversoft.salt.gui.exception

class SaltScriptNotFoundException extends BasicSaltGuiException {

    SaltScriptNotFoundException(String message, int code, String localizedMessage) {
        super(message, code)
        this.code = code
        this.localizedKey = localizedMessage
    }

    SaltScriptNotFoundException(String message) {
        super(message)
    }
}
