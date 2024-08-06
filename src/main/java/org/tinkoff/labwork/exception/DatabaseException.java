package org.tinkoff.labwork.exception;

public class DatabaseException extends RuntimeException {

    /**
     * DatabaseException.
     *
     * @param message - Сообщение об ошибке.
     */
    public DatabaseException(final String message) {
        super(message);
    }

    /**
     * DatabaseException.
     *
     * @param message - Сообщение об ошибке.
     * @param cause   - Причина
     */
    public DatabaseException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * DatabaseException.
     *
     * @param cause - Причина
     */
    public DatabaseException(final Throwable cause) {
        super(cause);
    }
}