package org.tdf4j.example.core.exceptions;

public class StackMachineException extends TranslatorException {
    public StackMachineException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public StackMachineException(final Throwable cause) {
        super(cause);
    }

    public StackMachineException(final String message) {
        super(message);
    }
}
