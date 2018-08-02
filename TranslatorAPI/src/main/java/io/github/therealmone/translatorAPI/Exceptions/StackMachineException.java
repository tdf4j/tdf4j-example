package io.github.therealmone.translatorAPI.Exceptions;

public class StackMachineException extends TranslatorException {
    public StackMachineException(String message, Throwable cause) {
        super(message, cause);
    }

    public StackMachineException(Throwable cause) {
        super(cause);
    }

    public StackMachineException(String message) {
        super(message);
    }
}
