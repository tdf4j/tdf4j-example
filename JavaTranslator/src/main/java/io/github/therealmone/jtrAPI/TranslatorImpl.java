package io.github.therealmone.jtrAPI;

import io.github.therealmone.spoLexer.Lexer;
import io.github.therealmone.spoParser.Parser;
import io.github.therealmone.spoStackMachine.StackMachine;
import io.github.therealmone.translatorAPI.Exceptions.TranslatorException;
import io.github.therealmone.translatorAPI.Interfaces.ExceptionInterface;
import io.github.therealmone.translatorAPI.ResourceLoader;
import io.github.therealmone.translatorAPI.SavePrinter;
import io.github.therealmone.translatorAPI.Token;

import java.util.HashSet;
import java.util.List;

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
            if (devMode) {
                lexer.showLexemes();
            }

            lexer.generateTokens(program);
            if (devMode) {
                lexer.showTokens();
            }

            parser = new Parser(new HashSet<>(lexer.lexemes.keySet()));
            if (devMode) {
                parser.showLangRules();
            }

            for (Token token : lexer.tokens) {
                token.accept(parser);
            }

            if (devMode) {
                SavePrinter.savePrintln("\u001B[32mPARSE SUCCESS\u001B[0m");
                SavePrinter.savePrintln("RPN: " + listToString(parser.getRPN()));
            }

            stackMachine = new StackMachine();
            parser.accept(stackMachine);

            SavePrinter.savePrintln("-----------------------------------------------------------------------------------------");
            SavePrinter.savePrintln("\u001B[31mMAIN PROGRAM DONE\u001B[0m");

            if (devMode) {
                SavePrinter.savePrintln("\u001B[32mCALCULATE SUCCESS\u001B[0m");
                stackMachine.showVariables();
            }

        } catch (TranslatorException e) {
            if (devMode) {
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
