package ru.therealmone.spoStackMachine.exceptions;

import ru.therealmone.translatorAPI.Exceptions.StackMachineException;

public class HashMapException extends StackMachineException {
    public HashMapException(String message, Throwable cause) {
        super(message, cause);
    }

    public HashMapException(String message) {
        super(message);
    }

    public HashMapException(Throwable cause) {
        super(cause);
    }
}
