package ru.therealmone.spoStackMachine.hashmap.exceptions;

import ru.therealmone.spoStackMachine.exceptions.HashMapException;
import ru.therealmone.translatorAPI.Interfaces.ExceptionInterface;

public class UnknownCommandException extends HashMapException implements ExceptionInterface {
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
