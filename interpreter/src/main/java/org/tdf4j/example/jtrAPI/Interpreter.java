package org.tdf4j.example.jtrAPI;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;

public interface Interpreter {
    void process(final String program, final OutputStream output);

    default void process(final InputStream input, final OutputStream output) {
        try {
            final StringWriter writer = new StringWriter();
            int bt;
            while((bt = input.read()) != -1) {
                writer.write(bt);
            }
            process(writer.toString(), output);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
