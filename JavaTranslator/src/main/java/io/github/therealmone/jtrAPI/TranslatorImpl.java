package io.github.therealmone.jtrAPI;

import io.github.therealmone.spoLexer.Lexer;
import io.github.therealmone.spoParser.Parser;
import io.github.therealmone.spoStackMachine.StackMachine;
import io.github.therealmone.translatorAPI.Exceptions.TranslatorException;
import io.github.therealmone.translatorAPI.Interfaces.ExceptionInterface;
import io.github.therealmone.translatorAPI.ResourceLoader;
import io.github.therealmone.translatorAPI.SavePrinter;
import io.github.therealmone.translatorAPI.Token;

import java.util.List;

class TranslatorImpl implements Translator {
    private boolean devMode = false;
    private Lexer lexer;
    private Parser parser;
    private StackMachine stackMachine;

    TranslatorImpl() {
        ResourceLoader.initialize();
    }

    @Override
    public void translate(String program) {
        if(devMode) {
            devModeTranslate(program);
        } else {
            defaultTranslate(program);
        }
    }

    private void defaultTranslate(String program) {
        try {
            lexer = Lexer.getInstance();
            lexer.generateTokens(program);

            parser = Parser.getInstance(lexer.getTerminals());
            for (Token token : lexer.getTokens()) {
                token.accept(parser);
            }

            stackMachine = StackMachine.getInstance();
            parser.accept(stackMachine);
        } catch (TranslatorException e) {
            printError(e);
        }
    }

    private void devModeTranslate(String program) {
        try {
            SavePrinter.savePrintln("\u001B[31mMAIN PROGRAM\u001B[0m");
            SavePrinter.savePrintln("-----------------------------------------------------------------------------------------");

            lexer = Lexer.getInstance();
            lexer.showLexemes();

            lexer.generateTokens(program);
            lexer.showTokens();

            parser = Parser.getInstance(lexer.getTerminals());
            parser.showLangRules();

            for (Token token : lexer.getTokens()) {
                token.accept(parser);
            }

            SavePrinter.savePrintln("\u001B[32mPARSE SUCCESS\u001B[0m");
            SavePrinter.savePrintln("RPN: " + listToString(parser.getRPN()));

            stackMachine = StackMachine.getInstance();
            parser.accept(stackMachine);

            SavePrinter.savePrintln("-----------------------------------------------------------------------------------------");
            SavePrinter.savePrintln("\u001B[31mMAIN PROGRAM DONE\u001B[0m");

            SavePrinter.savePrintln("\u001B[32mCALCULATE SUCCESS\u001B[0m");
            stackMachine.showVariables();

        } catch (TranslatorException e) {
            e.printStackTrace();
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
        if (e instanceof ExceptionInterface) {
            ((ExceptionInterface) e).message();
        } else {
            SavePrinter.savePrintln(e.getMessage());
        }
    }

    private String listToString(List<String> list) {
        StringBuilder out = new StringBuilder();
        for(String string : list) {
            out.append(string).append(",");
        }
        out.deleteCharAt(out.length() - 1);
        return out.toString();
    }
}
