package org.tdf4j.example.progs;

import com.google.inject.Guice;
import org.tdf4j.example.jtrAPI.Interpreter;

public class Do_While_Example {
    public static void main(String[] args) {
        final Interpreter interpreter = Guice.createInjector(new AppModule()).getInstance(Interpreter.class);

        interpreter.process(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("do_while_example.txt"),
                System.out
        );
    }
}
