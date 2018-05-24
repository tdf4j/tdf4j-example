package ru.therealmone.spoStackMachine;

import ru.therealmone.translatorAPI.Node;

class Element extends Node {
    private double value;
    private int hashCode;

    Element(String name, double value) {
        super(name);
        this.value = value;
        this.hashCode = name.hashCode();
    }

    double getValue() {
        return this.value;
    }

    void setValue(double value) {this.value = value;}

    int getHashCode() {
        return this.hashCode;
    }
}
