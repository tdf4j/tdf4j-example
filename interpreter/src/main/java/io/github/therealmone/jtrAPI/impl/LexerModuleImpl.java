package io.github.therealmone.jtrAPI.impl;

import io.github.therealmone.jtrAPI.LexerModule;

public class LexerModuleImpl extends LexerModule {
    @Override
    public void configure() {
        tokenize("VAR").pattern("[a-z]+").priority(1);
        tokenize("STRING").pattern("\"[^\"]*\"").priority(1);
        tokenize("NEW").pattern("new").priority(2);
        tokenize("TYPEOF").pattern("typeof").priority(2);
        tokenize("HASHSET").pattern("hashset").priority(2);
        tokenize("ARRAYLIST").pattern("arraylist").priority(2);
        tokenize("GET").pattern("get").priority(2);
        tokenize("SIZE").pattern("size").priority(2);
        tokenize("PUT").pattern("put").priority(2);
        tokenize("REMOVE").pattern("remove").priority(2);
        tokenize("REWRITE").pattern("rewrite").priority(2);
        tokenize("PRINT").pattern("print").priority(2);
        tokenize("COMMA").pattern(",").priority(1);
        tokenize("CONCAT").pattern("\\+\\+").priority(3);
        tokenize("QUOTE").pattern("\"").priority(1);
        tokenize("DIGIT").pattern("-?(0|([1-9][0-9]*))").priority(2);
        tokenize("DOUBLE").pattern("-?((0[.][0-9]*)|([1-9][0-9]*[.][0-9]*))").priority(1);
        tokenize("ASSIGN_OP").pattern("=").priority(1);
        tokenize("OP").pattern("[\\+\\-\\/\\*]|(div)|(mod)").priority(2);
        tokenize("DEL").pattern(";").priority(1);
        tokenize("WHILE").pattern("while").priority(2);
        tokenize("IF").pattern("if").priority(2);
        tokenize("ELSE").pattern("else").priority(2);
        tokenize("DO").pattern("do").priority(2);
        tokenize("FOR").pattern("for").priority(2);
        tokenize("LOP").pattern("[&\\|\\^\\!]").priority(2);
        tokenize("COP").pattern("[<>]|(<=)|(>=)|(==)|(!=)").priority(2);
        tokenize("LB").pattern("\\(").priority(1);
        tokenize("RB").pattern("\\)").priority(1);
        tokenize("FLB").pattern("\\{").priority(1);
        tokenize("FRB").pattern("\\}").priority(1);
        tokenize("$").pattern("\\$").priority(1);
        tokenize("ws").pattern("\\s|\\n|\\r").priority(Integer.MAX_VALUE).hidden(true);
        tokenize("one_line_comment").pattern("//.*(\n|\r|\r\n|\n\r)").priority(Integer.MAX_VALUE).hidden(true);
        tokenize("multi_line_comment").pattern("/\\*[^(\\*/)]*\\*/").priority(Integer.MAX_VALUE).hidden(true);
    }
}
