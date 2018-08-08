package io.github.therealmone.examples;

import io.github.therealmone.jtrAPI.Translator;

public class ArrayList_Example {
    public static void main(String[] args) {
        Translator translator = Translator.create();

        translator.translate(
                Translator.loadProgram(Thread.currentThread().getContextClassLoader().getResourceAsStream("arraylist_example.txt"))
        );
    }
}
