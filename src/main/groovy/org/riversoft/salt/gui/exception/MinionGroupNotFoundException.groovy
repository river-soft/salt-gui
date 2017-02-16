package org.riversoft.salt.gui.exception

class MinionGroupNotFoundException extends BasicSaltGuiException {

    MinionGroupNotFoundException(String message, int code, String localizedMessage) {
        super(message, code)
        this.code = code
        this.localizedKey = localizedMessage
    }

    MinionGroupNotFoundException(String message) {
        super(message)
    }
}
