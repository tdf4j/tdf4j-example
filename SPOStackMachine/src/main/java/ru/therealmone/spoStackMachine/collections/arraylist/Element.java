package ru.therealmone.spoStackMachine.collections.arraylist;

import ru.therealmone.translatorAPI.Node;

public class Element extends Node {
    private int index;
    private double value;

    Element(String name, int index, double value) {
        super(name);
        this.index = index;
        this.value = value;
    }

    public int getIndex() {
        return this.index;
    }

    public double getValue() {
        return this.value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
