package ru.therealmone.SPOLexer;

public class Main {
    public static void main(String[] args) {
        Lexer lexer = new Lexer();
        lexer.addLexeme("VAR", "^[a-z]+$", 4);
        lexer.addLexeme("DIGIT", "^0|([1-9][0-9]*)", 3);
        lexer.addLexeme("ASSIGN_OP", "^=$", 2);
        lexer.addLexeme("OP", "^[\\+\\-\\/\\*]$", 1);

        System.out.println("LEXEMES: ");
        lexer.showLexemes();

        lexer.generateTokens("value = a + b - c / 0 * 10045645 = value1 = value2 = - * + /$");
        System.out.println("TOKENS: ");
        lexer.showTokens();
    }
}
