package io.github.therealmone.examples;

import com.google.inject.AbstractModule;
import io.github.therealmone.jtrAPI.Interpreter;
import io.github.therealmone.jtrAPI.LexerModule;
import io.github.therealmone.jtrAPI.ParserModule;
import io.github.therealmone.jtrAPI.StackMachineModule;
import io.github.therealmone.jtrAPI.impl.InterpreterImpl;
import io.github.therealmone.jtrAPI.impl.LexerModuleImpl;
import io.github.therealmone.jtrAPI.impl.ParserModuleImpl;
import io.github.therealmone.jtrAPI.impl.StackMachineModuleImpl;

public class AppModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Interpreter.class).to(InterpreterImpl.class);
        bind(LexerModule.class).to(LexerModuleImpl.class);
        bind(ParserModule.class).to(ParserModuleImpl.class);
        bind(StackMachineModule.class).to(StackMachineModuleImpl.class);
    }
}
