package ru.therealmone.spoStackMachine.collections.arraylist.exceptions;

import ru.therealmone.spoStackMachine.exceptions.ArrayListException;
import ru.therealmone.translatorAPI.Interfaces.ExceptionInterface;
import ru.therealmone.translatorAPI.SavePrinter;

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
