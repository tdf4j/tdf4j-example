package ru.therealmone.SPOLexer;

import org.junit.Test;
import static org.junit.Assert.*;
import ru.therealmone.SPOParser.Parser;
import ru.therealmone.TranslatorAPI.Token;
import ru.therealmone.TranslatorAPI.UnexpectedSymbolException;

import java.util.ArrayList;
import java.util.HashSet;

public class ParserTest {
    private static final String LEXEMES_DIR = "D:/JavaProjects/SPOTranslator/SPOLexer/src/main/resources/lexemes.xml";
    private static final String LANG_RULES_DIR = "D:/JavaProjects/SPOTranslator/SPOParser/src/main/resources/langRules.csv";
    private static final String ANALYZE_TABLE_DIR = "D:/JavaProjects/SPOTranslator/SPOParser/src/main/resources/analyzeTable.csv";

    @Test
    public void parserTests() {
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
    }

    @Test
    public void OPNTests() {
        //TODO: Протестировать обратную польскую нотацию
    }

    private boolean check(ArrayList<Token> tokens, Parser parser) {
        for(Token token: tokens) {
            token.accept(parser);
        }
        return true;
    }
}
