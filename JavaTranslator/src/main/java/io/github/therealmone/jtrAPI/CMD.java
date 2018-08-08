package io.github.therealmone.jtrAPI;

import java.io.FileInputStream;
import java.io.IOException;

public class CMD {
    public static void main(String[] args) throws IOException {
        Translator translator = Translator.getInstance();
        translator.translate(
                Translator.loadProgram(new FileInputStream(args[0]))
        );
    }
}
