package ru.therealmone.applicationAPI;

import ru.therealmone.spoLexer.*;
import ru.therealmone.spoParser.Parser;
import ru.therealmone.spoStackMachine.StackMachine;
import ru.therealmone.translatorAPI.ResourceLoader;
import ru.therealmone.translatorAPI.Token;

import java.util.HashSet;

class TranslatorImpl implements Translator {
    @Override
    public void translate(String program) {
        ResourceLoader.initialize();

        Lexer lexer;
        Parser parser;
        StackMachine stackMachine;

        lexer = new Lexer();
        lexer.showLexemes();

        lexer.generateTokens(program);
        lexer.showTokens();

        parser = new Parser(new HashSet<>(lexer.lexemes.keySet()));
        parser.showLangRules();

        for(Token token: lexer.tokens) {
            token.accept(parser);
        }

        System.out.println("\u001B[32mPARSE SUCCESS\u001B[0m");
        System.out.println("OPN: " + parser.getOPN());
        System.out.println("\u001B[31mMAIN PROGRAM\u001B[0m");

        System.out.println("-----------------------------------------------------------------------------------------");

        stackMachine = new StackMachine();
        parser.accept(stackMachine);

        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println("\u001B[31mMAIN PROGRAM DONE\u001B[0m");

        System.out.println("\u001B[32mCALCULATE SUCCESS\u001B[0m");
        stackMachine.showVariables();
    }
}
