package io.github.therealmone.jtrAPI;

import io.github.therealmone.core.exceptions.TranslatorException;
import io.github.therealmone.core.utils.SavePrinter;

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


//TODO: 1) Конфигурация лексем (Добавить флаг на терминал) - Базу конфигурации храним в SPOLexer, чтобы была возможность использовать и конфигурировать лексер отдельно от всего
//TODO: 2) Конфигурация грамматики - Базу храним в SPOParser. Та же история, как и с лексемами
//TODO: 3) Конфигурация правил раскрытия нетерменалов - Базу храним в SPOParser
//TODO: 4) Конфигурация команд для стек машины (before(), execute(), after()) - Базу храним в SPOStackMachine
//TODO: 5) Продумать конфигурирование RPNConverter'а (Идея: "lang/expr/while_loop" = func()) - Базу храним в SPOParser

/*Все должно конфигурироваться через наследование от базовых классов конфигурации
* Таким образом избавляемся от всех ресурсов и делаем систему более гибкой
* В этом случае пользователь сможет сам настраивать транслятор*/