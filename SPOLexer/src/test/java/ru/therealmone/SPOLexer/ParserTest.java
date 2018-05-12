package ru.therealmone.SPOLexer;

import org.junit.Test;
import static org.junit.Assert.*;
import ru.therealmone.SPOParser.Parser;
import ru.therealmone.TranslatorAPI.Token;

import java.util.ArrayList;
import java.util.HashSet;

public class ParserTest {
    @Test
    public void parserTests() {
        Lexer lexer = new Lexer(true);
        HashSet<String> terminals = new HashSet<>(lexer.lexemes.keySet());

        //while tests
        lexer.generateTokens("while() {};");
        assertFalse(check(lexer.tokens, new Parser(terminals)));

        lexer.generateTokens("while(a > b) {};");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        lexer.generateTokens("while((a > b) & (c < d)) {};");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        lexer.generateTokens("while((((((a > b))))) & (((((c < d)))))) {};");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        lexer.generateTokens("while((a == b) & (c < d * (5 + 1)) | ((10 *(s + 150) <= 300))){};");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        lexer.generateTokens("while(((((((((((c < d))))))))))) {};");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        lexer.generateTokens("while(a < b & c > d) {};");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        lexer.generateTokens("while(a == b & c < d * (5 + 1) | 10 * (s + 150) <= 300){};");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        //if tests
        lexer.generateTokens("if() {};");
        assertFalse(check(lexer.tokens, new Parser(terminals)));

        lexer.generateTokens("if(a > b) {};");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        lexer.generateTokens("if((a > b) & (c < d)) {};");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        lexer.generateTokens("if((((((a > b))))) & (((((c < d)))))) {};");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        lexer.generateTokens("if((a == b) & (c < d * (5 + 1)) | ((10 *(s + 150) <= 300))){};");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        lexer.generateTokens("if(((((((((((c < d))))))))))) {};");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        lexer.generateTokens("if(a < b & c > d) {};");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        lexer.generateTokens("if(a == b & c < d * (5 + 1) | 10 * (s + 150) <= 300){};");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        //for tests
        lexer.generateTokens("for(i = 0; ; i = i + 1){};");
        assertFalse(check(lexer.tokens, new Parser(terminals)));

        lexer.generateTokens("for(i = 0; i < 100; i = i + 1) {};");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        lexer.generateTokens("for(i = (((((a + b))))) * (((((c + d))))); i > 100; i = i + 1){};");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        lexer.generateTokens("for(i = (((1 + 1) * (1 + 2)) * (1 + 1)) * (100 - 10); i > 100; i = i + 1){};");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        //do-while tests
        lexer.generateTokens("do{}while();");
        assertFalse(check(lexer.tokens, new Parser(terminals)));

        lexer.generateTokens("do{}while(a > b);");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        lexer.generateTokens("do{}while((a > b) & (c < d));");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        lexer.generateTokens("do{}while((((((a > b))))) & (((((c < d))))));");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        lexer.generateTokens("do{}while((a == b) & (c < d * (5 + 1)) | ((10 *(s + 150) <= 300)));");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        lexer.generateTokens("do{}while(((((((((((c < d)))))))))));");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        lexer.generateTokens("do{}while(a < b & c > d);");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        lexer.generateTokens("do{}while(a == b & c < d * (5 + 1) | 10 * (s + 150) <= 300);");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        //Combinations
        lexer.generateTokens("while(a > b) {if (a < d) {do{i = i;}while(a < b);} else {for(i = i; i < 100; i = i + 1){i = i;};};};");
        assertTrue(check(lexer.tokens, new Parser(terminals)));
    }

    private boolean check(ArrayList<Token> tokens, Parser parser) {
        for(Token token: tokens) {
            token.accept(parser);
            if (parser.ERROR) {
                System.out.println("ERROR: Unexpected token " + token.getType());
                return false;
            }
            System.out.println("Success: " + token.getType() + " " + token.getValue());
        }
        return true;
    }
}
