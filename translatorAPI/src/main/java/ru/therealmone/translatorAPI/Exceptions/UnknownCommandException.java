package ru.therealmone.translatorAPI.Exceptions;

public class UnknownCommandException extends Exception implements ExceptionInterface {
    private String command;

    public UnknownCommandException(String command) {
        this.command = command;
    }

    @Override
    public void message() {
        System.out.println("Unknown command: " + command);
    }
}
