package org.tdf4j.example.core.collections.hashset.exceptions;

import org.tdf4j.example.core.IException;
import org.tdf4j.example.core.utils.SavePrinter;

public class NoSuchElementException extends HashSetException implements IException {
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
