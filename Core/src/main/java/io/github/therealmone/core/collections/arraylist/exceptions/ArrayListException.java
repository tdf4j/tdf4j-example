package io.github.therealmone.core.collections.arraylist.exceptions;

import io.github.therealmone.core.exceptions.CollectionException;

public class ArrayListException extends CollectionException {
    public ArrayListException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ArrayListException(final String message) {
        super(message);
    }

    public ArrayListException(final Throwable cause) {
        super(cause);
    }
}