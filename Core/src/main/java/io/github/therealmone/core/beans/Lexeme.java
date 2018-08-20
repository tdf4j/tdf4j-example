package io.github.therealmone.core.beans;

import java.util.regex.Pattern;

public class Lexeme {
    private final String type;
    private final Pattern pattern;
    private final Integer priority;

    public Lexeme(final String type, final String template, final int priority) {
        this.type = type;
        this.pattern = Pattern.compile(template);
        this.priority = priority;
    }

    public String getType() {
        return type;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public Integer getPriority() {
        return priority;
    }
}
