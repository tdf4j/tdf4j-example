package org.tdf4j.example.progs;

import com.google.inject.AbstractModule;
import org.tdf4j.example.jtrAPI.Interpreter;
import org.tdf4j.example.jtrAPI.impl.InterpreterImpl;

import java.io.InputStream;

public class AppModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Interpreter.class).to(InterpreterImpl.class);
        bind(InputStream.class).toInstance(Thread.currentThread().getContextClassLoader().getResourceAsStream("grammar.tdf"));
    }
}
