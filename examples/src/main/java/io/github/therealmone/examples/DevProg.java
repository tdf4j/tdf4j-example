package io.github.therealmone.examples;

import com.google.inject.Guice;
import io.github.therealmone.jtrAPI.Interpreter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class DevProg {
    public static void main(String[] args) {
        Runnable run1 = () -> {
            try {
                final Interpreter interpreter = Guice.createInjector(new AppModule()).getInstance(Interpreter.class);
                interpreter.process(
                        "new i = 0; for (i = 0; i < 10000000; i = i + 1) {print(\"Thread1: \" ++ i);}$",
                        new FileOutputStream(new File("out1.txt"))
                );
            } catch (FileNotFoundException e) {
                //ignore
            }
        };

        Runnable run2 = () -> {
            try {
                final Interpreter interpreter = Guice.createInjector(new AppModule()).getInstance(Interpreter.class);
                interpreter.process(
                        "new i = 0; for (i = 0; i < 10000000; i = i + 1) {print(\"Thread2: \" ++ i);}$",
                        new FileOutputStream(new File("out2.txt"))
                );
            } catch (FileNotFoundException e) {
                //ignore
            }
        };

        Thread thread1 = new Thread(run1);
        Thread thread2 = new Thread(run2);
        thread1.start();
        thread2.start();
    }
}
