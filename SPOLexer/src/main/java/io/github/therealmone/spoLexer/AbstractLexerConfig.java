package io.github.therealmone.spoLexer;

import io.github.therealmone.core.beans.Lexeme;
import io.github.therealmone.core.utils.SavePrinter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractLexerConfig {
    private Set<Lexeme> lexemes;

    public AbstractLexerConfig() {
        lexemes = new HashSet<>();
        configure();
        editConfig();
    }

    public abstract void configure();

    public void editConfig() {
        /*Override it*/
    }

    public void addLexeme(final String type, final String template, final int priority) {
        if(!containsLexeme(type)) {
            this.lexemes.add(new Lexeme(type, template, priority));
        } else {
            SavePrinter.savePrintln("[addLexeme] Already contains lexeme: " + type);
        }
    }

    public void removeLexeme(final String type) {
        for(Lexeme lexeme : lexemes) {
            if(lexeme.getType().equals(type)) {
                lexemes.remove(lexeme);
                return;
            }
        }

        SavePrinter.savePrintln("[removeLexeme] Lexeme " + type + " wasn't found");
    }

    public void editLexeme(final String type, final String template, final int priority) {
        for(Lexeme lexeme: lexemes) {
            if(lexeme.getType().equals(type)) {
                lexeme = new Lexeme(type, template, priority);
                return;
            }
        }

        SavePrinter.savePrintln("[editLexeme] Lexeme " + type + " wasn't found");
    }

    private boolean containsLexeme(final String type) {
        for(Lexeme lexeme : lexemes) {
            if(lexeme.getType().equals(type)) {
                return true;
            }
        }

        return false;
    }

    public Set<Lexeme> getLexemes() {
        return Collections.unmodifiableSet(lexemes);
    }
}
