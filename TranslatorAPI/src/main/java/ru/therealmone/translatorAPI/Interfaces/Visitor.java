package ru.therealmone.translatorAPI.Interfaces;

import ru.therealmone.translatorAPI.Exceptions.ParserException;
import ru.therealmone.translatorAPI.Exceptions.StackMachineException;
import ru.therealmone.translatorAPI.Token;

public interface Visitor {
    void visit(Token token) throws ParserException;
    void visit(String opn) throws StackMachineException;
}
