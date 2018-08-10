package io.github.therealmone.core.collections.hashset.exceptions;

import io.github.therealmone.core.interfaces.ExceptionInterface;
import io.github.therealmone.core.utils.SavePrinter;

public class KeyAlreadyExistsException extends HashSetException implements ExceptionInterface {
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
