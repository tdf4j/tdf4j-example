package io.github.therealmone.core.collections.hashset.exceptions;

import io.github.therealmone.core.interfaces.IException;
import io.github.therealmone.core.utils.SavePrinter;

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
