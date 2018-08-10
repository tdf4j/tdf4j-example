package io.github.therealmone.spoLexer;

import org.junit.BeforeClass;
import org.junit.Test;
import io.github.therealmone.core.utils.ResourceLoader;

import static org.junit.Assert.*;

public class LexerTest {

    @BeforeClass
    public static void before() {
        ResourceLoader.initialize();
    }

    @Test
    public void testGenerate() {
        Lexer lexer = Lexer.getInstance();

        //Success tests
        lexer.generateTokens("value");
        assertEquals(2, lexer.getTokens().size());
        assertEquals("VAR", lexer.getTokens().get(0).getType());

        lexer.generateTokens("0");
        assertEquals(2, lexer.getTokens().size());
        assertEquals("DIGIT", lexer.getTokens().get(0).getType());

        lexer.generateTokens("=");
        assertEquals(2, lexer.getTokens().size());
        assertEquals("ASSIGN_OP", lexer.getTokens().get(0).getType());

        lexer.generateTokens("+");
        assertEquals(2, lexer.getTokens().size());
        assertEquals("OP", lexer.getTokens().get(0).getType());

        lexer.generateTokens("value = 15 + 0");
        assertEquals(6, lexer.getTokens().size());
        assertEquals("VAR", lexer.getTokens().get(0).getType());
        assertEquals("ASSIGN_OP", lexer.getTokens().get(1).getType());
        assertEquals("DIGIT", lexer.getTokens().get(2).getType());
        assertEquals("OP", lexer.getTokens().get(3).getType());
        assertEquals("DIGIT", lexer.getTokens().get(4).getType());

        lexer.generateTokens("15 = value - 10");
        assertEquals(6, lexer.getTokens().size());
        assertEquals("DIGIT", lexer.getTokens().get(0).getType());
        assertEquals("ASSIGN_OP", lexer.getTokens().get(1).getType());
        assertEquals("VAR", lexer.getTokens().get(2).getType());
        assertEquals("OP", lexer.getTokens().get(3).getType());
        assertEquals("DIGIT", lexer.getTokens().get(4).getType());

        lexer.generateTokens("= value / 0");
        assertEquals(5, lexer.getTokens().size());
        assertEquals("ASSIGN_OP", lexer.getTokens().get(0).getType());
        assertEquals("VAR", lexer.getTokens().get(1).getType());
        assertEquals("OP", lexer.getTokens().get(2).getType());
        assertEquals("DIGIT", lexer.getTokens().get(3).getType());

        lexer.generateTokens("* = value - 100000");
        assertEquals(6, lexer.getTokens().size());
        assertEquals("OP", lexer.getTokens().get(0).getType());
        assertEquals("ASSIGN_OP", lexer.getTokens().get(1).getType());
        assertEquals("VAR", lexer.getTokens().get(2).getType());
        assertEquals("OP", lexer.getTokens().get(3).getType());
        assertEquals("DIGIT", lexer.getTokens().get(4).getType());

        lexer.generateTokens("value = a + b - c / 0 * 10045645 = value1 = value2 = - * + /");
        assertEquals(23, lexer.getTokens().size());
        assertEquals("VAR", lexer.getTokens().get(0).getType());
        assertEquals("ASSIGN_OP", lexer.getTokens().get(1).getType());
        assertEquals("VAR", lexer.getTokens().get(2).getType());
        assertEquals("OP", lexer.getTokens().get(3).getType());
        assertEquals("VAR", lexer.getTokens().get(4).getType());
        assertEquals("OP", lexer.getTokens().get(5).getType());
        assertEquals("VAR", lexer.getTokens().get(6).getType());
        assertEquals("OP", lexer.getTokens().get(7).getType());
        assertEquals("DIGIT", lexer.getTokens().get(8).getType());
        assertEquals("OP", lexer.getTokens().get(9).getType());
        assertEquals("DIGIT", lexer.getTokens().get(10).getType());
        assertEquals("ASSIGN_OP", lexer.getTokens().get(11).getType());
        assertEquals("VAR", lexer.getTokens().get(12).getType());
        assertEquals("DIGIT", lexer.getTokens().get(13).getType());
        assertEquals("ASSIGN_OP", lexer.getTokens().get(14).getType());
        assertEquals("VAR", lexer.getTokens().get(15).getType());
        assertEquals("DIGIT", lexer.getTokens().get(16).getType());
        assertEquals("ASSIGN_OP", lexer.getTokens().get(17).getType());
        assertEquals("OP", lexer.getTokens().get(18).getType());
        assertEquals("OP", lexer.getTokens().get(19).getType());
        assertEquals("OP", lexer.getTokens().get(20).getType());
        assertEquals("OP", lexer.getTokens().get(21).getType());

        lexer.generateTokens("= - * + / value1 = value2 = a + b * 0 - 9999 = 10000 - 10000 * = value");
        assertEquals(26, lexer.getTokens().size());
        assertEquals("ASSIGN_OP", lexer.getTokens().get(0).getType());
        assertEquals("OP", lexer.getTokens().get(1).getType());
        assertEquals("OP", lexer.getTokens().get(2).getType());
        assertEquals("OP", lexer.getTokens().get(3).getType());
        assertEquals("OP", lexer.getTokens().get(4).getType());
        assertEquals("VAR", lexer.getTokens().get(5).getType());
        assertEquals("DIGIT", lexer.getTokens().get(6).getType());
        assertEquals("ASSIGN_OP", lexer.getTokens().get(7).getType());
        assertEquals("VAR", lexer.getTokens().get(8).getType());
        assertEquals("DIGIT", lexer.getTokens().get(9).getType());
        assertEquals("ASSIGN_OP", lexer.getTokens().get(10).getType());
        assertEquals("VAR", lexer.getTokens().get(11).getType());
        assertEquals("OP", lexer.getTokens().get(12).getType());
        assertEquals("VAR", lexer.getTokens().get(13).getType());
        assertEquals("OP", lexer.getTokens().get(14).getType());
        assertEquals("DIGIT", lexer.getTokens().get(15).getType());
        assertEquals("OP", lexer.getTokens().get(16).getType());
        assertEquals("DIGIT", lexer.getTokens().get(17).getType());
        assertEquals("ASSIGN_OP", lexer.getTokens().get(18).getType());
        assertEquals("DIGIT", lexer.getTokens().get(19).getType());
        assertEquals("OP", lexer.getTokens().get(20).getType());
        assertEquals("DIGIT", lexer.getTokens().get(21).getType());
        assertEquals("OP", lexer.getTokens().get(22).getType());
        assertEquals("ASSIGN_OP", lexer.getTokens().get(23).getType());
        assertEquals("VAR", lexer.getTokens().get(24).getType());

        lexer.generateTokens("= = = = = = = = = = = 10000 100 10 1 0 v a l u e * * * * - - - - / / /");
        assertEquals(33, lexer.getTokens().size());
        assertEquals("ASSIGN_OP", lexer.getTokens().get(0).getType());
        assertEquals("ASSIGN_OP", lexer.getTokens().get(1).getType());
        assertEquals("ASSIGN_OP", lexer.getTokens().get(2).getType());
        assertEquals("ASSIGN_OP", lexer.getTokens().get(3).getType());
        assertEquals("ASSIGN_OP", lexer.getTokens().get(4).getType());
        assertEquals("ASSIGN_OP", lexer.getTokens().get(5).getType());
        assertEquals("ASSIGN_OP", lexer.getTokens().get(6).getType());
        assertEquals("ASSIGN_OP", lexer.getTokens().get(7).getType());
        assertEquals("ASSIGN_OP", lexer.getTokens().get(8).getType());
        assertEquals("ASSIGN_OP", lexer.getTokens().get(9).getType());
        assertEquals("ASSIGN_OP", lexer.getTokens().get(10).getType());
        assertEquals("DIGIT", lexer.getTokens().get(11).getType());
        assertEquals("DIGIT", lexer.getTokens().get(12).getType());
        assertEquals("DIGIT", lexer.getTokens().get(13).getType());
        assertEquals("DIGIT", lexer.getTokens().get(14).getType());
        assertEquals("DIGIT", lexer.getTokens().get(15).getType());
        assertEquals("VAR", lexer.getTokens().get(16).getType());
        assertEquals("VAR", lexer.getTokens().get(17).getType());
        assertEquals("VAR", lexer.getTokens().get(18).getType());
        assertEquals("VAR", lexer.getTokens().get(19).getType());
        assertEquals("VAR", lexer.getTokens().get(20).getType());
        assertEquals("OP", lexer.getTokens().get(21).getType());
        assertEquals("OP", lexer.getTokens().get(22).getType());
        assertEquals("OP", lexer.getTokens().get(23).getType());
        assertEquals("OP", lexer.getTokens().get(24).getType());
        assertEquals("OP", lexer.getTokens().get(25).getType());
        assertEquals("OP", lexer.getTokens().get(26).getType());
        assertEquals("OP", lexer.getTokens().get(27).getType());
        assertEquals("OP", lexer.getTokens().get(28).getType());
        assertEquals("OP", lexer.getTokens().get(29).getType());
        assertEquals("OP", lexer.getTokens().get(30).getType());
        assertEquals("OP", lexer.getTokens().get(31).getType());


        lexer.generateTokens("$");
        assertEquals(1, lexer.getTokens().size());

        lexer.generateTokens("!= !");
        assertEquals("COP", lexer.getTokens().get(0).getType());
        assertEquals("LOP", lexer.getTokens().get(1).getType());

        lexer.generateTokens("0.155");
        assertEquals("DOUBLE", lexer.getTokens().get(0).getType());

        lexer.generateTokens("155.0000000");
        assertEquals("DOUBLE", lexer.getTokens().get(0).getType());

        lexer.generateTokens("print");
        assertEquals("PRINT", lexer.getTokens().get(0).getType());

        lexer.generateTokens("size");
        assertEquals("SIZE", lexer.getTokens().get(0).getType());

        lexer.generateTokens("else");
        assertEquals("ELSE", lexer.getTokens().get(0).getType());

        lexer.generateTokens("new");
        assertEquals("NEW", lexer.getTokens().get(0).getType());

        lexer.generateTokens("typeof");
        assertEquals("TYPEOF", lexer.getTokens().get(0).getType());

        lexer.generateTokens("arraylist");
        assertEquals("ARRAYLIST", lexer.getTokens().get(0).getType());

        lexer.generateTokens("hashset");
        assertEquals("HASHSET", lexer.getTokens().get(0).getType());

        lexer.generateTokens("get");
        assertEquals("GET", lexer.getTokens().get(0).getType());

        lexer.generateTokens("size");
        assertEquals("SIZE", lexer.getTokens().get(0).getType());

        lexer.generateTokens("put");
        assertEquals("PUT", lexer.getTokens().get(0).getType());

        lexer.generateTokens("remove");
        assertEquals("REMOVE", lexer.getTokens().get(0).getType());

        lexer.generateTokens("rewrite");
        assertEquals("REWRITE", lexer.getTokens().get(0).getType());

        lexer.generateTokens(",");
        assertEquals("COMMA", lexer.getTokens().get(0).getType());
    }
}
