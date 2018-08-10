package io.github.therealmone.spoStackMachine.exceptions;

import io.github.therealmone.core.exceptions.StackMachineException;
import io.github.therealmone.core.interfaces.ExceptionInterface;
import io.github.therealmone.core.utils.SavePrinter;

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
