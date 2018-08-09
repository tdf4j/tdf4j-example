package io.github.therealmone.spoLexer;

import io.github.therealmone.translatorAPI.Beans.Lexeme;
import io.github.therealmone.translatorAPI.Beans.Token;

import java.util.List;
import java.util.Set;

public interface Lexer {
    void generateTokens(String program);

    List<Token> getTokens();

    void showTokens();

    Set<Lexeme> getTerminals();

    void showLexemes();

    static Lexer getInstance() {
        return new LexerImpl();
    }
}
