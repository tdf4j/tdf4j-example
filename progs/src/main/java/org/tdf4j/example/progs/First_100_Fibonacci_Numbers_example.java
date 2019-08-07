package org.tdf4j.example.progs;

import com.google.inject.Guice;
import org.tdf4j.example.jtrAPI.Interpreter;

public class First_100_Fibonacci_Numbers_example {
    public static void main(String[] args) {
        final Interpreter interpreter = Guice.createInjector(new AppModule()).getInstance(Interpreter.class);

        interpreter.process(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("first_100_Fibonacci_numbers.txt"),
                System.out
        );
    }
}
