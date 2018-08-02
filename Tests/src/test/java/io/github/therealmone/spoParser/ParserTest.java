package ru.therealmone.spoParser;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

import ru.therealmone.spoLexer.Lexer;
import ru.therealmone.translatorAPI.ResourceLoader;
import ru.therealmone.translatorAPI.Token;

import java.util.ArrayList;
import java.util.HashSet;

public class ParserTest {

    @BeforeClass
    public static void before() {
        ResourceLoader.initialize();
    }

    @Test
    public void parserTests() {
        Lexer lexer = new Lexer();
        HashSet<String> terminals = new HashSet<>(lexer.lexemes.keySet());

        //while tests
        lexer.generateTokens("while(a > b) {}");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        lexer.generateTokens("while((a > b) & (c < d)) {}");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        lexer.generateTokens("while((((((a > b))))) & (((((c < d)))))) {}");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        lexer.generateTokens("while((a == b) & (c < d * (5 + 1)) | ((10 *(s + 150) <= 300))){}");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        lexer.generateTokens("while(((((((((((c < d))))))))))) {}");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        lexer.generateTokens("while(a < b & c > d) {}");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        lexer.generateTokens("while(a == b & c < d * (5 + 1) | 10 * (s + 150) <= 300){}");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        //if tests
        lexer.generateTokens("if(a > b) {}");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        lexer.generateTokens("if((a > b) & (c < d)) {}");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        lexer.generateTokens("if((((((a > b))))) & (((((c < d)))))) {}");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        lexer.generateTokens("if((a == b) & (c < d * (5 + 1)) | ((10 *(s + 150) <= 300))){}");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        lexer.generateTokens("if(((((((((((c < d))))))))))) {}");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        lexer.generateTokens("if(a < b & c > d) {}");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        lexer.generateTokens("if(a == b & c < d * (5 + 1) | 10 * (s + 150) <= 300){}");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        //for tests
        lexer.generateTokens("for(i = 0; i < 100; i = i + 1) {}");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        lexer.generateTokens("for(i = (((((a + b))))) * (((((c + d))))); i > 100; i = i + 1){}");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        lexer.generateTokens("for(i = (((1 + 1) * (1 + 2)) * (1 + 1)) * (100 - 10); i > 100; i = i + 1){}");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        //do-while tests
        lexer.generateTokens("do{}while(a > b)");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        lexer.generateTokens("do{}while((a > b) & (c < d))");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        lexer.generateTokens("do{}while((((((a > b))))) & (((((c < d))))))");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        lexer.generateTokens("do{}while((a == b) & (c < d * (5 + 1)) | ((10 *(s + 150) <= 300)))");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        lexer.generateTokens("do{}while(((((((((((c < d)))))))))))");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        lexer.generateTokens("do{}while(a < b & c > d)");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        lexer.generateTokens("do{}while(a == b & c < d * (5 + 1) | 10 * (s + 150) <= 300)");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        //print tests
        lexer.generateTokens("print(0);");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        lexer.generateTokens("new a = 100; print(a);");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        lexer.generateTokens("new a typeof arraylist; put(a, 100); print(get(a, 0));");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        lexer.generateTokens("new a typeof hashset; new i = 0; put(a, i); print(get(a, i));");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        //get tests
        lexer.generateTokens("new a typeof hashset; new i = 100; put(a, i); i = get(a, i);");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        lexer.generateTokens("new a typeof arraylist; put(a, 100); i = get(a, 0);");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        //remove tests
        lexer.generateTokens("new a typeof hashset; new i = 100; put(a, i); remove(a, i);");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        lexer.generateTokens("new a typeof arraylist; put(a, 100); remove(a, 0);");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        //Combinations
        //TODO: Написать больше комбинаций
        lexer.generateTokens("while(a > b) {if (a < d) {do{i = i;}while(a < b)} else {for(i = i; i < 100; i = i + 1){i = i;}}}");
        assertTrue(check(lexer.tokens, new Parser(terminals)));

        lexer.generateTokens("a = b; c = a; a = a;");
        assertTrue(check(lexer.tokens, new Parser(terminals)));
    }

