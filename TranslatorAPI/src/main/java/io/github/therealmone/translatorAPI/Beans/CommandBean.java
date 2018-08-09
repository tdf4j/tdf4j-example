package io.github.therealmone.translatorAPI.Beans;

import java.util.regex.Pattern;

public class CommandBean {
    private String type;
    private Pattern pattern;

    public CommandBean() {
        this.type = null;
        this.pattern = null;
    }

    public String getType() {
        return type;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setType(final String type) {
        if(this.type == null) {
            this.type = type;
        }
    }

    public void setPattern(final Pattern pattern) {
        if(this.pattern == null) {
            this.pattern = pattern;
        }
    }
}
