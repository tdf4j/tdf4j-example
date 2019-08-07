package org.tdf4j.example.progs;

import com.google.inject.Guice;
import org.tdf4j.example.jtrAPI.Interpreter;

public class If_Else_Example {
    public static void main(String[] args) {
        final Interpreter interpreter = Guice.createInjector(new AppModule()).getInstance(Interpreter.class);

        interpreter.process(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("if_else_example.txt"),
                System.out
        );
    }
}
