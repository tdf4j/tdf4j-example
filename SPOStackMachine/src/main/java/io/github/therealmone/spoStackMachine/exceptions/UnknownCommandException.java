package io.github.therealmone.spoStackMachine.exceptions;

import io.github.therealmone.translatorAPI.Exceptions.StackMachineException;
import io.github.therealmone.translatorAPI.Interfaces.ExceptionInterface;
import io.github.therealmone.translatorAPI.SavePrinter;

public class UnknownCommandException extends StackMachineException implements ExceptionInterface {
    private String command;

    public UnknownCommandException(String command) {
        super("Unknown command " + command);
        this.command = command;
    }

    @Override
    public void message() {
        SavePrinter.savePrintln("Unknown command " + command);
    }
}
