package ru.therealmone.spoStackMachine;

import ru.therealmone.translatorAPI.Node;

class Bucket extends Node {
    private int eleCount;

    Bucket() {
        super("bucket");
        this.eleCount = 0;
    }

    void incCount() {
        this.eleCount++;
    }

    void decCount() {
        this.eleCount--;
    }

    int getEleCount() {
        return this.eleCount;
    }
}
