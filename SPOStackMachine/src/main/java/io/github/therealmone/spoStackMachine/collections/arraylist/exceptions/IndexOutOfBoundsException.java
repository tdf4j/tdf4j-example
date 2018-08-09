package io.github.therealmone.spoStackMachine.collections.arraylist.exceptions;

import io.github.therealmone.spoStackMachine.exceptions.ArrayListException;
import io.github.therealmone.translatorAPI.Interfaces.ExceptionInterface;
import io.github.therealmone.translatorAPI.Utils.SavePrinter;

public class IndexOutOfBoundsException extends ArrayListException implements ExceptionInterface {
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
