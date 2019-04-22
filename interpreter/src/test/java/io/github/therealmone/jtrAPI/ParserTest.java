package io.github.therealmone.jtrAPI;

import io.github.therealmone.core.beans.Lexeme;
import io.github.therealmone.tdf4j.commons.Stream;
import io.github.therealmone.tdf4j.commons.Token;
import io.github.therealmone.tdf4j.generator.impl.ParserGenerator;
import io.github.therealmone.tdf4j.lexer.Lexer;
import io.github.therealmone.tdf4j.lexer.LexerFactory;
import io.github.therealmone.tdf4j.parser.Parser;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;


import java.util.List;
import java.util.Set;

public class ParserTest {

    @Test
    public void parserTests() {
        final Lexer lexer = new LexerFactory().withModule(new LexerModule());
        final Parser parser = new ParserGenerator().generate(new ParserModule());

        //while tests
        {
            final Stream<Token> stream = lexer.stream("while(a > b) {}$");
            assertTrue(check(stream, parser));
        }

        {
            final Stream<Token> stream = lexer.stream("while((a > b) & (c < d)) {}$");
            assertTrue(check(stream, parser));
        }

        {
            final Stream<Token> stream = lexer.stream("while((((((a > b))))) & (((((c < d)))))) {}$");
            assertTrue(check(stream, parser));
        }

        {
            final Stream<Token> stream = lexer.stream("while((a == b) & (c < d * (5 + 1)) | ((10 *(s + 150) <= 300))){}$");
            assertTrue(check(stream, parser));
        }

        {
            final Stream<Token> stream = lexer.stream("while(((((((((((c < d))))))))))) {}$");
            assertTrue(check(stream, parser));
        }

        {
            final Stream<Token> stream = lexer.stream("while(a < b & c > d) {}$");
            assertTrue(check(stream, parser));
        }

        {
            final Stream<Token> stream = lexer.stream("while(a == b & c < d * (5 + 1) | 10 * (s + 150) <= 300){}$");
            assertTrue(check(stream, parser));
        }

        {
            //if tests
            final Stream<Token> stream = lexer.stream("if(a > b) {}$");
            assertTrue(check(stream, parser));
        }

        {
            final Stream<Token> stream = lexer.stream("if((a > b) & (c < d)) {}$");
            assertTrue(check(stream, parser));
        }

        {
            final Stream<Token> stream = lexer.stream("if((((((a > b))))) & (((((c < d)))))) {}$");
            assertTrue(check(stream, parser));
        }

        {
            final Stream<Token> stream = lexer.stream("if((a == b) & (c < d * (5 + 1)) | ((10 *(s + 150) <= 300))){}$");
            assertTrue(check(stream, parser));
        }

        {
            final Stream<Token> stream = lexer.stream("if(((((((((((c < d))))))))))) {}$");
            assertTrue(check(stream, parser));
        }

        {
            final Stream<Token> stream = lexer.stream("if(a < b & c > d) {}$");
            assertTrue(check(stream, parser));
        }

        {
            final Stream<Token> stream = lexer.stream("if(a == b & c < d * (5 + 1) | 10 * (s + 150) <= 300){}$");
            assertTrue(check(stream, parser));
        }

        {
            //for tests
            final Stream<Token> stream = lexer.stream("for(i = 0; i < 100; i = i + 1) {}$");
            assertTrue(check(stream, parser));
        }

        {
            final Stream<Token> stream = lexer.stream("for(i = (((((a + b))))) * (((((c + d))))); i > 100; i = i + 1){}$");
            assertTrue(check(stream, parser));
        }

        {
            final Stream<Token> stream = lexer.stream("for(i = (((1 + 1) * (1 + 2)) * (1 + 1)) * (100 - 10); i > 100; i = i + 1){}$");
            assertTrue(check(stream, parser));
        }

        {
            //do-while tests
            final Stream<Token> stream = lexer.stream("do{}while(a > b)$");
            assertTrue(check(stream, parser));
        }

        {
            final Stream<Token> stream = lexer.stream("do{}while((a > b) & (c < d))$");
            assertTrue(check(stream, parser));
        }

        {
            final Stream<Token> stream = lexer.stream("do{}while((((((a > b))))) & (((((c < d))))))$");
            assertTrue(check(stream, parser));
        }

        {
            final Stream<Token> stream = lexer.stream("do{}while((a == b) & (c < d * (5 + 1)) | ((10 *(s + 150) <= 300)))$");
            assertTrue(check(stream, parser));
        }

        {
            final Stream<Token> stream = lexer.stream("do{}while(((((((((((c < d)))))))))))$");
            assertTrue(check(stream, parser));
        }

        {
            final Stream<Token> stream = lexer.stream("do{}while(a < b & c > d)$");
            assertTrue(check(stream, parser));
        }

        {
            final Stream<Token> stream = lexer.stream("do{}while(a == b & c < d * (5 + 1) | 10 * (s + 150) <= 300)$");
            assertTrue(check(stream, parser));
        }

        {
            //print tests
            final Stream<Token> stream = lexer.stream("print(0);$");
            assertTrue(check(stream, parser));
        }

        {
            final Stream<Token> stream = lexer.stream("new a = 100; print(a);$");
            assertTrue(check(stream, parser));
        }

        {
            final Stream<Token> stream = lexer.stream("new a typeof arraylist; put(a, 100); print(get(a, 0));$");
            assertTrue(check(stream, parser));
        }

        {
            final Stream<Token> stream = lexer.stream("new a typeof hashset; new i = 0; put(a, i); print(get(a, i));$");
            assertTrue(check(stream, parser));
        }

        {
            //get tests
            final Stream<Token> stream = lexer.stream("new a typeof hashset; new i = 100; put(a, i); i = get(a, i);$");
            assertTrue(check(stream, parser));
        }

        {
            final Stream<Token> stream = lexer.stream("new a typeof arraylist; put(a, 100); i = get(a, 0);$");
            assertTrue(check(stream, parser));
        }

        {
            //remove tests
            final Stream<Token> stream = lexer.stream("new a typeof hashset; new i = 100; put(a, i); remove(a, i);$");
            assertTrue(check(stream, parser));
        }

        {
            final Stream<Token> stream = lexer.stream("new a typeof arraylist; put(a, 100); remove(a, 0);$");
            assertTrue(check(stream, parser));
        }

        {
            //Combinations
            //TODO: Написать больше комбинаций
            final Stream<Token> stream = lexer.stream("while(a > b) {if (a < d) {do{i = i;}while(a < b)} else {for(i = i; i < 100; i = i + 1){i = i;}}}$");
            assertTrue(check(stream, parser));
        }

        {
            final Stream<Token> stream = lexer.stream("a = b; c = a; a = a;$");
            assertTrue(check(stream, parser));
        }
    }

//    @Test
//    @Ignore
//    public void converterTests() {
//        Lexer lexer = Lexer.getDefault();
//        Set<Lexeme> terminals = lexer.getTerminals();
//
//        lexer.generateTokens(""); //main program
//        assertEquals("$", //rpn
//                getRPN(lexer, Parser.getDefault(terminals)));
//
//        //TODO: Написать больше тестов
//        //while tests
//        lexer.generateTokens("while(a < b) {a = a + 1;}");
//        assertEquals("%a,%b,<,!F@10,#a,%a,1,+,=,!@0,$",
//                getRPN(lexer, Parser.getDefault(terminals)));
//
//        lexer.generateTokens("while(a < b & c > d) {}");
//        assertEquals("%a,%b,<,%c,%d,>,&,!F@9,!@0,$",
//                getRPN(lexer, Parser.getDefault(terminals)));
//
//        lexer.generateTokens("while(((a < b) & (c > d)) | ((a > c) & (b < d))) {}");
//        assertEquals("%a,%b,<,%c,%d,>,&,%a,%c,>,%b,%d,<,&,|,!F@17,!@0,$",
//                getRPN(lexer, Parser.getDefault(terminals)));
//
//        lexer.generateTokens("while((a < b) & (c > d) | (a > c) & (b < d)) {}");
//        assertEquals("%a,%b,<,%c,%d,>,&,%a,%c,>,%b,%d,<,&,|,!F@17,!@0,$",
//                getRPN(lexer, Parser.getDefault(terminals)));
//
//        lexer.generateTokens("while(a < b) {if (a < b) {}}");
//        assertEquals("%a,%b,<,!F@10,%a,%b,<,!F@9,!@9,!@0,$",
//                getRPN(lexer, Parser.getDefault(terminals)));
//
//        lexer.generateTokens("while(a < b) {do{} while(a < b)}");
//        assertEquals("%a,%b,<,!F@9,%a,%b,<,!T@4,!@0,$",
//                getRPN(lexer, Parser.getDefault(terminals)));
//
//
//        lexer.generateTokens("while(a < b) {for(i = 1; i < 100; i = i + 1) {}}");
//        assertEquals("%a,%b,<,!F@18,#i,1,=,%i,100,<,!F@17,#i,%i,1,+,=,!@7,!@0,$",
//                getRPN(lexer, Parser.getDefault(terminals)));
//
//        lexer.generateTokens("while(a < b) {new a typeof hashset; new i = 1; put(a, i);}");
//        assertEquals("%a,%b,<,!F@16,#a,new,#hashset,typeof,#i,new,1,=,#a,#i,put,!@0,$",
//                getRPN(lexer, Parser.getDefault(terminals)));
//
//
//        //for tests
//        lexer.generateTokens("for(i = 1; (i < n) & (n > i); i = i + 1) {}");
//        assertEquals("#i,1,=,%i,%n,<,%n,%i,>,&,!F@17,#i,%i,1,+,=,!@3,$",
//                getRPN(lexer, Parser.getDefault(terminals)));
//
//    }
//
//    @Test
//    @Ignore
//    public void testOptimizer() {
//        Lexer lexer = Lexer.getDefault();
//        Set<Lexeme> terminals = lexer.getTerminals();
//
//        lexer.generateTokens("print(100 / (25 + 25));");
//        assertEquals("2.0,print,$",
//                getRPN(lexer, Parser.getDefault(terminals)));
//
//        lexer.generateTokens("print(1 / (100 * (50 - (1 / 0.16))));");
//        assertEquals("2.2857142857142857E-4,print,$",
//                getRPN(lexer, Parser.getDefault(terminals)));
//
//        lexer.generateTokens("print(100 / (25 + 25 - a));");
//        assertEquals("100,50.0,%a,-,/,print,$",
//                getRPN(lexer, Parser.getDefault(terminals)));
//
//        lexer.generateTokens("print(1 / (100 * (50 - (1 / 0.16 - a))));");
//        assertEquals("1,100,50,6.25,%a,-,-,*,/,print,$",
//                getRPN(lexer, Parser.getDefault(terminals)));
//    }
//

    private boolean check(final Stream<Token> tokens, final Parser parser) {
        assertNotNull(parser.parse(tokens));
        return true;
    }

//    private String getRPN(Lexer lexer, Parser parser) {
//        for (Token token : lexer.getTokens()) {
//            token.accept(parser);
//        }
//
//        return listToString(parser.getRPN());
//    }

//    private String listToString(List<String> list) {
//        StringBuilder out = new StringBuilder();
//        for(String string : list) {
//            out.append(string).append(",");
//        }
//        out.deleteCharAt(out.length() - 1);
//        return out.toString();
//    }
}
