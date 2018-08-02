package io.github.therealmone.spoParser.exceptions;

import io.github.therealmone.translatorAPI.Exceptions.ParserException;
import io.github.therealmone.translatorAPI.Interfaces.ExceptionInterface;
import io.github.therealmone.translatorAPI.SavePrinter;
import io.github.therealmone.translatorAPI.Token;

public class UnexpectedTokenException extends ParserException implements ExceptionInterface {
    private String expected;
    private Token token;
    private String history;

    public UnexpectedTokenException(Token token, String expected, String history) {
        super("Got unexpected token " + token.getValue());
        this.expected = expected;
        this.token = token;
        this.history = history;
    }

    public UnexpectedTokenException(Token token, String history) {
        super("Got unexpected token " + token.getValue());
        this.token = token;
        this.history = history;
    }

    @Override
    public void message() {
        SavePrinter.savePrintln("Unexpected token " + token.getType() + " at '^' mark. \n" + history + token.getValue());
        for (int i = 0; i < history.length(); i++) {
            SavePrinter.savePrint(" ");
        }
        SavePrinter.savePrintln("^");

        if (expected != null) {
            SavePrinter.savePrintln("Expected: " + expected);
        }
    }
}
