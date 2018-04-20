package ru.therealmone.SPOLexer;

class Token {
    private String type;
    private String value;

    Token(String type, String value) {
        this.type = type;
        this.value = value;
    }

    String getType() {
        return(this.type);
    }

    String getValue() {
        return(this.value);
    }
}
