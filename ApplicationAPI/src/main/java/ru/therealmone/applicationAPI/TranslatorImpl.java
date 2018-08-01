package ru.therealmone.applicationAPI;

import ru.therealmone.spoLexer.*;
import ru.therealmone.spoParser.Parser;
import ru.therealmone.spoStackMachine.StackMachine;
import ru.therealmone.translatorAPI.Exceptions.TranslatorException;
import ru.therealmone.translatorAPI.Interfaces.ExceptionInterface;
import ru.therealmone.translatorAPI.ResourceLoader;
import ru.therealmone.translatorAPI.SavePrinter;
import ru.therealmone.translatorAPI.Token;

import java.util.HashSet;

class TranslatorImpl implements Translator {
    private boolean devMode = false;

    @Override
    public void translate(String program) {
        try {
            ResourceLoader.initialize();

            Lexer lexer;
            Parser parser;
            StackMachine stackMachine;

            SavePrinter.savePrintln("\u001B[31mMAIN PROGRAM\u001B[0m");
            SavePrinter.savePrintln("-----------------------------------------------------------------------------------------");

            lexer = new Lexer();
            if(devMode) {
                lexer.showLexemes();
            }

            lexer.generateTokens(program);
            if(devMode) {
                lexer.showTokens();
            }

            parser = new Parser(new HashSet<>(lexer.lexemes.keySet()));
            if(devMode) {
                parser.showLangRules();
            }

            for (Token token : lexer.tokens) {
                token.accept(parser);
            }

            if(devMode) {
                SavePrinter.savePrintln("\u001B[32mPARSE SUCCESS\u001B[0m");
                SavePrinter.savePrintln("OPN: " + parser.getOPN());
            }

            stackMachine = new StackMachine();
            parser.accept(stackMachine);

            SavePrinter.savePrintln("-----------------------------------------------------------------------------------------");
            SavePrinter.savePrintln("\u001B[31mMAIN PROGRAM DONE\u001B[0m");

            if(devMode) {
                SavePrinter.savePrintln("\u001B[32mCALCULATE SUCCESS\u001B[0m");
                stackMachine.showVariables();
            }

        } catch (TranslatorException e) {
            if(devMode) {
                e.printStackTrace();
            }
            printError(e);
            SavePrinter.savePrintln("-----------------------------------------------------------------------------------------");
            SavePrinter.savePrintln("\u001B[31mMAIN PROGRAM FAILED\u001B[0m");
        }
    }

    @Override
    public void setDevMode(Boolean devMode) {
        this.devMode = devMode;
    }

    private void printError(TranslatorException e) {
        if(e instanceof ExceptionInterface) {
            ((ExceptionInterface) e).message();
        } else {
            SavePrinter.savePrintln(e.getMessage());
        }
    }
}
