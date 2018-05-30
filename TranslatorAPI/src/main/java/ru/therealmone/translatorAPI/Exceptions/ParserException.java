package ru.therealmone.translatorAPI.Exceptions;

public class ParserException extends TranslatorException {
    public ParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParserException(Throwable cause) {
        super(cause);
    }

    public ParserException(String message) {
        super(message);
    }
}
