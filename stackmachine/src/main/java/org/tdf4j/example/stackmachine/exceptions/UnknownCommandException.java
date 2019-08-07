package org.tdf4j.example.stackmachine.exceptions;

import org.tdf4j.example.core.exceptions.StackMachineException;
import org.tdf4j.example.core.IException;
import org.tdf4j.example.core.utils.SavePrinter;

public class UnknownCommandException extends StackMachineException implements IException {
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
