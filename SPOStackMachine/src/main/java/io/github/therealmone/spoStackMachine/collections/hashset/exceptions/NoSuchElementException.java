package io.github.therealmone.spoStackMachine.collections.hashset.exceptions;

import io.github.therealmone.spoStackMachine.exceptions.HashMapException;
import io.github.therealmone.translatorAPI.Interfaces.ExceptionInterface;
import io.github.therealmone.translatorAPI.Utils.SavePrinter;

public class NoSuchElementException extends HashMapException implements ExceptionInterface {
    private final String key;

    public NoSuchElementException(final String key) {
        super("Can't find key " + key);
        this.key = key;
    }

    @Override
    public void message() {
        SavePrinter.savePrintln("Can't find key " + key + ".");
    }
}
