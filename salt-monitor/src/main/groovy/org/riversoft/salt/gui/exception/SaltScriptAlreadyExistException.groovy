package org.riversoft.salt.gui.exception

class SaltScriptAlreadyExistException extends BasicSaltGuiException {

    SaltScriptAlreadyExistException(String message, int code, String localizedMessage) {
        super(message, code)
        this.code = code
        this.localizedKey = localizedMessage
    }

    SaltScriptAlreadyExistException(String message) {
        super(message)
    }
}
