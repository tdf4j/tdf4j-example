package ru.therealmone.TranslatorAPI;

public class KeyAlreadyExistsException extends Exception{
    private String key;

    public KeyAlreadyExistsException(String key) {
        this.key = key;
    }

    public void message() {
        System.out.println("Key " + key + " already exists.");
    }
}
