package ru.therealmone.SPOLexer;

import org.junit.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class lexerTest {

    private void addLexemes(Lexer lexer) {
        lexer.addLexeme("VAR", "^[a-z]+$", 4);
        lexer.addLexeme("DIGIT", "^0|([1-9][0-9]*)",3);
        lexer.addLexeme("ASSIGN_OP", "^=$", 2);
        lexer.addLexeme("OP", "^[\\+\\-\\/\\*]$", 1);
    }

    @Test
    public void testGenerate() {
        Lexer lexer = new Lexer();
        addLexemes(lexer);

        //Success tests
        lexer.generateTokens("value$");
        assertEquals(1, lexer.tokens.size());

        lexer.generateTokens("0$");
        assertEquals(1, lexer.tokens.size());

        lexer.generateTokens("=$");
        assertEquals(1, lexer.tokens.size());

        lexer.generateTokens("+$");
        assertEquals(1, lexer.tokens.size());

        lexer.generateTokens("value = 15 + 0$");
        assertEquals(5, lexer.tokens.size());

        lexer.generateTokens("15 = value - 10$");
        assertEquals(5, lexer.tokens.size());

        lexer.generateTokens("= value / 0$");
        assertEquals(4, lexer.tokens.size());

        lexer.generateTokens("* = value - 100000$");
        assertEquals(5, lexer.tokens.size());

        lexer.generateTokens("value = a + b - c / 0 * 10045645 = value1 = value2 = - * + /$");
        assertEquals(22, lexer.tokens.size());

        lexer.generateTokens("= - * + / value1 = value2 = a + b * 0 - 9999 = 10000 - 10000 * = value$");
        assertEquals(25, lexer.tokens.size());

        lexer.generateTokens("= = = = = = = = = = = 10000 100 10 1 0 v a l u e * * * * - - - - / / /$");
        assertEquals(32, lexer.tokens.size());

        lexer.generateTokens("$");
        assertEquals(0, lexer.tokens.size());
    }

    @Test
    public void testLexemes() {
        Lexer lexer = new Lexer();
        addLexemes(lexer);

        assertEquals(4, lexer.lexemes.size());
    }

    @Test
    public void testCompile() {
        Lexer lexer = new Lexer();
        addLexemes(lexer);

        //Success tests
        assertTrue(lexer.compile("VAR", "value"));
        assertTrue(lexer.compile("VAR", "v"));
        assertTrue(lexer.compile("VAR", "z"));
        assertTrue(lexer.compile("VAR", "va"));
        assertTrue(lexer.compile("VAR", "valuevaluevalue"));

        assertTrue(lexer.compile("DIGIT", "0"));
        assertTrue(lexer.compile("DIGIT", "1"));
        assertTrue(lexer.compile("DIGIT", "9"));
        assertTrue(lexer.compile("DIGIT", "100000"));
        assertTrue(lexer.compile("DIGIT", "100009"));
        assertTrue(lexer.compile("DIGIT", "19"));

        assertTrue(lexer.compile("ASSIGN_OP", "="));

        assertTrue(lexer.compile("OP", "+"));
        assertTrue(lexer.compile("OP", "-"));
        assertTrue(lexer.compile("OP", "/"));
        assertTrue(lexer.compile("OP", "*"));

        //Failure tests
        assertFalse(lexer.compile("VAR", "A"));
        assertFalse(lexer.compile("VAR", "Z"));
        assertFalse(lexer.compile("VAR", "value "));
        assertFalse(lexer.compile("VAR", " value"));
        assertFalse(lexer.compile("VAR", " value "));
        assertFalse(lexer.compile("VAR", " a "));
        assertFalse(lexer.compile("VAR", "valuE"));
        assertFalse(lexer.compile("VAR", "Value"));
        assertFalse(lexer.compile("VAR", "vaLue"));
        assertFalse(lexer.compile("VAR", "valuevalue value"));

        assertFalse(lexer.compile("DIGIT", "09"));
        assertFalse(lexer.compile("DIGIT", "000000"));

        assertFalse(lexer.compile("ASSIGN_OP", "=="));
        assertFalse(lexer.compile("ASSIGN_OP", ":="));
        assertFalse(lexer.compile("ASSIGN_OP", "!="));

        assertFalse(lexer.compile("OP", "+-"));
        assertFalse(lexer.compile("OP", "/*"));
        assertFalse(lexer.compile("OP", "+*"));
        assertFalse(lexer.compile("OP", "="));
        assertFalse(lexer.compile("OP", "=="));
    }
}
