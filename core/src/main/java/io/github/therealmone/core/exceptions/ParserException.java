package io.github.therealmone.core.exceptions;

public class ParserException extends TranslatorException {
    public ParserException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ParserException(final Throwable cause) {
        super(cause);
    }

    public ParserException(final String message) {
        super(message);
    }
}
