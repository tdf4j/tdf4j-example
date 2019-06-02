package io.github.therealmone.examples;

import com.google.inject.AbstractModule;
import io.github.therealmone.jtrAPI.Interpreter;
import io.github.therealmone.jtrAPI.impl.InterpreterImpl;

import java.io.InputStream;

public class AppModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Interpreter.class).to(InterpreterImpl.class);
        bind(InputStream.class).toInstance(Thread.currentThread().getContextClassLoader().getResourceAsStream("grammar.tdf"));
    }
}
