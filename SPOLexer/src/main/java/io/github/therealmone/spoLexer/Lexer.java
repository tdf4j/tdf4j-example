package io.github.therealmone.spoLexer;

import io.github.therealmone.translatorAPI.Token;

import java.util.List;
import java.util.Set;

public interface Lexer {
    void generateTokens(String program);

    List<Token> getTokens();

    void showTokens();

    Set<String> getTerminals();

    void showLexemes();

    static Lexer getInstance() {
        return new LexerImpl();
    }
}
