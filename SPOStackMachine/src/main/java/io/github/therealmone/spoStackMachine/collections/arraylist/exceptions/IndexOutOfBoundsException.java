package io.github.therealmone.spoStackMachine.collections.arraylist.exceptions;

import io.github.therealmone.spoStackMachine.exceptions.ArrayListException;
import io.github.therealmone.translatorAPI.Interfaces.ExceptionInterface;
import io.github.therealmone.translatorAPI.SavePrinter;

public class IndexOutOfBoundsException extends ArrayListException implements ExceptionInterface {
    private int index;
    private int length;

    public IndexOutOfBoundsException(int index, int length) {
        super("Index out of bound: " + index + ". Length = " + length);
        this.index = index;
        this.length = length;
    }

    @Override
    public void message() {
        SavePrinter.savePrintln("Index out of bound: " + index + ". Length = " + length);
    }
}
