package io.github.therealmone.jtrAPI;

import io.github.therealmone.jtrAPI.impl.InterpreterImpl;
import io.github.therealmone.tdf4j.generator.impl.ParserGenerator;
import io.github.therealmone.tdf4j.lexer.Lexer;
import io.github.therealmone.tdf4j.lexer.LexerFactory;
import io.github.therealmone.tdf4j.parser.Parser;

public class InterpreterFactory {
    public Interpreter create() {
        return create(new LexerFactory().withModule(new LexerModule()), new ParserGenerator().generate(new ParserModule()));
    }

    public Interpreter create(final Lexer lexer) {
        return create(lexer, new ParserGenerator().generate(new ParserModule()));
    }

    public Interpreter create(final Parser parser) {
        return create(new LexerFactory().withModule(new LexerModule()), parser);
    }

    public Interpreter create(final Lexer lexer, final Parser parser) {
        return new InterpreterImpl(lexer, parser);
    }
}
