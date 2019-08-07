package org.tdf4j.example.jtrAPI;

import io.github.therealmone.tdf4j.generator.impl.LexerGenerator;
import io.github.therealmone.tdf4j.lexer.Lexer;
import io.github.therealmone.tdf4j.model.Token;
import io.github.therealmone.tdf4j.tdfparser.TdfParser;
import io.github.therealmone.tdf4j.tdfparser.TdfParserGenerator;
import org.junit.BeforeClass;
import org.junit.Test;


import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class LexerTest {
    private static TdfParser tdfParser;

    @BeforeClass
    public static void beforeClass() {
        try {
            tdfParser = new TdfParserGenerator(Thread.currentThread().getContextClassLoader().getResourceAsStream("grammar.tdf")).generate();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGenerate() throws IOException {
        Lexer lexer = new LexerGenerator(tdfParser.getLexerModule()).generate();

        //Success tests
        {
            final List<Token> tokens = lexer.analyze("value");
            assertEquals(1, tokens.size());
            assertEquals("VAR", tokens.get(0).tag().value());
        }

        {
            final List<Token> tokens = lexer.analyze("0");
            assertEquals(1, tokens.size());
            assertEquals("DIGIT", tokens.get(0).tag().value());
        }

        {
            final List<Token> tokens = lexer.analyze("=");
            assertEquals(1, tokens.size());
            assertEquals("ASSIGN_OP", tokens.get(0).tag().value());
        }

        {
            final List<Token> tokens = lexer.analyze("+");
            assertEquals(1, tokens.size());
            assertEquals("OP", tokens.get(0).tag().value());
        }

        {
            final List<Token> tokens = lexer.analyze("value = 15 + 0");
            assertEquals(5, tokens.size());
            assertEquals("VAR", tokens.get(0).tag().value());
            assertEquals("ASSIGN_OP", tokens.get(1).tag().value());
            assertEquals("DIGIT", tokens.get(2).tag().value());
            assertEquals("OP", tokens.get(3).tag().value());
            assertEquals("DIGIT", tokens.get(4).tag().value());
        }

        {
            final List<Token> tokens = lexer.analyze("15 = value - 10");
            assertEquals(5, tokens.size());
            assertEquals("DIGIT", tokens.get(0).tag().value());
            assertEquals("ASSIGN_OP", tokens.get(1).tag().value());
            assertEquals("VAR", tokens.get(2).tag().value());
            assertEquals("OP", tokens.get(3).tag().value());
            assertEquals("DIGIT", tokens.get(4).tag().value());
        }

        {
            final List<Token> tokens = lexer.analyze("= value / 0");
            assertEquals(4, tokens.size());
            assertEquals("ASSIGN_OP", tokens.get(0).tag().value());
            assertEquals("VAR", tokens.get(1).tag().value());
            assertEquals("OP", tokens.get(2).tag().value());
            assertEquals("DIGIT", tokens.get(3).tag().value());
        }

        {
            final List<Token> tokens = lexer.analyze("* = value - 100000");
            assertEquals(5, tokens.size());
            assertEquals("OP", tokens.get(0).tag().value());
            assertEquals("ASSIGN_OP", tokens.get(1).tag().value());
            assertEquals("VAR", tokens.get(2).tag().value());
            assertEquals("OP", tokens.get(3).tag().value());
            assertEquals("DIGIT", tokens.get(4).tag().value());
        }

        {
            final List<Token> tokens = lexer.analyze("value = a + b - c / 0 * 10045645 = value1 = value2 = - * + /");
            assertEquals(22, tokens.size());
            assertEquals("VAR", tokens.get(0).tag().value());
            assertEquals("ASSIGN_OP", tokens.get(1).tag().value());
            assertEquals("VAR", tokens.get(2).tag().value());
            assertEquals("OP", tokens.get(3).tag().value());
            assertEquals("VAR", tokens.get(4).tag().value());
            assertEquals("OP", tokens.get(5).tag().value());
            assertEquals("VAR", tokens.get(6).tag().value());
            assertEquals("OP", tokens.get(7).tag().value());
            assertEquals("DIGIT", tokens.get(8).tag().value());
            assertEquals("OP", tokens.get(9).tag().value());
            assertEquals("DIGIT", tokens.get(10).tag().value());
            assertEquals("ASSIGN_OP", tokens.get(11).tag().value());
            assertEquals("VAR", tokens.get(12).tag().value());
            assertEquals("DIGIT", tokens.get(13).tag().value());
            assertEquals("ASSIGN_OP", tokens.get(14).tag().value());
            assertEquals("VAR", tokens.get(15).tag().value());
            assertEquals("DIGIT", tokens.get(16).tag().value());
            assertEquals("ASSIGN_OP", tokens.get(17).tag().value());
            assertEquals("OP", tokens.get(18).tag().value());
            assertEquals("OP", tokens.get(19).tag().value());
            assertEquals("OP", tokens.get(20).tag().value());
            assertEquals("OP", tokens.get(21).tag().value());
        }

        {
            final List<Token> tokens = lexer.analyze("= - * + / value1 = value2 = a + b * 0 - 9999 = 10000 - 10000 * = value");
            assertEquals(25, tokens.size());
            assertEquals("ASSIGN_OP", tokens.get(0).tag().value());
            assertEquals("OP", tokens.get(1).tag().value());
            assertEquals("OP", tokens.get(2).tag().value());
            assertEquals("OP", tokens.get(3).tag().value());
            assertEquals("OP", tokens.get(4).tag().value());
            assertEquals("VAR", tokens.get(5).tag().value());
            assertEquals("DIGIT", tokens.get(6).tag().value());
            assertEquals("ASSIGN_OP", tokens.get(7).tag().value());
            assertEquals("VAR", tokens.get(8).tag().value());
            assertEquals("DIGIT", tokens.get(9).tag().value());
            assertEquals("ASSIGN_OP", tokens.get(10).tag().value());
            assertEquals("VAR", tokens.get(11).tag().value());
            assertEquals("OP", tokens.get(12).tag().value());
            assertEquals("VAR", tokens.get(13).tag().value());
            assertEquals("OP", tokens.get(14).tag().value());
            assertEquals("DIGIT", tokens.get(15).tag().value());
            assertEquals("OP", tokens.get(16).tag().value());
            assertEquals("DIGIT", tokens.get(17).tag().value());
            assertEquals("ASSIGN_OP", tokens.get(18).tag().value());
            assertEquals("DIGIT", tokens.get(19).tag().value());
            assertEquals("OP", tokens.get(20).tag().value());
            assertEquals("DIGIT", tokens.get(21).tag().value());
            assertEquals("OP", tokens.get(22).tag().value());
            assertEquals("ASSIGN_OP", tokens.get(23).tag().value());
            assertEquals("VAR", tokens.get(24).tag().value());
        }

        {
            final List<Token> tokens = lexer.analyze("= = = = = = = = = = = 10000 100 10 1 0 v a l u e * * * * - - - - / / /");
            assertEquals(32, tokens.size());
            assertEquals("ASSIGN_OP", tokens.get(0).tag().value());
            assertEquals("ASSIGN_OP", tokens.get(1).tag().value());
            assertEquals("ASSIGN_OP", tokens.get(2).tag().value());
            assertEquals("ASSIGN_OP", tokens.get(3).tag().value());
            assertEquals("ASSIGN_OP", tokens.get(4).tag().value());
            assertEquals("ASSIGN_OP", tokens.get(5).tag().value());
            assertEquals("ASSIGN_OP", tokens.get(6).tag().value());
            assertEquals("ASSIGN_OP", tokens.get(7).tag().value());
            assertEquals("ASSIGN_OP", tokens.get(8).tag().value());
            assertEquals("ASSIGN_OP", tokens.get(9).tag().value());
            assertEquals("ASSIGN_OP", tokens.get(10).tag().value());
            assertEquals("DIGIT", tokens.get(11).tag().value());
            assertEquals("DIGIT", tokens.get(12).tag().value());
            assertEquals("DIGIT", tokens.get(13).tag().value());
            assertEquals("DIGIT", tokens.get(14).tag().value());
            assertEquals("DIGIT", tokens.get(15).tag().value());
            assertEquals("VAR", tokens.get(16).tag().value());
            assertEquals("VAR", tokens.get(17).tag().value());
            assertEquals("VAR", tokens.get(18).tag().value());
            assertEquals("VAR", tokens.get(19).tag().value());
            assertEquals("VAR", tokens.get(20).tag().value());
            assertEquals("OP", tokens.get(21).tag().value());
            assertEquals("OP", tokens.get(22).tag().value());
            assertEquals("OP", tokens.get(23).tag().value());
            assertEquals("OP", tokens.get(24).tag().value());
            assertEquals("OP", tokens.get(25).tag().value());
            assertEquals("OP", tokens.get(26).tag().value());
            assertEquals("OP", tokens.get(27).tag().value());
            assertEquals("OP", tokens.get(28).tag().value());
            assertEquals("OP", tokens.get(29).tag().value());
            assertEquals("OP", tokens.get(30).tag().value());
            assertEquals("OP", tokens.get(31).tag().value());
        }


        {
            final List<Token> tokens = lexer.analyze("$");
            assertEquals(1, tokens.size());
        }

        {
            final List<Token> tokens = lexer.analyze("!= !");
            assertEquals("COP", tokens.get(0).tag().value());
            assertEquals("LOP", tokens.get(1).tag().value());
        }

        {
            final List<Token> tokens = lexer.analyze("0.155");
            assertEquals("DOUBLE", tokens.get(0).tag().value());
        }

        {
            final List<Token> tokens = lexer.analyze("155.0000000");
            assertEquals("DOUBLE", tokens.get(0).tag().value());
        }

        {
            final List<Token> tokens = lexer.analyze("print");
            assertEquals("PRINT", tokens.get(0).tag().value());
        }

        {
            final List<Token> tokens = lexer.analyze("size");
            assertEquals("SIZE", tokens.get(0).tag().value());
        }

        {
            final List<Token> tokens = lexer.analyze("else");
            assertEquals("ELSE", tokens.get(0).tag().value());
        }

        {
            final List<Token> tokens = lexer.analyze("new");
            assertEquals("NEW", tokens.get(0).tag().value());
        }

        {
            final List<Token> tokens = lexer.analyze("typeof");
            assertEquals("TYPEOF", tokens.get(0).tag().value());
        }

        {
            final List<Token> tokens = lexer.analyze("arraylist");
            assertEquals("ARRAYLIST", tokens.get(0).tag().value());
        }

        {
            final List<Token> tokens = lexer.analyze("hashset");
            assertEquals("HASHSET", tokens.get(0).tag().value());
        }

        {
            final List<Token> tokens = lexer.analyze("get");
            assertEquals("GET", tokens.get(0).tag().value());
        }

        {
            final List<Token> tokens = lexer.analyze("size");
            assertEquals("SIZE", tokens.get(0).tag().value());
        }

        {
            final List<Token> tokens = lexer.analyze("put");
            assertEquals("PUT", tokens.get(0).tag().value());
        }

        {
            final List<Token> tokens = lexer.analyze("remove");
            assertEquals("REMOVE", tokens.get(0).tag().value());
        }

        {
            final List<Token> tokens = lexer.analyze("rewrite");
            assertEquals("REWRITE", tokens.get(0).tag().value());
        }

        {
            final List<Token> tokens = lexer.analyze(",");
            assertEquals("COMMA", tokens.get(0).tag().value());
        }
    }
}
