package io.github.therealmone.examples;

import io.github.therealmone.jtrAPI.Translator;

public class HashSet_Example {
    public static void main(String[] args) {
        Translator translator = Translator.getInstance();

        translator.translate(
                Translator.loadProgram(Thread.currentThread().getContextClassLoader().getResourceAsStream("hashset_example.txt"))
        );
    }
}
