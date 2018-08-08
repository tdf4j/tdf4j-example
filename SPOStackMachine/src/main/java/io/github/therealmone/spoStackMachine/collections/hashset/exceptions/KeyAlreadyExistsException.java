package io.github.therealmone.spoStackMachine.collections.hashset.exceptions;

import io.github.therealmone.spoStackMachine.exceptions.HashMapException;
import io.github.therealmone.translatorAPI.Interfaces.ExceptionInterface;
import io.github.therealmone.translatorAPI.SavePrinter;

public class KeyAlreadyExistsException extends HashMapException implements ExceptionInterface {
    private final String key;

    public KeyAlreadyExistsException(final String key) {
        super("Key " + key + " already exists.");
        this.key = key;
    }

    @Override
    public void message() {
        SavePrinter.savePrintln("Key " + key + " already exists.");
    }
}
