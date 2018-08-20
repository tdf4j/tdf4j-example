package io.github.therealmone.examples;

import io.github.therealmone.jtrAPI.Translator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class DevProg {
    public static void main(String[] args) {
        Runnable run1 = () -> {
            try {
                Translator translator = Translator.getInstance();
                Translator.setOutputStream(new FileOutputStream(new File("out1.txt")));
                translator.translate("new i = 0; for (i = 0; i < 10000000; i = i + 1) {print(\"Thread1: \" ++ i);}");
            } catch (FileNotFoundException e) {}
        };

        Runnable run2 = () -> {
            try {
                Translator translator = Translator.getInstance();
                Translator.setOutputStream(new FileOutputStream(new File("out2.txt")));
                translator.translate("new i = 0; for (i = 0; i < 10000000; i = i + 1) {print(\"Thread2: \" ++ i);}");
            } catch (FileNotFoundException e) {}
        };

        Thread thread1 = new Thread(run1);
        Thread thread2 = new Thread(run2);
        thread1.start();
        thread2.start();
    }
}
