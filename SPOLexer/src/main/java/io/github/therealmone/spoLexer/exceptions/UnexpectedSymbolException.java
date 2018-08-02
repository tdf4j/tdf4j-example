package io.github.therealmone.spoLexer.exceptions;

import io.github.therealmone.translatorAPI.Exceptions.LexerException;
import io.github.therealmone.translatorAPI.Interfaces.ExceptionInterface;
import io.github.therealmone.translatorAPI.SavePrinter;
import io.github.therealmone.translatorAPI.Token;

import java.util.ArrayList;

public class UnexpectedSymbolException extends LexerException implements ExceptionInterface {
    private String input;
    private ArrayList<Token> tokens;

    public UnexpectedSymbolException(String input, ArrayList<Token> tokens) {
        super("Got unexpected symbol " + input.charAt(0));
        this.input = input;
        this.tokens = tokens;
    }

    @Override
    public void message() {
        int count = 0;
        StringBuilder message = new StringBuilder();

        for (Token token : tokens) {
            count += token.getValue().length() + 1;
            message.append(token.getValue()).append(" ");
        }

        SavePrinter.savePrintln("Unexpected symbol at '^' mark. \n" + message + input);
        for (int i = 0; i < count; i++) {
            SavePrinter.savePrint(" ");
        }
        SavePrinter.savePrintln("^");
    }
}
