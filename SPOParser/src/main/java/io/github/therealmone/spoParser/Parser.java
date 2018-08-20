package io.github.therealmone.spoParser;

import io.github.therealmone.core.beans.Lexeme;
import io.github.therealmone.core.interfaces.Visitable;
import io.github.therealmone.core.interfaces.Visitor;

import java.util.List;
import java.util.Set;

public interface Parser extends Visitor, Visitable {
    void showLangRules();

    List<String> getRPN();

    static Parser getDefault(Set<Lexeme> terminals) {
        return new ParserImpl(terminals);
    }

    static Parser getConfigurable(Set<Lexeme> terminals, AbstractParserConfig config) {
        return new ParserImpl(terminals, config);
    }
}
