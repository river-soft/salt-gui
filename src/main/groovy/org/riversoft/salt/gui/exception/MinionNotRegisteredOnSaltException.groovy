package org.riversoft.salt.gui.exception

class MinionNotRegisteredOnSaltException extends BasicSaltGuiException {

    MinionNotRegisteredOnSaltException(String message, int code, String localizedMessage) {
        super(message, code)
        this.code = code
        this.localizedKey = localizedMessage
    }

    MinionNotRegisteredOnSaltException(String message, String localizedMessage) {
        super(message)
        this.localizedKey = localizedMessage
    }

    MinionNotRegisteredOnSaltException(String message) {
        super(message)
    }
}
