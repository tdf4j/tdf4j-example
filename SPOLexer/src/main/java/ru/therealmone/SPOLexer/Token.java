package ru.therealmone.SPOLexer;

import ru.therealmone.SPOParser.Visitor;

public class Token implements Visitable {
    private String type;
    private String value;

    Token(String type, String value) {
        this.type = type;
        this.value = value;
    }

    String getType() {
        return(this.type);
    }

    String getValue() {
        return(this.value);
    }

    @Override
    public void accept(Visitor v){
        v.visit(this.type, this.value);
    }
}
