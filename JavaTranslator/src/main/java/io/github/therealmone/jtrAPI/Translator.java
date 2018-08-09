package io.github.therealmone.jtrAPI;

import io.github.therealmone.translatorAPI.Exceptions.TranslatorException;
import io.github.therealmone.translatorAPI.SavePrinter;

import java.io.*;

public interface Translator {
    void translate(String program);

    void setDevMode(Boolean devMode);

    static String loadProgram(InputStream inputStream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder out = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }

            return out.toString();
        } catch (IOException e) {
            throw new TranslatorException("Can't load program.", e);
        }
    }

    static Translator getInstance() {
        return new TranslatorImpl();
    }

    static void setOutputStream(OutputStream out) {
       SavePrinter.setOut(new PrintStream(out));
    }

}
