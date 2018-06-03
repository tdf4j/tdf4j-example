package ru.therealmone.spoStackMachine.exceptions;

import ru.therealmone.translatorAPI.Exceptions.StackMachineException;
import ru.therealmone.translatorAPI.Interfaces.ExceptionInterface;

public class UnknownCommandException extends StackMachineException implements ExceptionInterface {
    private String command;

    public UnknownCommandException(String command) {
        super("Unknown command " + command);
        this.command = command;
    }

    @Override
    public void message() {
        System.out.println("Unknown command " + command);
    }
}
