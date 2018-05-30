package ru.therealmone.translatorAPI.Exceptions;

public class TranslatorException extends Exception {
    public TranslatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public TranslatorException(String message) {
        super(message);
    }

    public TranslatorException(Throwable cause) {
        super(cause);
    }
}
