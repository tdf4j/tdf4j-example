package ru.therealmone.translatorAPI.Exceptions;

import ru.therealmone.translatorAPI.Token;

import java.util.ArrayList;

public class UnexpectedSymbolException extends Exception implements ExceptionInterface{
    private String input;
    private ArrayList<Token> tokens;

    public UnexpectedSymbolException(String input, ArrayList<Token> tokens) {
        this.input = input;
        this.tokens = tokens;
    }

    @Override
    public void message() {
        int count = 0;
        StringBuilder message = new StringBuilder();

        for(Token token: tokens) {
            count += token.getValue().length() + 1;
            message.append(token.getValue()).append(" ");
        }

        System.out.println("Unexpected symbol at '^' mark. \n" + message + input);
        for (int i = 0; i < count; i++) {
            System.out.print(" ");
        }
        System.out.println("^");
    }
}
