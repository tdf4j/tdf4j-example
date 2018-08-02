package io.github.therealmone.spoStackMachine.exceptions;

import io.github.therealmone.translatorAPI.Exceptions.StackMachineException;

public class WrongTypeException extends StackMachineException {
    public WrongTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongTypeException(String message) {
        super(message);
    }

    public WrongTypeException(Throwable cause) {
        super(cause);
    }
}
