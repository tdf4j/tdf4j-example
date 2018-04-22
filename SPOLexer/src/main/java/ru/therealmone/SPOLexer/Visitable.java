package ru.therealmone.SPOLexer;

import ru.therealmone.SPOParser.Visitor;

public interface Visitable {
    void accept(Visitor v);
}
