package io.github.therealmone.core.exceptions;

public class TranslatorException extends RuntimeException {
    public TranslatorException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public TranslatorException(final String message) {
        super(message);
    }

    public TranslatorException(final Throwable cause) {
        super(cause);
    }
}
