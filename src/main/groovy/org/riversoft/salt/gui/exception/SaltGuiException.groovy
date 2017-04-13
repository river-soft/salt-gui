package org.riversoft.salt.gui.exception

class SaltGuiException extends BasicSaltGuiException {

    SaltGuiException(String message, Throwable cause, int code, String localizedMessage) {
        super(message, cause)
        this.code = code
        this.localizedKey = localizedMessage
    }

    SaltGuiException(String message, Throwable cause, String localizedMessage) {
        super(message, cause)
        this.localizedKey = localizedMessage
    }

    SaltGuiException(String message, Throwable cause, String localizedMessage, Object[] params) {
        super(message, cause)
        this.localizedKey = localizedMessage
        this.params = params
    }

    SaltGuiException(String message) {
        super(message)
    }
}
