package io.github.therealmone.examples;

import io.github.therealmone.jtrAPI.Interpreter;
import io.github.therealmone.jtrAPI.InterpreterFactory;

public class First_100_Fibonacci_Numbers_example {
    public static void main(String[] args) {
        final Interpreter interpreter = new InterpreterFactory().create();

        interpreter.process(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("first_100_Fibonacci_numbers.txt"),
                System.out
        );
    }
}
