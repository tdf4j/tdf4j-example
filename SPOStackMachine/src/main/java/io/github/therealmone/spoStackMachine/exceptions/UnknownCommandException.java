package io.github.therealmone.spoStackMachine.exceptions;

import io.github.therealmone.translatorAPI.Exceptions.StackMachineException;
import io.github.therealmone.translatorAPI.Interfaces.ExceptionInterface;
import io.github.therealmone.translatorAPI.SavePrinter;

public class UnknownCommandException extends StackMachineException implements ExceptionInterface {
    private final String command;

    public UnknownCommandException(final String command) {
        super("Unknown command " + command);
        this.command = command;
    }

    @Override
    public void message() {
        SavePrinter.savePrintln("Unknown command " + command);
    }
}