    @Test
    public void converterTests() {
        Lexer lexer = new Lexer();
        HashSet<String> terminals = new HashSet<>(lexer.lexemes.keySet());

        lexer.generateTokens(""); //main program
        assertEquals("$", //opn
                getOPN(lexer, new Parser(terminals)));

        //TODO: Написать больше тестов
        //while tests
        lexer.generateTokens("while(a < b) {a = a + 1;}");
        assertEquals("%a,%b,<,!F@10,#a,%a,1,+,=,!@0,$",
                getOPN(lexer, new Parser(terminals)));

        lexer.generateTokens("while(a < b & c > d) {}");
        assertEquals("%a,%b,<,%c,%d,>,&,!F@9,!@0,$",
                getOPN(lexer, new Parser(terminals)));

        lexer.generateTokens("while(((a < b) & (c > d)) | ((a > c) & (b < d))) {}");
        assertEquals("%a,%b,<,%c,%d,>,&,%a,%c,>,%b,%d,<,&,|,!F@17,!@0,$",
                getOPN(lexer, new Parser(terminals)));

        lexer.generateTokens("while((a < b) & (c > d) | (a > c) & (b < d)) {}");
        assertEquals("%a,%b,<,%c,%d,>,&,%a,%c,>,%b,%d,<,&,|,!F@17,!@0,$",
                getOPN(lexer, new Parser(terminals)));

        lexer.generateTokens("while(a < b) {if (a < b) {}}");
        assertEquals("%a,%b,<,!F@10,%a,%b,<,!F@9,!@9,!@0,$",
                getOPN(lexer, new Parser(terminals)));

        lexer.generateTokens("while(a < b) {do{} while(a < b)}");
        assertEquals("%a,%b,<,!F@9,%a,%b,<,!T@4,!@0,$",
                getOPN(lexer, new Parser(terminals)));


        lexer.generateTokens("while(a < b) {for(i = 1; i < 100; i = i + 1) {}}");
        assertEquals("%a,%b,<,!F@18,#i,1,=,%i,100,<,!F@17,#i,%i,1,+,=,!@7,!@0,$",
                getOPN(lexer, new Parser(terminals)));

        lexer.generateTokens("while(a < b) {new a typeof hashset; new i = 1; put(a, i);}");
        assertEquals("%a,%b,<,!F@16,#a,new,#hashset,typeof,#i,new,1,=,#a,#i,put,!@0,$",
                getOPN(lexer, new Parser(terminals)));


        //for tests
        lexer.generateTokens("for(i = 1; (i < n) & (n > i); i = i + 1) {}");
        assertEquals("#i,1,=,%i,%n,<,%n,%i,>,&,!F@17,#i,%i,1,+,=,!@3,$",
                getOPN(lexer, new Parser(terminals)));

    }

    @Test
    public void testOptimizer() {
        Lexer lexer = new Lexer();
        HashSet<String> terminals = new HashSet<>(lexer.lexemes.keySet());

        lexer.generateTokens("print(100 / (25 + 25));");
        assertEquals("2.0,print,$",
                getOPN(lexer, new Parser(terminals)));

        lexer.generateTokens("print(1 / (100 * (50 - (1 / 0.16))));");
        assertEquals("2.2857142857142857E-4,print,$",
                getOPN(lexer, new Parser(terminals)));

        lexer.generateTokens("print(100 / (25 + 25 - a));");
        assertEquals("100,50.0,%a,-,/,print,$",
                getOPN(lexer, new Parser(terminals)));

        lexer.generateTokens("print(1 / (100 * (50 - (1 / 0.16 - a))));");
        assertEquals("1,100,50,6.25,%a,-,-,*,/,print,$",
                getOPN(lexer, new Parser(terminals)));
    }

    private boolean check(ArrayList<Token> tokens, Parser parser) {
        for (Token token : tokens) {
            token.accept(parser);
        }

        return true;
    }

    private String getOPN(Lexer lexer, Parser parser) {
        for (Token token : lexer.tokens) {
            token.accept(parser);
        }

        return parser.getOPN();
    }
}
