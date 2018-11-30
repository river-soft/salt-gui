package org.riversoft.salt.gui.exception

class SaltScriptGroupNotFoundException extends BasicSaltGuiException {

    SaltScriptGroupNotFoundException(String message, int code, String localizedMessage) {
        super(message, code)
        this.code = code
        this.localizedKey = localizedMessage
    }

    SaltScriptGroupNotFoundException(String message) {
        super(message)
    }
}
