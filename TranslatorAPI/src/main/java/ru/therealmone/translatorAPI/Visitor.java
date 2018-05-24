package ru.therealmone.TranslatorAPI;

public interface Visitor {
    void visit(Token token);
    void visit(String opn);
}
