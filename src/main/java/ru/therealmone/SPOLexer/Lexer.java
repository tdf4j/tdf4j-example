package ru.therealmone.SPOLexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.*;

class Lexer {
    ArrayList<Token> tokens = new ArrayList<Token>();
    Map<String, Pattern> lexemes = new HashMap<String, Pattern>();
    private Map<String, Integer> priority = new HashMap<String, Integer>();

    void addLexeme(String type, String pattern, Integer priority) {
        if (!lexemes.containsKey(type)) {
            this.lexemes.put(type, Pattern.compile(pattern));
            this.priority.put(type, priority);
        } else
            System.out.println("Lexeme " + type + " already exists!");
    }

    void showLexemes() {
        for(Map.Entry<String, Pattern> entry: lexemes.entrySet()) {
            System.out.println(entry.getKey() + " --> " + entry.getValue().pattern());
        }
    }

    void generateTokens(String input) {

        boolean expectSuccess = true;
        boolean passed = false;
        tokens.clear();
        String tempString = "";
        List<String> passedLexemes = new ArrayList<String>();

        while(!input.equals("$")) {
            input = input.trim();
            tempString += input.charAt(tempString.length());

            for(Map.Entry<String, Pattern> entry: lexemes.entrySet()) {
                if(this.compile(entry.getKey(), tempString)) {
                    passed = true;
                    passedLexemes.add(entry.getKey());
                }
            }

            if(expectSuccess) {
                if(passed) {
                    expectSuccess = false;
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
                    expectSuccess = true;
                    tempString = String.copyValueOf(tempString.toCharArray(), 0, tempString.length() - 1);
                    if(passedLexemes.size() == 1) {
                        tokens.add(new Token(passedLexemes.get(0), tempString));
                    } else if(passedLexemes.size() != 0){
                        tokens.add(new Token(checkPassedLexemes(passedLexemes), tempString));
                    } else {
                        System.out.println("ERROR: No passed lexeme found!");
                    }
                    input = String.copyValueOf(input.toCharArray(), tempString.length(), input.length() - tempString.length());
                    tempString = "";
                    passedLexemes.clear();
                }
            }
        }

    }

    private String checkPassedLexemes(List<String> list) {
        String tmp = list.get(0);
        for(String lexeme: list) {
            if(priority.get(lexeme) > priority.get(tmp))
                tmp = lexeme;
        }
        return tmp;
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
