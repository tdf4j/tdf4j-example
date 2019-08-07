package org.tdf4j.example.stackmachine.exceptions;

import org.tdf4j.example.core.exceptions.StackMachineException;

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
