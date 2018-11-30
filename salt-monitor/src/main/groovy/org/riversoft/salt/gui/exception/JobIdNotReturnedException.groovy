package org.riversoft.salt.gui.exception

class JobIdNotReturnedException extends BasicSaltGuiException {

    JobIdNotReturnedException(String message, String localizedMessage) {
        super(message)
        this.localizedKey = localizedMessage
    }

    JobIdNotReturnedException(String message) {
        super(message)
    }
}
