package io.github.therealmone.jtrAPI;

import io.github.therealmone.jtrAPI.impl.InterpreterImpl;

public class InterpreterFactory {
    public Interpreter create() {
        return create(new LexerModule(), new ParserModule());
    }

    public Interpreter create(final LexerModule lexer) {
        return create(lexer, new ParserModule());
    }

    public Interpreter create(final ParserModule parser) {
        return create(new LexerModule(), parser);
    }

    public Interpreter create(final LexerModule lexer, final ParserModule parser) {
        return new InterpreterImpl(lexer, parser);
    }
}
