package ru.therealmone.translatorAPI.Exceptions;

public class KeyAlreadyExistsException extends Exception implements ExceptionInterface {
    private String key;

    public KeyAlreadyExistsException(String key) {
        this.key = key;
    }

    @Override
    public void message() {
        System.out.println("Key " + key + " already exists.");
    }
}
