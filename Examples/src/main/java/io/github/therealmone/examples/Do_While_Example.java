package io.github.therealmone.examples;

import io.github.therealmone.jtrAPI.Translator;

public class Do_While_Example {
    public static void main(String[] args) {
        Translator translator = Translator.create();

        translator.translate(
                Translator.loadProgram(Thread.currentThread().getContextClassLoader().getResourceAsStream("do_while_example.txt"))
        );
    }
}
