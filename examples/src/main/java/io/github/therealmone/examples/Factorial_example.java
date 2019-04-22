package io.github.therealmone.examples;

import io.github.therealmone.jtrAPI.Interpreter;
import io.github.therealmone.jtrAPI.InterpreterFactory;

public class Factorial_example {
    public static void main(String[] args) {
        final Interpreter interpreter = new InterpreterFactory().create();

        interpreter.process(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("factorial_example.txt"),
                System.out
        );
    }
}
