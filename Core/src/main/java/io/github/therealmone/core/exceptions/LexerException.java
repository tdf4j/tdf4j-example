package io.github.therealmone.core.exceptions;

public class LexerException extends TranslatorException {
    public LexerException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public LexerException(final Throwable cause) {
        super(cause);
    }

    public LexerException(final String message) {
        super(message);
    }
}