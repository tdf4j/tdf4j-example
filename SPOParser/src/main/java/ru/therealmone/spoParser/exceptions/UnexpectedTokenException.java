package ru.therealmone.spoParser.exceptions;

import ru.therealmone.translatorAPI.Exceptions.ParserException;
import ru.therealmone.translatorAPI.Interfaces.ExceptionInterface;
import ru.therealmone.translatorAPI.Token;

public class UnexpectedTokenException extends ParserException implements ExceptionInterface {
    private String expected;
    private Token token;
    private String history;

    public UnexpectedTokenException(Token token, String expected, String history) {
        super("Got unexpected token " + token.getValue() + " -> " + token.getValue());
        this.expected = expected;
        this.token = token;
        this.history = history;
    }

    @Override
    public void message() {
        System.out.println("Unexpected token " + token.getType() + " at '^' mark. \n" + history + token.getValue());
        for (int i = 0; i < history.length(); i++) {
            System.out.print(" ");
        }
        System.out.println("^");
        System.out.println("Expected: " + expected);
    }
}
