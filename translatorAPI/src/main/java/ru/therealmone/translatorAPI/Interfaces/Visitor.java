package ru.therealmone.translatorAPI.Interfaces;

import ru.therealmone.translatorAPI.Token;

public interface Visitor {
    void visit(Token token);
    void visit(String opn);
}
