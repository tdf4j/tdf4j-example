package ru.therealmone.spoLexer;

import org.junit.BeforeClass;
import org.junit.Test;
import ru.therealmone.translatorAPI.Exceptions.LexerException;
import ru.therealmone.translatorAPI.ResourceLoader;

import static org.junit.Assert.*;

public class LexerTest {

    @BeforeClass
    public static void before() {
        ResourceLoader.loadLexemes("../TranslatorAPI/src/main/resources/", "lexemes.xml");
        ResourceLoader.loadLangRules("../TranslatorAPI/src/main/resources/", "langRules.csv");
        ResourceLoader.loadAnalyzeTable("../TranslatorAPI/src/main/resources/", "analyzeTable.csv");
        ResourceLoader.loadCommands("../TranslatorAPI/src/main/resources/", "commands.xml");
    }

    @Test
    public void testGenerate() {
        Lexer lexer = new Lexer();

        //Success tests
        lexer.generateTokens("value");
        assertEquals(2, lexer.tokens.size());
        assertEquals("VAR", lexer.tokens.get(0).getType());

        lexer.generateTokens("0");
        assertEquals(2, lexer.tokens.size());
        assertEquals("DIGIT", lexer.tokens.get(0).getType());

        lexer.generateTokens("=");
        assertEquals(2, lexer.tokens.size());
        assertEquals("ASSIGN_OP", lexer.tokens.get(0).getType());

        lexer.generateTokens("+");
        assertEquals(2, lexer.tokens.size());
        assertEquals("OP", lexer.tokens.get(0).getType());

        lexer.generateTokens("value = 15 + 0");
        assertEquals(6, lexer.tokens.size());
        assertEquals("VAR", lexer.tokens.get(0).getType());
        assertEquals("ASSIGN_OP", lexer.tokens.get(1).getType());
        assertEquals("DIGIT", lexer.tokens.get(2).getType());
        assertEquals("OP", lexer.tokens.get(3).getType());
        assertEquals("DIGIT", lexer.tokens.get(4).getType());

        lexer.generateTokens("15 = value - 10");
        assertEquals(6, lexer.tokens.size());
        assertEquals("DIGIT", lexer.tokens.get(0).getType());
        assertEquals("ASSIGN_OP", lexer.tokens.get(1).getType());
        assertEquals("VAR", lexer.tokens.get(2).getType());
        assertEquals("OP", lexer.tokens.get(3).getType());
        assertEquals("DIGIT", lexer.tokens.get(4).getType());

        lexer.generateTokens("= value / 0");
        assertEquals(5, lexer.tokens.size());
        assertEquals("ASSIGN_OP", lexer.tokens.get(0).getType());
        assertEquals("VAR", lexer.tokens.get(1).getType());
        assertEquals("OP", lexer.tokens.get(2).getType());
        assertEquals("DIGIT", lexer.tokens.get(3).getType());

        lexer.generateTokens("* = value - 100000");
        assertEquals(6, lexer.tokens.size());
        assertEquals("OP", lexer.tokens.get(0).getType());
        assertEquals("ASSIGN_OP", lexer.tokens.get(1).getType());
        assertEquals("VAR", lexer.tokens.get(2).getType());
        assertEquals("OP", lexer.tokens.get(3).getType());
        assertEquals("DIGIT", lexer.tokens.get(4).getType());

        lexer.generateTokens("value = a + b - c / 0 * 10045645 = value1 = value2 = - * + /");
        assertEquals(23, lexer.tokens.size());
        assertEquals("VAR", lexer.tokens.get(0).getType());
        assertEquals("ASSIGN_OP", lexer.tokens.get(1).getType());
        assertEquals("VAR", lexer.tokens.get(2).getType());
        assertEquals("OP", lexer.tokens.get(3).getType());
        assertEquals("VAR", lexer.tokens.get(4).getType());
        assertEquals("OP", lexer.tokens.get(5).getType());
        assertEquals("VAR", lexer.tokens.get(6).getType());
        assertEquals("OP", lexer.tokens.get(7).getType());
        assertEquals("DIGIT", lexer.tokens.get(8).getType());
        assertEquals("OP", lexer.tokens.get(9).getType());
        assertEquals("DIGIT", lexer.tokens.get(10).getType());
        assertEquals("ASSIGN_OP", lexer.tokens.get(11).getType());
        assertEquals("VAR", lexer.tokens.get(12).getType());
        assertEquals("DIGIT", lexer.tokens.get(13).getType());
        assertEquals("ASSIGN_OP", lexer.tokens.get(14).getType());
        assertEquals("VAR", lexer.tokens.get(15).getType());
        assertEquals("DIGIT", lexer.tokens.get(16).getType());
        assertEquals("ASSIGN_OP", lexer.tokens.get(17).getType());
        assertEquals("OP", lexer.tokens.get(18).getType());
        assertEquals("OP", lexer.tokens.get(19).getType());
        assertEquals("OP", lexer.tokens.get(20).getType());
        assertEquals("OP", lexer.tokens.get(21).getType());

        lexer.generateTokens("= - * + / value1 = value2 = a + b * 0 - 9999 = 10000 - 10000 * = value");
        assertEquals(26, lexer.tokens.size());
        assertEquals("ASSIGN_OP", lexer.tokens.get(0).getType());
        assertEquals("OP", lexer.tokens.get(1).getType());
        assertEquals("OP", lexer.tokens.get(2).getType());
        assertEquals("OP", lexer.tokens.get(3).getType());
        assertEquals("OP", lexer.tokens.get(4).getType());
        assertEquals("VAR", lexer.tokens.get(5).getType());
        assertEquals("DIGIT", lexer.tokens.get(6).getType());
        assertEquals("ASSIGN_OP", lexer.tokens.get(7).getType());
        assertEquals("VAR", lexer.tokens.get(8).getType());
        assertEquals("DIGIT", lexer.tokens.get(9).getType());
        assertEquals("ASSIGN_OP", lexer.tokens.get(10).getType());
        assertEquals("VAR", lexer.tokens.get(11).getType());
        assertEquals("OP", lexer.tokens.get(12).getType());
        assertEquals("VAR", lexer.tokens.get(13).getType());
        assertEquals("OP", lexer.tokens.get(14).getType());
        assertEquals("DIGIT", lexer.tokens.get(15).getType());
        assertEquals("OP", lexer.tokens.get(16).getType());
        assertEquals("DIGIT", lexer.tokens.get(17).getType());
        assertEquals("ASSIGN_OP", lexer.tokens.get(18).getType());
        assertEquals("DIGIT", lexer.tokens.get(19).getType());
        assertEquals("OP", lexer.tokens.get(20).getType());
        assertEquals("DIGIT", lexer.tokens.get(21).getType());
        assertEquals("OP", lexer.tokens.get(22).getType());
        assertEquals("ASSIGN_OP", lexer.tokens.get(23).getType());
        assertEquals("VAR", lexer.tokens.get(24).getType());

        lexer.generateTokens("= = = = = = = = = = = 10000 100 10 1 0 v a l u e * * * * - - - - / / /");
        assertEquals(33, lexer.tokens.size());
        assertEquals("ASSIGN_OP", lexer.tokens.get(0).getType());
        assertEquals("ASSIGN_OP", lexer.tokens.get(1).getType());
        assertEquals("ASSIGN_OP", lexer.tokens.get(2).getType());
        assertEquals("ASSIGN_OP", lexer.tokens.get(3).getType());
        assertEquals("ASSIGN_OP", lexer.tokens.get(4).getType());
        assertEquals("ASSIGN_OP", lexer.tokens.get(5).getType());
        assertEquals("ASSIGN_OP", lexer.tokens.get(6).getType());
        assertEquals("ASSIGN_OP", lexer.tokens.get(7).getType());
        assertEquals("ASSIGN_OP", lexer.tokens.get(8).getType());
        assertEquals("ASSIGN_OP", lexer.tokens.get(9).getType());
        assertEquals("ASSIGN_OP", lexer.tokens.get(10).getType());
        assertEquals("DIGIT", lexer.tokens.get(11).getType());
        assertEquals("DIGIT", lexer.tokens.get(12).getType());
        assertEquals("DIGIT", lexer.tokens.get(13).getType());
        assertEquals("DIGIT", lexer.tokens.get(14).getType());
        assertEquals("DIGIT", lexer.tokens.get(15).getType());
        assertEquals("VAR", lexer.tokens.get(16).getType());
        assertEquals("VAR", lexer.tokens.get(17).getType());
        assertEquals("VAR", lexer.tokens.get(18).getType());
        assertEquals("VAR", lexer.tokens.get(19).getType());
        assertEquals("VAR", lexer.tokens.get(20).getType());
        assertEquals("OP", lexer.tokens.get(21).getType());
        assertEquals("OP", lexer.tokens.get(22).getType());
        assertEquals("OP", lexer.tokens.get(23).getType());
        assertEquals("OP", lexer.tokens.get(24).getType());
        assertEquals("OP", lexer.tokens.get(25).getType());
        assertEquals("OP", lexer.tokens.get(26).getType());
        assertEquals("OP", lexer.tokens.get(27).getType());
        assertEquals("OP", lexer.tokens.get(28).getType());
        assertEquals("OP", lexer.tokens.get(29).getType());
        assertEquals("OP", lexer.tokens.get(30).getType());
        assertEquals("OP", lexer.tokens.get(31).getType());


        lexer.generateTokens("$");
        assertEquals(1, lexer.tokens.size());

        lexer.generateTokens("!= !");
        assertEquals("COP", lexer.tokens.get(0).getType());
        assertEquals("LOP", lexer.tokens.get(1).getType());

        lexer.generateTokens("0.155");
        assertEquals("DOUBLE", lexer.tokens.get(0).getType());

        lexer.generateTokens("155.0000000");
        assertEquals("DOUBLE", lexer.tokens.get(0).getType());

        lexer.generateTokens("print");
        assertEquals("PRINT", lexer.tokens.get(0).getType());

        lexer.generateTokens("size");
        assertEquals("SIZE", lexer.tokens.get(0).getType());

        lexer.generateTokens("else");
        assertEquals("ELSE", lexer.tokens.get(0).getType());

        lexer.generateTokens("new");
        assertEquals("NEW", lexer.tokens.get(0).getType());

        lexer.generateTokens("typeof");
        assertEquals("TYPEOF", lexer.tokens.get(0).getType());

        lexer.generateTokens("arraylist");
        assertEquals("ARRAYLIST", lexer.tokens.get(0).getType());

        lexer.generateTokens("hashset");
        assertEquals("HASHSET", lexer.tokens.get(0).getType());

        lexer.generateTokens("get");
        assertEquals("GET", lexer.tokens.get(0).getType());

        lexer.generateTokens("size");
        assertEquals("SIZE", lexer.tokens.get(0).getType());

        lexer.generateTokens("put");
        assertEquals("PUT", lexer.tokens.get(0).getType());

        lexer.generateTokens("remove");
        assertEquals("REMOVE", lexer.tokens.get(0).getType());

        lexer.generateTokens("rewrite");
        assertEquals("REWRITE", lexer.tokens.get(0).getType());

        lexer.generateTokens(",");
        assertEquals("COMMA", lexer.tokens.get(0).getType());
    }


