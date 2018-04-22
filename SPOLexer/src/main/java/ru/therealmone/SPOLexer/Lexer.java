package ru.therealmone.SPOLexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.*;

class Lexer {
    ArrayList<Token> tokens = new ArrayList<>();
    Map<String, Pattern> lexemes = new HashMap<>();
    private Map<String, Integer> priority = new HashMap<>();

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
        if(input.charAt(input.length() - 1) == '$') {

            boolean expectSuccess = true;
            boolean passed = false;
            tokens.clear();
            StringBuilder tempString = new StringBuilder();
            List<String> passedLexemes = new ArrayList<>();

            while (!input.equals("$")) {
                input = input.trim();
                tempString.append(input.charAt(tempString.length()));

                for (Map.Entry<String, Pattern> entry : lexemes.entrySet()) {
                    if (this.compile(entry.getKey(), tempString.toString())) {
                        passed = true;
                        break;
                    }
                }

                if (expectSuccess) {
                    if (passed) {
                        expectSuccess = false;
                        passed = false;
                    } else {
                        System.out.println("ERROR: Unexpected first symbol!");
                        System.out.println("Current input: " + input);
                        break;
                    }

                } else {
                    if (passed) {
                        passed = false;
                    } else {
                        expectSuccess = true;
                        tempString.deleteCharAt(tempString.length() - 1);

                        for(Map.Entry<String, Pattern> entry: lexemes.entrySet()) {
                            if(this.compile(entry.getKey(), tempString.toString())) {
                                passedLexemes.add(entry.getKey());
                            }
                        }

                        tokens.add(new Token(checkPassedLexemes(passedLexemes), tempString.toString()));

                        input = String.copyValueOf(input.toCharArray(), tempString.length(), input.length() - tempString.length());
                        tempString.delete(0, tempString.length());
                        passedLexemes.clear();
                    }
                }
            }

        } else {
            System.out.println("ERROR: String doesn't contain '&' at the end!");
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
