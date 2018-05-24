package ru.therealmone.spoLexer;

import ru.therealmone.spoParser.Parser;
import ru.therealmone.translatorAPI.Token;
import ru.therealmone.spoStackMachine.StackMachine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;


public class Main {
    private static final String LEXEMES_DIR = "lexemes.xml";
    private static final String LANG_RULES_DIR = "langRules.csv";
    private static final String ANALYZE_TABLE_DIR = "analyzeTable.csv";
    private static final String CONTEXT_DIR = "context.csv";
    private static final String PROGRAM_DIR = "program.txt";

    public static void main(String[] args) {

        Lexer lexer = new Lexer(LEXEMES_DIR);
        lexer.showLexemes();

        lexer.generateTokens(loadProgram());
        lexer.showTokens();

        Parser parser = new Parser(LANG_RULES_DIR, ANALYZE_TABLE_DIR, new HashSet<>(lexer.lexemes.keySet()));
        parser.showLangRules();

        for(Token token: lexer.tokens) {
            token.accept(parser);
        }

        System.out.println("PARSE SUCCESS");
        System.out.println("OPN: " + parser.getOPN());

        StackMachine stackMachine = new StackMachine(CONTEXT_DIR);
        parser.accept(stackMachine);

        System.out.println("CALCULATE SUCCESS");
        stackMachine.showVariables();
    }

    private static String loadProgram() {
        StringBuilder out = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(PROGRAM_DIR));
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return out.toString();
    }
}
