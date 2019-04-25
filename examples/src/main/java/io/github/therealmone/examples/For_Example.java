package io.github.therealmone.examples;

import com.google.inject.Guice;
import io.github.therealmone.jtrAPI.Interpreter;

public class For_Example {
    public static void main(String[] args) {
        final Interpreter interpreter = Guice.createInjector(new AppModule()).getInstance(Interpreter.class);

        interpreter.process(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("for_example.txt"),
                System.out
        );
    }
}
