package ru.therealmone.SPOLexer;

import ru.therealmone.SPOParser.Parser;
import ru.therealmone.TranslatorAPI.Token;
import ru.therealmone.SPOStackMachine.StackMachine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;


public class Main {
    public static void main(String[] args) {
        Lexer lexer = new Lexer(true);
        Parser parser = new Parser(new HashSet<>(lexer.lexemes.keySet()));
        StackMachine stackMachine = new StackMachine();


        lexer.generateTokens(loadProgram());
        for(Token token: lexer.tokens) {
            token.accept(parser);
            if(parser.ERROR) {
                System.out.println("ERROR: Unexpected token " + token.getType());
                System.exit(1);
            }
        }

        System.out.println("PARSE SUCCESS");
        System.out.println("OPN: " + parser.getOPN());
        parser.accept(stackMachine);
        System.out.println("CALCULATE SUCCESS");
        stackMachine.showVariables();
    }

    private static String loadProgram() {
        StringBuilder out = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("D:/JavaProjects/SPOTranslator/SPOLexer/src/main/resources/program.txt"));
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
