package io.github.therealmone.spoParser;

import io.github.therealmone.spoLexer.Lexer;
import io.github.therealmone.translatorAPI.Beans.Lexeme;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

import io.github.therealmone.translatorAPI.Utils.ResourceLoader;
import io.github.therealmone.translatorAPI.Beans.Token;

import java.util.List;
import java.util.Set;

public class ParserTest {

    @BeforeClass
    public static void before() {
        ResourceLoader.initialize();
    }

    @Test
    public void parserTests() {
        Lexer lexer = Lexer.getInstance();
        Set<Lexeme> terminals = lexer.getTerminals();

        //while tests
        lexer.generateTokens("while(a > b) {}");
        assertTrue(check(lexer.getTokens(), Parser.getInstance(terminals)));

        lexer.generateTokens("while((a > b) & (c < d)) {}");
        assertTrue(check(lexer.getTokens(), Parser.getInstance(terminals)));

        lexer.generateTokens("while((((((a > b))))) & (((((c < d)))))) {}");
        assertTrue(check(lexer.getTokens(), Parser.getInstance(terminals)));

        lexer.generateTokens("while((a == b) & (c < d * (5 + 1)) | ((10 *(s + 150) <= 300))){}");
        assertTrue(check(lexer.getTokens(), Parser.getInstance(terminals)));

        lexer.generateTokens("while(((((((((((c < d))))))))))) {}");
        assertTrue(check(lexer.getTokens(), Parser.getInstance(terminals)));

        lexer.generateTokens("while(a < b & c > d) {}");
        assertTrue(check(lexer.getTokens(), Parser.getInstance(terminals)));

        lexer.generateTokens("while(a == b & c < d * (5 + 1) | 10 * (s + 150) <= 300){}");
        assertTrue(check(lexer.getTokens(), Parser.getInstance(terminals)));

        //if tests
        lexer.generateTokens("if(a > b) {}");
        assertTrue(check(lexer.getTokens(), Parser.getInstance(terminals)));

        lexer.generateTokens("if((a > b) & (c < d)) {}");
        assertTrue(check(lexer.getTokens(), Parser.getInstance(terminals)));

        lexer.generateTokens("if((((((a > b))))) & (((((c < d)))))) {}");
        assertTrue(check(lexer.getTokens(), Parser.getInstance(terminals)));

        lexer.generateTokens("if((a == b) & (c < d * (5 + 1)) | ((10 *(s + 150) <= 300))){}");
        assertTrue(check(lexer.getTokens(), Parser.getInstance(terminals)));

        lexer.generateTokens("if(((((((((((c < d))))))))))) {}");
        assertTrue(check(lexer.getTokens(), Parser.getInstance(terminals)));

        lexer.generateTokens("if(a < b & c > d) {}");
        assertTrue(check(lexer.getTokens(), Parser.getInstance(terminals)));

        lexer.generateTokens("if(a == b & c < d * (5 + 1) | 10 * (s + 150) <= 300){}");
        assertTrue(check(lexer.getTokens(), Parser.getInstance(terminals)));

        //for tests
        lexer.generateTokens("for(i = 0; i < 100; i = i + 1) {}");
        assertTrue(check(lexer.getTokens(), Parser.getInstance(terminals)));

        lexer.generateTokens("for(i = (((((a + b))))) * (((((c + d))))); i > 100; i = i + 1){}");
        assertTrue(check(lexer.getTokens(), Parser.getInstance(terminals)));

        lexer.generateTokens("for(i = (((1 + 1) * (1 + 2)) * (1 + 1)) * (100 - 10); i > 100; i = i + 1){}");
        assertTrue(check(lexer.getTokens(), Parser.getInstance(terminals)));

        //do-while tests
        lexer.generateTokens("do{}while(a > b)");
        assertTrue(check(lexer.getTokens(), Parser.getInstance(terminals)));

        lexer.generateTokens("do{}while((a > b) & (c < d))");
        assertTrue(check(lexer.getTokens(), Parser.getInstance(terminals)));

        lexer.generateTokens("do{}while((((((a > b))))) & (((((c < d))))))");
        assertTrue(check(lexer.getTokens(), Parser.getInstance(terminals)));

        lexer.generateTokens("do{}while((a == b) & (c < d * (5 + 1)) | ((10 *(s + 150) <= 300)))");
        assertTrue(check(lexer.getTokens(), Parser.getInstance(terminals)));

        lexer.generateTokens("do{}while(((((((((((c < d)))))))))))");
        assertTrue(check(lexer.getTokens(), Parser.getInstance(terminals)));

        lexer.generateTokens("do{}while(a < b & c > d)");
        assertTrue(check(lexer.getTokens(), Parser.getInstance(terminals)));

        lexer.generateTokens("do{}while(a == b & c < d * (5 + 1) | 10 * (s + 150) <= 300)");
        assertTrue(check(lexer.getTokens(), Parser.getInstance(terminals)));

        //print tests
        lexer.generateTokens("print(0);");
        assertTrue(check(lexer.getTokens(), Parser.getInstance(terminals)));

        lexer.generateTokens("new a = 100; print(a);");
        assertTrue(check(lexer.getTokens(), Parser.getInstance(terminals)));

        lexer.generateTokens("new a typeof arraylist; put(a, 100); print(get(a, 0));");
        assertTrue(check(lexer.getTokens(), Parser.getInstance(terminals)));

        lexer.generateTokens("new a typeof hashset; new i = 0; put(a, i); print(get(a, i));");
        assertTrue(check(lexer.getTokens(), Parser.getInstance(terminals)));

        //get tests
        lexer.generateTokens("new a typeof hashset; new i = 100; put(a, i); i = get(a, i);");
        assertTrue(check(lexer.getTokens(), Parser.getInstance(terminals)));

        lexer.generateTokens("new a typeof arraylist; put(a, 100); i = get(a, 0);");
        assertTrue(check(lexer.getTokens(), Parser.getInstance(terminals)));

        //remove tests
        lexer.generateTokens("new a typeof hashset; new i = 100; put(a, i); remove(a, i);");
        assertTrue(check(lexer.getTokens(), Parser.getInstance(terminals)));

        lexer.generateTokens("new a typeof arraylist; put(a, 100); remove(a, 0);");
        assertTrue(check(lexer.getTokens(), Parser.getInstance(terminals)));

        //Combinations
        //TODO: Написать больше комбинаций
        lexer.generateTokens("while(a > b) {if (a < d) {do{i = i;}while(a < b)} else {for(i = i; i < 100; i = i + 1){i = i;}}}");
        assertTrue(check(lexer.getTokens(), Parser.getInstance(terminals)));

        lexer.generateTokens("a = b; c = a; a = a;");
        assertTrue(check(lexer.getTokens(), Parser.getInstance(terminals)));
    }

