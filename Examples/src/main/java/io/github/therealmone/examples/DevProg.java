package io.github.therealmone.examples;

import io.github.therealmone.jtrAPI.Translator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class DevProg {
    public static void main(String[] args) throws IOException {
        Translator translator = Translator.create();
        Translator.setOutputStream(new FileOutputStream(new File("out.txt")));
        translator.setDevMode(true);

        translator.translate(
                Translator.loadProgram(new FileInputStream("examples/src/main/resources/dev_program.txt"))
        );
    }
}