    @Test
    public void testCompile() {
        Lexer lexer = new Lexer();

        //Success tests
        assertTrue(lexer.match("VAR", "value"));
        assertTrue(lexer.match("VAR", "v"));
        assertTrue(lexer.match("VAR", "z"));
        assertTrue(lexer.match("VAR", "va"));
        assertTrue(lexer.match("VAR", "valuevaluevalue"));

        assertTrue(lexer.match("DIGIT", "0"));
        assertTrue(lexer.match("DIGIT", "1"));
        assertTrue(lexer.match("DIGIT", "9"));
        assertTrue(lexer.match("DIGIT", "100000"));
        assertTrue(lexer.match("DIGIT", "100009"));
        assertTrue(lexer.match("DIGIT", "19"));

        assertTrue(lexer.match("ASSIGN_OP", "="));

        assertTrue(lexer.match("OP", "+"));
        assertTrue(lexer.match("OP", "-"));
        assertTrue(lexer.match("OP", "/"));
        assertTrue(lexer.match("OP", "*"));

        //Failure tests
        assertFalse(lexer.match("VAR", "A"));
        assertFalse(lexer.match("VAR", "Z"));
        assertFalse(lexer.match("VAR", "value "));
        assertFalse(lexer.match("VAR", " value"));
        assertFalse(lexer.match("VAR", " value "));
        assertFalse(lexer.match("VAR", " a "));
        assertFalse(lexer.match("VAR", "valuE"));
        assertFalse(lexer.match("VAR", "Value"));
        assertFalse(lexer.match("VAR", "vaLue"));
        assertFalse(lexer.match("VAR", "valuevalue value"));

        assertFalse(lexer.match("DIGIT", "09"));
        assertFalse(lexer.match("DIGIT", "000000"));

        assertFalse(lexer.match("ASSIGN_OP", "=="));
        assertFalse(lexer.match("ASSIGN_OP", ":="));
        assertFalse(lexer.match("ASSIGN_OP", "!="));

        assertFalse(lexer.match("OP", "+-"));
        assertFalse(lexer.match("OP", "/*"));
        assertFalse(lexer.match("OP", "+*"));
        assertFalse(lexer.match("OP", "="));
        assertFalse(lexer.match("OP", "=="));
    }
}
