package io.github.therealmone.translatorAPI.Exceptions;

public class LexerException extends TranslatorException {
    public LexerException(String message, Throwable cause) {
        super(message, cause);
    }

    public LexerException(Throwable cause) {
        super(cause);
    }

    public LexerException(String message) {
        super(message);
    }
}
