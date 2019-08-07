package org.tdf4j.example.core.collections.arraylist.exceptions;

import org.tdf4j.example.core.exceptions.CollectionException;

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