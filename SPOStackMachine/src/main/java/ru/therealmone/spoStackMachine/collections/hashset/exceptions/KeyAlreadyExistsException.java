package ru.therealmone.spoStackMachine.collections.hashset.exceptions;

import ru.therealmone.spoStackMachine.exceptions.HashMapException;
import ru.therealmone.translatorAPI.Interfaces.ExceptionInterface;
import ru.therealmone.translatorAPI.SavePrinter;

public class KeyAlreadyExistsException extends HashMapException implements ExceptionInterface {
    private String key;

    public KeyAlreadyExistsException(String key) {
        super("Key " + key + " already exists.");
        this.key = key;
    }

    @Override
    public void message() {
        SavePrinter.savePrintln("Key " + key + " already exists.");
    }
}
