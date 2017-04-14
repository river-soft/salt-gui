package org.riversoft.salt.gui.exception

class MinionNotFoundException extends BasicSaltGuiException {

    MinionNotFoundException(String message, int code, String localizedMessage) {
        super(message, code)
        this.code = code
        this.localizedKey = localizedMessage
    }

    MinionNotFoundException(String message, String localizedMessage, Object[] params) {
        super(message)
        this.localizedKey = localizedMessage
        this.params = params
    }

    MinionNotFoundException(String message) {
        super(message)
    }
}
