package io.github.therealmone.examples;

import io.github.therealmone.jtrAPI.Translator;

public class First_100_Fibonacci_Numbers_example {
    public static void main(String[] args) {
        Translator translator = Translator.create();

        translator.translate(
                Translator.loadProgram(Thread.currentThread().getContextClassLoader().getResourceAsStream("first_100_Fibonacci_numbers.txt"))
        );
    }
}
