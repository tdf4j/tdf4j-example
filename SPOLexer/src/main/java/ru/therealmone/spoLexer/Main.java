package ru.therealmone.SPOLexer;

import ru.therealmone.SPOParser.Parser;
import ru.therealmone.TranslatorAPI.Token;
import ru.therealmone.SPOStackMachine.StackMachine;
import ru.therealmone.TranslatorAPI.UnexpectedSymbolException;
import ru.therealmone.TranslatorAPI.UnexpectedTokenException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;


public class Main {
    private static final String LEXEMES_DIR = "SPOLexer/src/main/resources/lexemes.xml";
    private static final String LANG_RULES_DIR = "SPOParser/src/main/resources/langRules.csv";
    private static final String ANALYZE_TABLE_DIR = "SPOParser/src/main/resources/analyzeTable.csv";
    private static final String CONTEXT_DIR = "SPOStackMachine/src/main/resources/context.csv";
    private static final String PROGRAM_DIR = "SPOLexer/src/main/resources/program.txt";

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
