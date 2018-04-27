package ru.therealmone.SPOLexer;

import ru.therealmone.SPOParser.Parser;

public class Main {
    public static void main(String[] args) {
        Lexer lexer = new Lexer(true);

        System.out.println("LEXEMES: ");
        lexer.showLexemes();

        lexer.generateTokens("value = a + b - c / 0 * 10045645 = value1 = value2 = - * + /$");
        System.out.println("TOKENS: ");
        lexer.showTokens();
        lexer.tokens.get(0).accept(new Parser());
    }
}
