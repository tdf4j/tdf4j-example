package ru.therealmone.translatorAPI;

public class NoSuchElementException extends Exception{
    private String key;

    public NoSuchElementException(String key) {
        this.key = key;
    }

    public void message() {
        System.out.println("Can't find key " + key + ".");
    }
}
