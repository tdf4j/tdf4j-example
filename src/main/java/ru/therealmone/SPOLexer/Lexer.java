package ru.therealmone.SPOLexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.*;

public class Lexer {
    ArrayList<Token> tokens = new ArrayList<Token>();
    Map<String, Pattern> lexemes = new HashMap<String, Pattern>();

    void addLexeme(String type, String pattern) {
        if (!lexemes.containsKey(type))
            this.lexemes.put(type, Pattern.compile(pattern));
        else
            System.out.println("Lexeme " + type + " already exists!");
    }

    void showLexemes() {
        for(Map.Entry<String, Pattern> entry: lexemes.entrySet()) {
            System.out.println(entry.getKey() + " --> " + entry.getValue().pattern());
        }
    }

    void generateTokens(String input) {

        boolean expectSuccsess = true;
        boolean passed = false;
        tokens.clear();
        String tempString = "";

        while(!input.equals("$")) {
            input = input.trim();
            tempString += input.charAt(tempString.length());
            String passedLexeme = "";

            for(Map.Entry<String, Pattern> entry: lexemes.entrySet()) {
                if(this.compile(entry.getKey(), tempString)) {
                    passed = true;
                    passedLexeme = entry.getKey();
                }
            }

            if(expectSuccsess) {
                if(passed) {
                    expectSuccsess = false;
                    passed = false;
                } else {
                    System.out.println("ERROR: Unexpected first symbol!");
                    System.out.println("Current input: " + input);
                    break;
                }
            } else {
                if(passed) {
                    passed = false;
                } else {
                    expectSuccsess = true;
                    tempString = String.copyValueOf(tempString.toCharArray(), 0, tempString.length() - 1);
                    tokens.add(new Token(passedLexeme, tempString));
                    input = String.copyValueOf(input.toCharArray(), tempString.length(), input.length() - tempString.length());
                    tempString = "";
                }
            }
        }

    }

    void showTokens() {
        for(Token token: tokens) {
            System.out.println("Token №" + tokens.indexOf(token) + " : " + token.getType() + " --> " + token.getValue());
        }
    }

    boolean compile(String lexeme, String string) {
        Pattern p = lexemes.get(lexeme);
        Matcher m = p.matcher(string);
        return m.matches();
    }
}
