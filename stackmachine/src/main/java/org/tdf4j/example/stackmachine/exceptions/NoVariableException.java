package org.tdf4j.example.stackmachine.exceptions;

import org.tdf4j.example.core.exceptions.StackMachineException;

public class NoVariableException extends StackMachineException {
    public NoVariableException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NoVariableException(final String message) {
        super(message);
    }

    public NoVariableException(final Throwable cause) {
        super(cause);
    }
}
