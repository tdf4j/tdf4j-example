package io.github.therealmone.spoLexer;

import io.github.therealmone.translatorAPI.Beans.Lexeme;
import io.github.therealmone.translatorAPI.ResourceLoader;
import io.github.therealmone.translatorAPI.SavePrinter;
import io.github.therealmone.translatorAPI.Beans.Token;
import io.github.therealmone.spoLexer.exceptions.UnexpectedSymbolException;

import java.util.*;
import java.util.regex.*;

class LexerImpl implements Lexer {
    private List<Token> tokens;
    private final Set<Lexeme> lexemes;

    private static final char END_SYMBOL = '$';

    LexerImpl() {
        tokens = new ArrayList<>();
        lexemes = ResourceLoader.getLexemes();

        Lexeme endLexeme = new Lexeme();
        endLexeme.setType(String.valueOf(END_SYMBOL));
        endLexeme.setPattern(Pattern.compile("^\\" + END_SYMBOL + "$"));
        endLexeme.setPriority(0);

        lexemes.add(endLexeme);
    }

    @Override
    public void showLexemes() {
        SavePrinter.savePrintln("\u001B[33mLEXEMES:\u001B[0m");
        lexemes.forEach(lexeme -> SavePrinter.savePrintf("%-20s%-10s%-40s%n", lexeme.getType(), "-->", lexeme.getPattern().pattern()));
    }

    @Override
    public void generateTokens(String input) {
        input = input.replaceAll("\\" + END_SYMBOL, "");
        input += END_SYMBOL;

        tokens.clear();
        StringBuilder tempString = new StringBuilder();

        while (input.charAt(0) != END_SYMBOL) {
            input = input.trim();
            tempString.append(input.charAt(tempString.length()));

            if (!checkLexemes(tempString.toString())) {
                if (tempString.length() > 1) {
                    tempString.deleteCharAt(tempString.length() - 1);
                    tokens.add(new Token(chooseLexeme(tempString.toString()), tempString.toString()));
                    input = input.substring(tempString.length());
                    tempString.delete(0, tempString.length());
                } else {
                    throw new UnexpectedSymbolException(input, tokens);
                }
            }
        }

        tokens.add(new Token("$", "$"));
    }

    private boolean checkLexemes(final String str) {
        for(Lexeme lexeme : lexemes) {
            if(lexeme.getPattern().matcher(str).matches()) {
                return true;
            }
        }

        return false;
    }

    private String chooseLexeme(final String str) {
        int tmpPriority = 0;
        String lexType = "";

        for(Lexeme lexeme : lexemes) {
            if(lexeme.getPattern().matcher(str).matches()) {
                if(lexeme.getPriority() > tmpPriority) {
                    lexType = lexeme.getType();
                    tmpPriority = lexeme.getPriority();
                }
            }
        }

        return lexType;
    }

    @Override
    public void showTokens() {
        SavePrinter.savePrintln("\u001B[33mGENERATED TOKENS:\u001B[0m");
        tokens.forEach(token -> SavePrinter.savePrintf("%-20s%-10s%-40s%n", token.getType(), "-->", token.getValue()));
    }

    @Override
    public Set<Lexeme> getTerminals() {
        return Collections.unmodifiableSet(lexemes);
    }

    @Override
    public List<Token> getTokens() {
        return Collections.unmodifiableList(tokens);
    }
}
