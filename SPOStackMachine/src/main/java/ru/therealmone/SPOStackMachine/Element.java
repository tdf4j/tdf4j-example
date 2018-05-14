package ru.therealmone.SPOStackMachine;

import ru.therealmone.TranslatorAPI.Node;

class Element extends Node {
    private int value;
    private int hashCode;

    Element(String name, int value) {
        super(name);
        this.value = value;
        this.hashCode = name.hashCode();
    }

    int getValue() {
        return this.value;
    }

    int getHashCode() {
        return this.hashCode;
    }
}
