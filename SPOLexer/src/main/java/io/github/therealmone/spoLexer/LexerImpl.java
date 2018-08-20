package io.github.therealmone.spoLexer;

import io.github.therealmone.core.beans.Lexeme;
import io.github.therealmone.core.utils.SavePrinter;
import io.github.therealmone.core.beans.Token;
import io.github.therealmone.spoLexer.exceptions.UnexpectedSymbolException;

import java.util.*;

class LexerImpl implements Lexer {
    private List<Token> tokens;
    private final AbstractLexerConfig config;

    private static final char END_SYMBOL = '$';

    LexerImpl() {
        this.tokens = new ArrayList<>();
        this.config = new DefaultLexerConfig();
    }

    LexerImpl(AbstractLexerConfig config) {
        this.tokens = new ArrayList<>();
        this.config = config;
    }

    @Override
    public void showLexemes() {
        SavePrinter.savePrintln("\u001B[33mLEXEMES:\u001B[0m");
        config.getLexemes().forEach(lexeme -> SavePrinter.savePrintf("%-20s%-10s%-40s%n", lexeme.getType(), "-->", lexeme.getPattern().pattern()));
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
        for(Lexeme lexeme : config.getLexemes()) {
            if(lexeme.getPattern().matcher(str).matches()) {
                return true;
            }
        }

        return false;
    }

    private String chooseLexeme(final String str) {
        int tmpPriority = 0;
        String lexType = "";

        for(Lexeme lexeme : config.getLexemes()) {
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
        return Collections.unmodifiableSet(config.getLexemes());
    }

    @Override
    public List<Token> getTokens() {
        return Collections.unmodifiableList(tokens);
    }
}
