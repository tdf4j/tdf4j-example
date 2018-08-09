package io.github.therealmone.spoParser.exceptions;

import io.github.therealmone.translatorAPI.Exceptions.ParserException;
import io.github.therealmone.translatorAPI.Interfaces.ExceptionInterface;
import io.github.therealmone.translatorAPI.SavePrinter;
import io.github.therealmone.translatorAPI.Beans.Token;

public class UnexpectedTokenException extends ParserException implements ExceptionInterface {
    private final String expected;
    private final Token token;
    private final String history;

    public UnexpectedTokenException(final Token token, final String expected, final String history) {
        super("Got unexpected token " + token.getValue());
        this.expected = expected;
        this.token = token;
        this.history = history;
    }

    public UnexpectedTokenException(final Token token, final String history) {
        super("Got unexpected token " + token.getValue());
        this.token = token;
        this.history = history;
        this.expected = null;
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
