package org.riversoft.salt.gui.exception

class BasicSaltGuiException extends Exception {

    final static def NOT_FOUND = 400

    final static def INTERNAL_SERVER_ERROR = 500
    final static def DEFAULT_CODE = INTERNAL_SERVER_ERROR

    /***
     * Внутренний код ошибки
     */
    int code

    /**
     * Код локализированного сообщения
     */
    String localizedKey

    /**
     * Constructs a new exception with {@code null} as its detail text.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    BasicSaltGuiException(int code = DEFAULT_CODE) {

        super()
        this.code = code
    }

    /**
     * Constructs a new exception with the specified detail text.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail text. The detail text is saved for
     *          later retrieval by the {@link #getMessage()} method.
     */
    BasicSaltGuiException(String message, int code = DEFAULT_CODE) {

        super(message)
        this.code = code
    }

    /**
     * Constructs a new exception with the specified detail text and
     * cause.  <p>Note that the detail text associated with
     * {@code cause} is <i>not</i> automatically incorporated in
     * this exception's detail text.
     *
     * @param message the detail text (which is saved for later retrieval
     *         by the {@link #getMessage()} method).
     * @param cause the cause (which is saved for later retrieval by the
     * {@link #getCause()} method).  (A <tt>null</tt> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     * @since 1.4
     */
    BasicSaltGuiException(String message, Throwable cause, int code = DEFAULT_CODE) {

        super(message, cause)
        this.code = code
    }

    /**
     * Constructs a new exception with the specified cause and a detail
     * text of <tt>(cause==null ? null : cause.toString())</tt> (which
     * typically contains the class and detail text of <tt>cause</tt>).
     * This constructor is useful for exceptions that are little more than
     * wrappers for other throwables (for example, {@link
     * java.security.PrivilegedActionException}).
     *
     * @param cause the cause (which is saved for later retrieval by the
     * {@link #getCause()} method).  (A <tt>null</tt> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     * @since 1.4
     */
    BasicSaltGuiException(Throwable cause, int code = DEFAULT_CODE) {

        super(cause)
        this.code = code
    }

    /**
     * Constructs a new exception with the specified detail text,
     * cause, suppression enabled or disabled, and writable stack
     * trace enabled or disabled.
     *
     * @param message the detail text.
     * @param cause the cause.  (A {@code null} value is permitted,
     * and indicates that the cause is nonexistent or unknown.)
     * @param enableSuppression whether or not suppression is enabled
     *                          or disabled
     * @param writableStackTrace whether or not the stack trace should
     *                           be writable
     * @since 1.7
     */
    protected BasicSaltGuiException(String message, Throwable cause, boolean enableSuppression,
                                    boolean writableStackTrace, int code = DEFAULT_CODE) {

        super(message, cause, enableSuppression, writableStackTrace)
        this.code = code
    }

    /**
     * Creates a localized description of this throwable.
     * Subclasses may override this method in order to produce a
     * locale-specific text.  For subclasses that do not override this
     * method, the default implementation returns the same result as
     * {@code getMessage ( )}.
     *
     * @return The localized description of this throwable.
     * @since JDK1.1
     */
    @Override
    String getLocalizedMessage() {

        return super.getLocalizedMessage()
    }
}
