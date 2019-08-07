package org.tdf4j.example.core.collections.hashset.exceptions;

import org.tdf4j.example.core.IException;
import org.tdf4j.example.core.utils.SavePrinter;

public class KeyAlreadyExistsException extends HashSetException implements IException {
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
