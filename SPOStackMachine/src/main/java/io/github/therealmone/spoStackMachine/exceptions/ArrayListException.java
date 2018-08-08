package io.github.therealmone.spoStackMachine.exceptions;

import io.github.therealmone.translatorAPI.Exceptions.StackMachineException;

public class ArrayListException extends StackMachineException {
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