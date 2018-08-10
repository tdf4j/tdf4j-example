package io.github.therealmone.spoStackMachine.exceptions;

import io.github.therealmone.core.exceptions.StackMachineException;

public class WrongTypeException extends StackMachineException {
    public WrongTypeException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public WrongTypeException(final String message) {
        super(message);
    }

    public WrongTypeException(final Throwable cause) {
        super(cause);
    }
}
