package org.riversoft.salt.gui.exception;

/**
 * Exception to be thrown in case of a websocket message exceeding the configurable
 * maximum message length.
 */
public class MessageTooBigException extends SaltException {

    /**
     * Constructor.
     *
     * @param maxMessageLength the maximum message length to be mentioned in the message
     */
    public MessageTooBigException(int maxMessageLength) {
        super("Message length exceeded the configured maximum (" +
                maxMessageLength + " characters)");
    }
}
