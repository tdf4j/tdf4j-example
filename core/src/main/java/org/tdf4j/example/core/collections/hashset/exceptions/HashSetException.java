package org.tdf4j.example.core.collections.hashset.exceptions;

import org.tdf4j.example.core.exceptions.CollectionException;

public class HashSetException extends CollectionException {
    public HashSetException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public HashSetException(final String message) {
        super(message);
    }

    public HashSetException(final Throwable cause) {
        super(cause);
    }
}
