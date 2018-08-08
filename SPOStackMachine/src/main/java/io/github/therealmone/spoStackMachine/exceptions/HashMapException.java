package io.github.therealmone.spoStackMachine.exceptions;

import io.github.therealmone.translatorAPI.Exceptions.StackMachineException;

public class HashMapException extends StackMachineException {
    public HashMapException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public HashMapException(final String message) {
        super(message);
    }

    public HashMapException(final Throwable cause) {
        super(cause);
    }
}
