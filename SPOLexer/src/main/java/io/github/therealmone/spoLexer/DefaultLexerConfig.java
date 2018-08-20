package io.github.therealmone.spoLexer;

class DefaultLexerConfig extends AbstractLexerConfig {
    @Override
    public void configure() {
        addLexeme("VAR", "^[a-z]+$", 1);
        addLexeme("STRING", "^\"(.*?)\"$", 1);
        addLexeme("HALFSTRING", "^\"[^\"]*", 0);
        addLexeme("NEW", "^new$", 2);
        addLexeme("TYPEOF", "^typeof$", 2);
        addLexeme("HASHSET", "^hashset$", 2);
        addLexeme("ARRAYLIST", "^arraylist$", 2);
        addLexeme("GET", "^get$", 2);
        addLexeme("SIZE", "^size$", 2);
        addLexeme("PUT", "^put$", 2);
        addLexeme("REMOVE", "^remove$", 2);
        addLexeme("REWRITE", "^rewrite$", 2);
        addLexeme("PRINT", "^print$", 2);
        addLexeme("COMMA", "^,$", 1);
        addLexeme("CONCAT", "^\\+\\+$", 3);
        addLexeme("QUOTE", "^\"$", 1);
        addLexeme("DIGIT", "^-?(0|([1-9][0-9]*))$", 2);
        addLexeme("DOUBLE", "^-?((0[.][0-9]*)|([1-9][0-9]*[.][0-9]*))$", 1);
        addLexeme("ASSIGN_OP", "^=$", 1);
        addLexeme("OP", "^[\\+\\-\\/\\*]|(div)|(mod)$", 2);
        addLexeme("DEL", "^;$", 1);
        addLexeme("WHILE", "^while$", 2);
        addLexeme("IF", "^if$", 2);
        addLexeme("ELSE", "^else$", 2);
        addLexeme("DO", "^do$", 2);
        addLexeme("FOR", "^for$", 2);
        addLexeme("LOP", "^[&\\|\\^\\!]$", 2);
        addLexeme("COP", "^[<>]|(<=)|(>=)|(==)|(!=)$", 2);
        addLexeme("LB", "^\\($", 1);
        addLexeme("RB", "^\\)$", 1);
        addLexeme("FLB", "^\\{$", 1);
        addLexeme("FRB", "^\\}$", 1);
        addLexeme("$", "^\\$$", 1);
    }
}
