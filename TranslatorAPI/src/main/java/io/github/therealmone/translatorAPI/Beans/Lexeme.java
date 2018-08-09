package io.github.therealmone.translatorAPI.Beans;

import java.util.regex.Pattern;

public class Lexeme {
    private String type;
    private Pattern pattern;
    private Integer priority;

    public Lexeme() {
        this.type = null;
        this.pattern = null;
        this.priority = null;
    }

    public String getType() {
        return type;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public int getPriority() {
        return priority;
    }

    public void setType(String type) {
        if(this.type == null) {
            this.type = type;
        }
    }

    public void setPattern(Pattern pattern) {
        if(this.pattern == null) {
            this.pattern = pattern;
        }
    }

    public void setPriority(Integer priority) {
        if(this.priority == null) {
            this.priority = priority;
        }
    }
}
