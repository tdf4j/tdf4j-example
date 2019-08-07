package org.tdf4j.example.core.exceptions;

public class CollectionException extends TranslatorException {
    public CollectionException(String message) {
        super(message);
    }

    public CollectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public CollectionException(Throwable cause) {
        super(cause);
    }
}
