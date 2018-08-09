package io.github.therealmone.examples;

import io.github.therealmone.jtrAPI.Translator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DevProg {
    public static void main(String[] args) throws IOException  {
        Runnable run1 = () -> {
            Translator translator = Translator.getInstance();
            translator.setDevMode(true);
            try {
                Translator.setOutputStream(new FileOutputStream(new File("out1.txt")));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            translator.translate("new i = 0; for(i = 0; i < 10000000; i = i + 1) {print(\"run1 \" ++ i);}");
        };

        Runnable run2 = () -> {
            Translator translator = Translator.getInstance();
            translator.setDevMode(true);
            try {
                Translator.setOutputStream(new FileOutputStream(new File("out2.txt")));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            translator.translate("new i = 0; for(i = 0; i < 10000000; i = i + 1) {print(\"run2 \" ++ i);}");
        };

        Thread thread1 = new Thread(run1);
        Thread thread2 = new Thread(run2);
        thread1.start();
        //thread2.start();
    }
}
