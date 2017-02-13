package org.riversoft.salt.gui.exception

class MinionNotFoundException extends BasicSaltGuiException {

    MinionNotFoundException(String message, int code, String localizedMessage) {
        super(message, code)
        this.code = code
        this.localizedKey = localizedMessage
    }

    MinionNotFoundException(String message) {
        super(message)
    }
}
