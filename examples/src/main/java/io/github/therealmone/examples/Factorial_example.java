package io.github.therealmone.examples;

import com.google.inject.Guice;
import io.github.therealmone.jtrAPI.Interpreter;

public class Factorial_example {
    public static void main(String[] args) {
        final Interpreter interpreter = Guice.createInjector(new AppModule()).getInstance(Interpreter.class);

        interpreter.process(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("factorial_example.txt"),
                System.out
        );
    }
}
