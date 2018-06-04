package ru.therealmone.spoParser;

import org.junit.Test;
import static org.junit.Assert.*;

import ru.therealmone.spoLexer.Lexer;
import ru.therealmone.translatorAPI.Exceptions.TranslatorException;
import ru.therealmone.translatorAPI.Token;

import java.util.ArrayList;
import java.util.HashSet;

public class ParserTest {
    private static final String LEXEMES_DIR = "D:/JavaProjects/SPOTranslator/spoLexer/src/main/resources/lexemes.xml";
    private static final String LANG_RULES_DIR = "D:/JavaProjects/SPOTranslator/spoParser/src/main/resources/langRules.csv";
    private static final String ANALYZE_TABLE_DIR = "D:/JavaProjects/SPOTranslator/spoParser/src/main/resources/analyzeTable.csv";

    @Test
    public void parserTests() {
        try {
            Lexer lexer = new Lexer(LEXEMES_DIR);
            HashSet<String> terminals = new HashSet<>(lexer.lexemes.keySet());

            //while tests
            lexer.generateTokens("while(a > b) {}");
            assertTrue(check(lexer.tokens, new Parser(LANG_RULES_DIR, ANALYZE_TABLE_DIR, terminals)));

            lexer.generateTokens("while((a > b) & (c < d)) {}");
            assertTrue(check(lexer.tokens, new Parser(LANG_RULES_DIR, ANALYZE_TABLE_DIR, terminals)));

            lexer.generateTokens("while((((((a > b))))) & (((((c < d)))))) {}");
            assertTrue(check(lexer.tokens, new Parser(LANG_RULES_DIR, ANALYZE_TABLE_DIR, terminals)));

            lexer.generateTokens("while((a == b) & (c < d * (5 + 1)) | ((10 *(s + 150) <= 300))){}");
            assertTrue(check(lexer.tokens, new Parser(LANG_RULES_DIR, ANALYZE_TABLE_DIR, terminals)));

            lexer.generateTokens("while(((((((((((c < d))))))))))) {}");
            assertTrue(check(lexer.tokens, new Parser(LANG_RULES_DIR, ANALYZE_TABLE_DIR, terminals)));

            lexer.generateTokens("while(a < b & c > d) {}");
            assertTrue(check(lexer.tokens, new Parser(LANG_RULES_DIR, ANALYZE_TABLE_DIR, terminals)));

            lexer.generateTokens("while(a == b & c < d * (5 + 1) | 10 * (s + 150) <= 300){}");
            assertTrue(check(lexer.tokens, new Parser(LANG_RULES_DIR, ANALYZE_TABLE_DIR, terminals)));

            //if tests
            lexer.generateTokens("if(a > b) {}");
            assertTrue(check(lexer.tokens, new Parser(LANG_RULES_DIR, ANALYZE_TABLE_DIR, terminals)));

            lexer.generateTokens("if((a > b) & (c < d)) {}");
            assertTrue(check(lexer.tokens, new Parser(LANG_RULES_DIR, ANALYZE_TABLE_DIR, terminals)));

            lexer.generateTokens("if((((((a > b))))) & (((((c < d)))))) {}");
            assertTrue(check(lexer.tokens, new Parser(LANG_RULES_DIR, ANALYZE_TABLE_DIR, terminals)));

            lexer.generateTokens("if((a == b) & (c < d * (5 + 1)) | ((10 *(s + 150) <= 300))){}");
            assertTrue(check(lexer.tokens, new Parser(LANG_RULES_DIR, ANALYZE_TABLE_DIR, terminals)));

            lexer.generateTokens("if(((((((((((c < d))))))))))) {}");
            assertTrue(check(lexer.tokens, new Parser(LANG_RULES_DIR, ANALYZE_TABLE_DIR, terminals)));

            lexer.generateTokens("if(a < b & c > d) {}");
            assertTrue(check(lexer.tokens, new Parser(LANG_RULES_DIR, ANALYZE_TABLE_DIR, terminals)));

            lexer.generateTokens("if(a == b & c < d * (5 + 1) | 10 * (s + 150) <= 300){}");
            assertTrue(check(lexer.tokens, new Parser(LANG_RULES_DIR, ANALYZE_TABLE_DIR, terminals)));

            //for tests
            lexer.generateTokens("for(i = 0; i < 100; i = i + 1) {}");
            assertTrue(check(lexer.tokens, new Parser(LANG_RULES_DIR, ANALYZE_TABLE_DIR, terminals)));

            lexer.generateTokens("for(i = (((((a + b))))) * (((((c + d))))); i > 100; i = i + 1){}");
            assertTrue(check(lexer.tokens, new Parser(LANG_RULES_DIR, ANALYZE_TABLE_DIR, terminals)));

            lexer.generateTokens("for(i = (((1 + 1) * (1 + 2)) * (1 + 1)) * (100 - 10); i > 100; i = i + 1){}");
            assertTrue(check(lexer.tokens, new Parser(LANG_RULES_DIR, ANALYZE_TABLE_DIR, terminals)));

            //do-while tests
            lexer.generateTokens("do{}while(a > b)");
            assertTrue(check(lexer.tokens, new Parser(LANG_RULES_DIR, ANALYZE_TABLE_DIR, terminals)));

            lexer.generateTokens("do{}while((a > b) & (c < d))");
            assertTrue(check(lexer.tokens, new Parser(LANG_RULES_DIR, ANALYZE_TABLE_DIR, terminals)));

            lexer.generateTokens("do{}while((((((a > b))))) & (((((c < d))))))");
            assertTrue(check(lexer.tokens, new Parser(LANG_RULES_DIR, ANALYZE_TABLE_DIR, terminals)));

            lexer.generateTokens("do{}while((a == b) & (c < d * (5 + 1)) | ((10 *(s + 150) <= 300)))");
            assertTrue(check(lexer.tokens, new Parser(LANG_RULES_DIR, ANALYZE_TABLE_DIR, terminals)));

            lexer.generateTokens("do{}while(((((((((((c < d)))))))))))");
            assertTrue(check(lexer.tokens, new Parser(LANG_RULES_DIR, ANALYZE_TABLE_DIR, terminals)));

            lexer.generateTokens("do{}while(a < b & c > d)");
            assertTrue(check(lexer.tokens, new Parser(LANG_RULES_DIR, ANALYZE_TABLE_DIR, terminals)));

            lexer.generateTokens("do{}while(a == b & c < d * (5 + 1) | 10 * (s + 150) <= 300)");
            assertTrue(check(lexer.tokens, new Parser(LANG_RULES_DIR, ANALYZE_TABLE_DIR, terminals)));

            //Combinations
            //TODO: Написать больше комбинаций
            lexer.generateTokens("while(a > b) {if (a < d) {do{i = i;}while(a < b)} else {for(i = i; i < 100; i = i + 1){i = i;}}}");
            assertTrue(check(lexer.tokens, new Parser(LANG_RULES_DIR, ANALYZE_TABLE_DIR, terminals)));

            lexer.generateTokens("a = b; c = a; a = a;");
            assertTrue(check(lexer.tokens, new Parser(LANG_RULES_DIR, ANALYZE_TABLE_DIR, terminals)));
        } catch (TranslatorException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void converterTests() {
        try {
            Lexer lexer = new Lexer(LEXEMES_DIR);
            HashSet<String> terminals = new HashSet<>(lexer.lexemes.keySet());

            lexer.generateTokens(""); //main program
            assertEquals("$", //opn
                    getOPN(lexer, new Parser(LANG_RULES_DIR, ANALYZE_TABLE_DIR, terminals)));

            //TODO: Написать больше тестов
            //while tests
            lexer.generateTokens("while(a < b) {a = a + 1;}");
            assertEquals("%a,%b,<,!F@10,#a,%a,1,+,=,!@0,$",
                    getOPN(lexer, new Parser(LANG_RULES_DIR, ANALYZE_TABLE_DIR, terminals)));

            lexer.generateTokens("while(a < b & c > d) {}");
            assertEquals("%a,%b,<,%c,%d,>,&,!F@9,!@0,$",
                    getOPN(lexer, new Parser(LANG_RULES_DIR, ANALYZE_TABLE_DIR, terminals)));

            lexer.generateTokens("while(((a < b) & (c > d)) | ((a > c) & (b < d))) {}");
            assertEquals("%a,%b,<,%c,%d,>,&,%a,%c,>,%b,%d,<,&,|,!F@17,!@0,$",
                    getOPN(lexer, new Parser(LANG_RULES_DIR, ANALYZE_TABLE_DIR, terminals)));

            lexer.generateTokens("while((a < b) & (c > d) | (a > c) & (b < d)) {}");
            assertEquals("%a,%b,<,%c,%d,>,&,%a,%c,>,%b,%d,<,&,|,!F@17,!@0,$",
                    getOPN(lexer, new Parser(LANG_RULES_DIR, ANALYZE_TABLE_DIR, terminals)));

            lexer.generateTokens("while(a < b) {if (a < b) {}}");
            assertEquals("%a,%b,<,!F@10,%a,%b,<,!F@9,!@9,!@0,$",
                    getOPN(lexer, new Parser(LANG_RULES_DIR, ANALYZE_TABLE_DIR, terminals)));

            lexer.generateTokens("while(a < b) {do{} while(a < b)}");
            assertEquals("%a,%b,<,!F@9,%a,%b,<,!T@4,!@0,$",
                    getOPN(lexer, new Parser(LANG_RULES_DIR, ANALYZE_TABLE_DIR, terminals)));


            lexer.generateTokens("while(a < b) {for(i = 1; i < 100; i = i + 1) {}}");
            assertEquals("%a,%b,<,!F@18,#i,1,=,%i,100,<,!F@17,#i,%i,1,+,=,!@7,!@0,$",
                    getOPN(lexer, new Parser(LANG_RULES_DIR, ANALYZE_TABLE_DIR, terminals)));

            lexer.generateTokens("while(a < b) {new a typeof hashmap; new i = 1; put(a, i);}");
            assertEquals("%a,%b,<,!F@16,#a,new,#hashmap,typeof,#i,new,1,=,#a,#i,put,!@0,$",
                    getOPN(lexer, new Parser(LANG_RULES_DIR, ANALYZE_TABLE_DIR, terminals)));



            //for tests
            lexer.generateTokens("for(i = 1; (i < n) & (n > i); i = i + 1) {}");
            assertEquals("#i,1,=,%i,%n,<,%n,%i,>,&,!F@17,#i,%i,1,+,=,!@3,$",
                    getOPN(lexer, new Parser(LANG_RULES_DIR, ANALYZE_TABLE_DIR, terminals)));


        } catch (TranslatorException e) {
            e.printStackTrace();
            fail();
        }
    }

    private boolean check(ArrayList<Token> tokens, Parser parser) throws TranslatorException {
        for(Token token: tokens) {
            token.accept(parser);
        }

        return true;
    }

    private String getOPN(Lexer lexer, Parser parser) throws TranslatorException{
        for(Token token: lexer.tokens) {
            token.accept(parser);
        }

        return parser.getOPN();
    }
}
