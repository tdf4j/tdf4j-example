package io.github.therealmone.spoLexer;

import io.github.therealmone.core.beans.Lexeme;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractLexerConfig {
    private Set<Lexeme> lexemes;

    public AbstractLexerConfig() {
        lexemes = new HashSet<>();
        configure();
    }

    public abstract void configure();

    void addLexeme(String type, String template, int priority) {
        this.lexemes.add(new Lexeme(type, template, priority));
    }

    Set<Lexeme> getLexemes() {
        return Collections.unmodifiableSet(lexemes);
    }
}
