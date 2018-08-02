package io.github.therealmone.examples;

import io.github.therealmone.jtrAPI.Translator;

import java.io.FileInputStream;
import java.io.IOException;

public class While_Example {
    public static void main(String[] args) throws IOException {
        Translator translator = Translator.create();

        translator.translate(
                Translator.loadProgram(new FileInputStream("examples/src/main/resources/while_example.txt"))
        );
    }
}
