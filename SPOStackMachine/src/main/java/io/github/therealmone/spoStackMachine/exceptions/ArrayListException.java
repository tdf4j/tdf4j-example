package io.github.therealmone.spoStackMachine.exceptions;

import io.github.therealmone.translatorAPI.Exceptions.StackMachineException;

public class ArrayListException extends StackMachineException {
    public ArrayListException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArrayListException(String message) {
        super(message);
    }

    public ArrayListException(Throwable cause) {
        super(cause);
    }
}