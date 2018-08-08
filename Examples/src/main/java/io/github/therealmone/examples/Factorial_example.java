package io.github.therealmone.examples;

import io.github.therealmone.jtrAPI.Translator;

public class Factorial_example {
    public static void main(String[] args) {
        Translator translator = Translator.getInstance();

        translator.translate(
                Translator.loadProgram(Thread.currentThread().getContextClassLoader().getResourceAsStream("factorial_example.txt"))
        );
    }
}
