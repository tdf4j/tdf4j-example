package ru.therealmone.examples;

import io.github.therealmone.applicationAPI.Translator;

import java.io.FileInputStream;
import java.io.IOException;

public class Do_While_Example {
    public static void main(String[] args) throws IOException {
        Translator translator = Translator.create();

        translator.translate(
                Translator.loadProgram(new FileInputStream("examples/src/main/resources/do_while_example.txt"))
        );
    }
}
