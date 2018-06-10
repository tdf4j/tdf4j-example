package ru.therealmone.spoStackMachine.collections.arraylist;

import ru.therealmone.spoStackMachine.collections.Collection;
import ru.therealmone.spoStackMachine.collections.arraylist.exceptions.KeyAlreadyExistsException;

public class ArrayList implements Collection {
    private Element root;
    private int size;

    public ArrayList() {
        size = 0;
        root = null;
    }

    @Override
    public void add(String variable, double value) throws KeyAlreadyExistsException {
        if(contains(variable))
            throw new KeyAlreadyExistsException(variable);

        Element current = root;

        while(current.getNext() != null)
            current = (Element) current.getNext();

        current.setNext(new Element(variable, size, value));
        size++;
    }

    @Override
    public double get(String variable) {
        return 0;
    }

    @Override
    public void rewrite(String variable, double value) {

    }

    @Override
    public void remove(String variable) {

    }

    @Override
    public boolean contains(String variable) {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public String toString() {
        return null;
    }
}


