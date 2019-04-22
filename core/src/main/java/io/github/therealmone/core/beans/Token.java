package io.github.therealmone.core.beans;

import io.github.therealmone.core.interfaces.Visitable;
import io.github.therealmone.core.interfaces.Visitor;

public class Token implements Visitable {
    private final String type;
    private final String value;

    public Token(final String type, final String value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return (this.type);
    }

    public String getValue() {
        return (this.value);
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    @Override
    public String toString() {
        return this.type + " --> " + this.value;
    }
}
