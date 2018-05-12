package ru.therealmone.SPOLexer;

import ru.therealmone.SPOParser.Parser;
import ru.therealmone.TranslatorAPI.Token;

import java.util.HashSet;


public class Main {
    public static void main(String[] args) {
        Lexer lexer = new Lexer(true);
        lexer.generateTokens("while((a + b < c) & y == 10) {if(a + b == c) {} else {};};$");
        lexer.showTokens();

        Parser parser = new Parser(new HashSet<>(lexer.lexemes.keySet()));

        for(Token token: lexer.tokens) {
            token.accept(parser);
            if(parser.ERROR) {
                System.out.println("ERROR: Unexpected token " + token.getType());
                System.exit(1);
            }
        }
        System.out.println("Successfully parsed!");
    }
}
