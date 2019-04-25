package io.github.therealmone.core.collections.arraylist.exceptions;

import io.github.therealmone.core.IException;
import io.github.therealmone.core.utils.SavePrinter;

public class IndexOutOfBoundsException extends ArrayListException implements IException {
    private final int index;
    private final int length;

    public IndexOutOfBoundsException(final int index, final int length) {
        super("Index out of bound: " + index + ". Length = " + length);
        this.index = index;
        this.length = length;
    }

    @Override
    public void message() {
        SavePrinter.savePrintln("Index out of bound: " + index + ". Length = " + length);
    }
}
