package ru.therealmone.spoStackMachine.collections.arraylist.exceptions;

import ru.therealmone.spoStackMachine.exceptions.ArrayListException;
import ru.therealmone.translatorAPI.Interfaces.ExceptionInterface;

public class KeyAlreadyExistsException extends ArrayListException implements ExceptionInterface {
    private String key;

    public KeyAlreadyExistsException(String key) {
        super("Key " + key + " already exists.");
        this.key = key;
    }

    @Override
    public void message() {
        System.out.println("Key " + key + " already exists.");
    }
}
