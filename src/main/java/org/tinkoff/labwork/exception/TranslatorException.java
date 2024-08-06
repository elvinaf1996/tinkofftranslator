package org.tinkoff.labwork.exception;

public class TranslatorException extends RuntimeException {

    /**
     * TranslatorException.
     *
     * @param message - Сообщение об ошибке.
     */
    public TranslatorException(final String message) {
        super(message);
    }

    /**
     * TranslatorException.
     *
     * @param message - Сообщение об ошибке.
     * @param cause   - Причина
     */
    public TranslatorException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * TranslatorException.
     *
     * @param cause - Причина
     */
    public TranslatorException(final Throwable cause) {
        super(cause);
    }
}