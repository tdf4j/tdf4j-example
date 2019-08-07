package org.tdf4j.example.core.collections.arraylist.exceptions;

import org.tdf4j.example.core.IException;
import org.tdf4j.example.core.utils.SavePrinter;

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
