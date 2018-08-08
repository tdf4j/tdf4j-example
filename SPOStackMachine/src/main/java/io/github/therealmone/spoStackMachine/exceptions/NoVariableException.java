package io.github.therealmone.spoStackMachine.exceptions;

import io.github.therealmone.translatorAPI.Exceptions.StackMachineException;

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
