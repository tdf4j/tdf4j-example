package ru.therealmone.SPOLexer;

import ru.therealmone.TranslatorAPI.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.*;

class Lexer {
    ArrayList<Token> tokens = new ArrayList<>();
    Map<String, Pattern> lexemes;
    private Map<String, Integer> priority;

    private static final char END_SYMBOL = '$';

    Lexer(HashMap<String, Pattern> lexemes, HashMap<String, Integer> priority) {
        this.lexemes = lexemes;
        this.priority = priority;
    }

    Lexer() {
        lexemes = new HashMap<>();
        priority = new HashMap<>();
    }

    void addLexeme(String type, String pattern, Integer priority) {
        if (!lexemes.containsKey(type)) {
            this.lexemes.put(type, Pattern.compile(pattern));
            this.priority.put(type, priority);
        } else
            System.out.println("Lexeme " + type + " already exists!");
    }

    public void showLexemes() {
        for(Map.Entry<String, Pattern> entry: lexemes.entrySet()) {
            System.out.println("Lexeme #" + priority.get(entry.getKey()) + ": " + entry.getKey() + " --> " + entry.getValue().pattern());
        }
    }

    void generateTokens(String input) {
            input = input.replaceAll("\\$", "");
            input += END_SYMBOL;

            tokens.clear();
            StringBuilder tempString = new StringBuilder();

            while(input.charAt(0) != END_SYMBOL) {
                input = input.trim();
                tempString.append(input.charAt(tempString.length()));
                
                if(!checkLexemes(tempString.toString())) {
                    if(tempString.length() > 1) {
                        tempString.deleteCharAt(tempString.length() - 1);
                        tokens.add(new Token(chooseLexeme(tempString.toString()), tempString.toString()));
                        input = input.substring(tempString.length());
                        tempString.delete(0, tempString.length());
                    } else {
                        System.out.println("Unexpected first symbol in current input string: \n\t" + input);
                        break;
                    }
                }
            }

            tokens.add(new Token("$", "$"));
    }
    
    private boolean checkLexemes(String str) {
       for(Map.Entry<String, Pattern> entry: lexemes.entrySet()) {
           if(match(entry.getKey(), str))
               return true;
       }
       return false;
    }

    private String chooseLexeme(String str) {
        int tmpPriv = 0;
        String lexType = "";

        for(Map.Entry<String, Pattern> entry: lexemes.entrySet()) {
            if(match(entry.getKey(), str)) {
                if(priority.get(entry.getKey()) > tmpPriv) {
                    lexType = entry.getKey();
                    tmpPriv = priority.get(entry.getKey());
                }
            }
        }
        return lexType;
    }

    void showTokens() {
        System.out.println("GENERATED TOKENS: ");
        for(Token token: tokens) {
            System.out.println("Token №" + tokens.indexOf(token) + " : " + token.getType() + " --> " + token.getValue());
        }
    }

    boolean match(String lexeme, String string) {
        Pattern p = lexemes.get(lexeme);
        Matcher m = p.matcher(string);
        return m.matches();
    }
}
