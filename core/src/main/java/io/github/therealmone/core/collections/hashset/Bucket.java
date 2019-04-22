package io.github.therealmone.core.collections.hashset;

import io.github.therealmone.core.beans.Node;

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
