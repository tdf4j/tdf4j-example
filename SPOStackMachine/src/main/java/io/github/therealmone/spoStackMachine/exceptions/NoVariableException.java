package io.github.therealmone.spoStackMachine.exceptions;

import io.github.therealmone.translatorAPI.Exceptions.StackMachineException;

public class NoVariableException extends StackMachineException {
    public NoVariableException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoVariableException(String message) {
        super(message);
    }

    public NoVariableException(Throwable cause) {
        super(cause);
    }
}