    @Test
    public void converterTests() {
        Lexer lexer = Lexer.getInstance();
        Set<Lexeme> terminals = lexer.getTerminals();

        lexer.generateTokens(""); //main program
        assertEquals("$", //rpn
                getRPN(lexer, Parser.getInstance(terminals)));

        //TODO: Написать больше тестов
        //while tests
        lexer.generateTokens("while(a < b) {a = a + 1;}");
        assertEquals("%a,%b,<,!F@10,#a,%a,1,+,=,!@0,$",
                getRPN(lexer, Parser.getInstance(terminals)));

        lexer.generateTokens("while(a < b & c > d) {}");
        assertEquals("%a,%b,<,%c,%d,>,&,!F@9,!@0,$",
                getRPN(lexer, Parser.getInstance(terminals)));

        lexer.generateTokens("while(((a < b) & (c > d)) | ((a > c) & (b < d))) {}");
        assertEquals("%a,%b,<,%c,%d,>,&,%a,%c,>,%b,%d,<,&,|,!F@17,!@0,$",
                getRPN(lexer, Parser.getInstance(terminals)));

        lexer.generateTokens("while((a < b) & (c > d) | (a > c) & (b < d)) {}");
        assertEquals("%a,%b,<,%c,%d,>,&,%a,%c,>,%b,%d,<,&,|,!F@17,!@0,$",
                getRPN(lexer, Parser.getInstance(terminals)));

        lexer.generateTokens("while(a < b) {if (a < b) {}}");
        assertEquals("%a,%b,<,!F@10,%a,%b,<,!F@9,!@9,!@0,$",
                getRPN(lexer, Parser.getInstance(terminals)));

        lexer.generateTokens("while(a < b) {do{} while(a < b)}");
        assertEquals("%a,%b,<,!F@9,%a,%b,<,!T@4,!@0,$",
                getRPN(lexer, Parser.getInstance(terminals)));


        lexer.generateTokens("while(a < b) {for(i = 1; i < 100; i = i + 1) {}}");
        assertEquals("%a,%b,<,!F@18,#i,1,=,%i,100,<,!F@17,#i,%i,1,+,=,!@7,!@0,$",
                getRPN(lexer, Parser.getInstance(terminals)));

        lexer.generateTokens("while(a < b) {new a typeof hashset; new i = 1; put(a, i);}");
        assertEquals("%a,%b,<,!F@16,#a,new,#hashset,typeof,#i,new,1,=,#a,#i,put,!@0,$",
                getRPN(lexer, Parser.getInstance(terminals)));


        //for tests
        lexer.generateTokens("for(i = 1; (i < n) & (n > i); i = i + 1) {}");
        assertEquals("#i,1,=,%i,%n,<,%n,%i,>,&,!F@17,#i,%i,1,+,=,!@3,$",
                getRPN(lexer, Parser.getInstance(terminals)));

    }

    @Test
    public void testOptimizer() {
        Lexer lexer = Lexer.getInstance();
        Set<Lexeme> terminals = lexer.getTerminals();

        lexer.generateTokens("print(100 / (25 + 25));");
        assertEquals("2.0,print,$",
                getRPN(lexer, Parser.getInstance(terminals)));

        lexer.generateTokens("print(1 / (100 * (50 - (1 / 0.16))));");
        assertEquals("2.2857142857142857E-4,print,$",
                getRPN(lexer, Parser.getInstance(terminals)));

        lexer.generateTokens("print(100 / (25 + 25 - a));");
        assertEquals("100,50.0,%a,-,/,print,$",
                getRPN(lexer, Parser.getInstance(terminals)));

        lexer.generateTokens("print(1 / (100 * (50 - (1 / 0.16 - a))));");
        assertEquals("1,100,50,6.25,%a,-,-,*,/,print,$",
                getRPN(lexer, Parser.getInstance(terminals)));
    }

    private boolean check(List<Token> tokens, Parser parser) {
        for (Token token : tokens) {
            token.accept(parser);
        }

        return true;
    }

    private String getRPN(Lexer lexer, Parser parser) {
        for (Token token : lexer.getTokens()) {
            token.accept(parser);
        }

        return listToString(parser.getRPN());
    }

    private String listToString(List<String> list) {
        StringBuilder out = new StringBuilder();
        for(String string : list) {
            out.append(string).append(",");
        }
        out.deleteCharAt(out.length() - 1);
        return out.toString();
    }
}
