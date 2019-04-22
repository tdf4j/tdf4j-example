package io.github.therealmone.jtrAPI.impl;

import io.github.therealmone.jtrAPI.Interpreter;
import io.github.therealmone.tdf4j.commons.Stream;
import io.github.therealmone.tdf4j.commons.Token;
import io.github.therealmone.tdf4j.lexer.Lexer;
import io.github.therealmone.tdf4j.parser.Parser;
import io.github.therealmone.tdf4j.parser.model.ast.AST;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.OutputStream;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class InterpreterImpl implements Interpreter {
    private final static Logger log = LogManager.getLogger(InterpreterImpl.class);
    private final Lexer lexer;
    private final Parser parser;

    public InterpreterImpl(final Lexer lexer, final Parser parser) {
        this.lexer = lexer;
        this.parser = parser;
        log.debug("Parser : \n{}", parser.meta().sourceCode());
    }

    @Override
    public void process(final String program, final OutputStream output) {
        log.debug("Program : \n{}", program);
        final StreamDecorator<Token> stream = new StreamDecorator<>(lexer.stream(program));
        final AST ast = parser.parse(stream);
        log.debug("AST : \n{}", ast);
    }

    private class StreamDecorator<T> implements Stream<T> {
        private final Stream<T> stream;

        StreamDecorator(final Stream<T> stream) {
            this.stream = stream;
        }

        @Override
        public Supplier<T> generator() {
            return stream.generator();
        }

        @Nullable
        @Override
        public T next() {
            final T next = stream.next();
            log.debug("Next token : {}", next);
            return next;
        }

        @Override
        public void forEach(Consumer<? super T> action) {
            T value;
            while((value = this.next()) != null) {
                action.accept(value);
            }
        }
    }
}
