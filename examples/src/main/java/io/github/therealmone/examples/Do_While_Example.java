package io.github.therealmone.examples;

import io.github.therealmone.jtrAPI.Interpreter;
import io.github.therealmone.jtrAPI.InterpreterFactory;

public class Do_While_Example {
    public static void main(String[] args) {
        final Interpreter interpreter = new InterpreterFactory().create();

        interpreter.process(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("do_while_example.txt"),
                System.out
        );
    }
}
