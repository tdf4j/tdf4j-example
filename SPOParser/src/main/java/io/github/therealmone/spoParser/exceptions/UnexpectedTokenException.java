package io.github.therealmone.spoParser.exceptions;

import io.github.therealmone.core.exceptions.ParserException;
import io.github.therealmone.core.interfaces.IException;
import io.github.therealmone.core.utils.SavePrinter;
import io.github.therealmone.core.beans.Token;

public class UnexpectedTokenException extends ParserException implements IException {
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
        SavePrinter.savePrintln("Unexpected token " + token.getType() + " at '^' mark. \r\n" + history + token.getValue());
        for (int i = 0; i < history.length(); i++) {
            SavePrinter.savePrint(" ");
        }
        SavePrinter.savePrintln("^");

        if (expected != null) {
            SavePrinter.savePrintln("Expected: " + expected);
        }
    }
}
