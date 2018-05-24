package ru.therealmone.translatorAPI;

public interface Visitor {
    void visit(Token token);
    void visit(String opn);
}
