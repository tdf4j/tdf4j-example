package ru.therealmone.TranslatorAPI;

public class NoSuchElementException extends Exception{
    private String key;

    public NoSuchElementException(String key) {
        this.key = key;
    }

    public void message() {
        System.out.println("Can't find key " + key + ".");
    }
}
