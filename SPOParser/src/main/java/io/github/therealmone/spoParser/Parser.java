package io.github.therealmone.spoParser;

import io.github.therealmone.translatorAPI.Beans.Lexeme;
import io.github.therealmone.translatorAPI.Interfaces.Visitable;
import io.github.therealmone.translatorAPI.Interfaces.Visitor;

import java.util.List;
import java.util.Set;

public interface Parser extends Visitor, Visitable {
    void showLangRules();

    List<String> getRPN();

    static Parser getInstance(Set<Lexeme> terminals) {
        return new ParserImpl(terminals);
    }
}
