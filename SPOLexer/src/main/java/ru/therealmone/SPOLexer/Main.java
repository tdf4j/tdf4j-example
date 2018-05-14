package ru.therealmone.SPOLexer;

import ru.therealmone.SPOParser.Parser;
import ru.therealmone.TranslatorAPI.Token;
import ru.therealmone.SPOStackMachine.StackMachine;

import java.util.HashSet;


public class Main {
    public static void main(String[] args) {
        Lexer lexer = new Lexer(true);
        Parser parser = new Parser(new HashSet<>(lexer.lexemes.keySet()));
        StackMachine stackMachine = new StackMachine();


        lexer.generateTokens("while(a > b) {if (a < d) {do{i = i;}while(a < b)} else {for(i = i; i < 100; i = i + 1){i = i;}}}");

        for(Token token: lexer.tokens) {
            token.accept(parser);
            if(parser.ERROR) {
                System.out.println("ERROR: Unexpected token " + token.getType());
                System.exit(1);
            }
        }

        System.out.println("Successfully parsed!");

        System.out.println("OPN: " + parser.getOPN());

        parser.accept(stackMachine);
    }
}
