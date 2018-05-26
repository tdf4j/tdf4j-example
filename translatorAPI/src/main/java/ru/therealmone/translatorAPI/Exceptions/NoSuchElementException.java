package ru.therealmone.translatorAPI.Exceptions;

public class NoSuchElementException extends Exception implements ExceptionInterface {
    private String key;

    public NoSuchElementException(String key) {
        this.key = key;
    }

    @Override
    public void message() {
        System.out.println("Can't find key " + key + ".");
    }
}
