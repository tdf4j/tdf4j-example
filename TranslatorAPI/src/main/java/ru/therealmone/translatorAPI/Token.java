package ru.therealmone.translatorAPI;

import ru.therealmone.translatorAPI.Interfaces.Visitable;
import ru.therealmone.translatorAPI.Interfaces.Visitor;

public class Token implements Visitable {
    private String type;
    private String value;

    public Token(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return(this.type);
    }

    public String getValue() {
        return(this.value);
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
