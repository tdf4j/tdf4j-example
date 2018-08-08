package io.github.therealmone.examples;

import io.github.therealmone.jtrAPI.Translator;

public class DevProg {
    public static void main(String[] args) {
        Translator translator = Translator.create();
        translator.setDevMode(true);

        translator.translate(
                Translator.loadProgram(Thread.currentThread().getContextClassLoader().getResourceAsStream("dev_program.txt"))
        );
    }
}
