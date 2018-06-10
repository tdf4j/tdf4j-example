package ru.therealmone.spoStackMachine.collections.hashset.exceptions;

import ru.therealmone.spoStackMachine.exceptions.HashMapException;
import ru.therealmone.translatorAPI.Interfaces.ExceptionInterface;

public class NoSuchElementException extends HashMapException implements ExceptionInterface {
    private String key;

    public NoSuchElementException(String key) {
        super("Can't find key " + key);
        this.key = key;
    }

    @Override
    public void message() {
        System.out.println("Can't find key " + key + ".");
    }
}
