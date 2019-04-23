package io.github.therealmone.jtrAPI.impl;

import io.github.therealmone.core.exceptions.TranslatorException;
import io.github.therealmone.core.interfaces.IException;
import io.github.therealmone.core.utils.SavePrinter;
import io.github.therealmone.jtrAPI.Interpreter;
import io.github.therealmone.jtrAPI.LexerModule;
import io.github.therealmone.jtrAPI.ParserModule;
import io.github.therealmone.jtrAPI.utils.RPNOptimizer;
import io.github.therealmone.stackmachine.StackMachine;
import io.github.therealmone.tdf4j.commons.Stream;
import io.github.therealmone.tdf4j.commons.Token;
import io.github.therealmone.tdf4j.generator.impl.ParserGenerator;
import io.github.therealmone.tdf4j.lexer.Lexer;
import io.github.therealmone.tdf4j.lexer.LexerFactory;
import io.github.therealmone.tdf4j.lexer.UnexpectedSymbolException;
import io.github.therealmone.tdf4j.parser.Parser;
import io.github.therealmone.tdf4j.parser.UnexpectedTokenException;
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
    private final ParserModule module;
    private final StackMachine stackMachine;
    private final RPNOptimizer optimizer;

    public InterpreterImpl(final LexerModule lexer, final ParserModule parser) {
        this.lexer = new LexerFactory().withModule(lexer);
        this.parser = new ParserGenerator().generate(parser);
        this.module = parser;
        this.stackMachine = StackMachine.getInstance();
        this.optimizer = new RPNOptimizer();
        log.debug("Parser : \n{}", this.parser.meta().sourceCode());
    }

    @Override
    public void process(final String program, final OutputStream output) {
        try {
            SavePrinter.setOut(output);
            log.debug("Program : \n{}", program);
            final StreamDecorator<Token> stream = new StreamDecorator<>(lexer.stream(program));
            final AST ast = parser.parse(stream);
            log.debug("AST : \n{}", ast);
            log.debug("RPN : \n{}", module.getRpn());
            stackMachine.visit(optimizer.optimize(module.getRpn()));
        } catch (TranslatorException | UnexpectedTokenException | UnexpectedSymbolException e) {
            if (e instanceof IException) {
                ((IException) e).message();
            } else {
                SavePrinter.savePrintln(e.getMessage());
            }
        }
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
