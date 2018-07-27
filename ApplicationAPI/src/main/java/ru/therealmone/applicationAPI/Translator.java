package ru.therealmone.applicationAPI;

import java.io.*;

public interface Translator {
    void translate(String program);

    static String loadProgram(InputStream inputStream) throws IOException {
        StringBuilder out = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
        }

        return out.toString();
    }

    static Translator create() {
        return new TranslatorImpl();
    }
}
