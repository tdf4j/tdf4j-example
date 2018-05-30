package ru.therealmone.spoLexer;

import ru.therealmone.spoParser.Parser;
import ru.therealmone.spoStackMachine.StackMachine;
import ru.therealmone.translatorAPI.Exceptions.TranslatorException;
import ru.therealmone.translatorAPI.Token;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;


public class Main {
    private static final String LEXEMES_DIR = "spoLexer/src/main/resources/lexemes.xml";
    private static final String LANG_RULES_DIR = "spoParser/src/main/resources/langRules.csv";
    private static final String ANALYZE_TABLE_DIR = "spoParser/src/main/resources/analyzeTable.csv";
    private static final String COMMANDS_DIR = "spoStackMachine/src/main/resources/commands.xml";
    private static final String PROGRAM_DIR = "spoLexer/src/main/resources/program.txt";

    public static void main(String[] args) {

        Lexer lexer;
        Parser parser;
        StackMachine stackMachine;

        try {
            lexer = new Lexer(LEXEMES_DIR);
            lexer.showLexemes();

            lexer.generateTokens(loadProgram());
            lexer.showTokens();


            parser = new Parser(LANG_RULES_DIR, ANALYZE_TABLE_DIR, new HashSet<>(lexer.lexemes.keySet()));
            parser.showLangRules();

            for(Token token: lexer.tokens) {
                token.accept(parser);
            }

            System.out.println("PARSE SUCCESS");
            System.out.println("OPN: " + parser.getOPN());

            stackMachine = new StackMachine(COMMANDS_DIR);
            parser.accept(stackMachine);

            System.out.println("CALCULATE SUCCESS");
            stackMachine.showVariables();

        } catch (TranslatorException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static String loadProgram() throws TranslatorException {
        StringBuilder out = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(PROGRAM_DIR));
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
        } catch (IOException e) {
            throw new TranslatorException("Can't find program.txt", e);
        }

        return out.toString();
    }
}
