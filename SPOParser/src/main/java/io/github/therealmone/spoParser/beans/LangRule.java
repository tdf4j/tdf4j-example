package io.github.therealmone.spoParser.beans;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LangRule {
    private boolean openByDefaultAsError;
    private Map<String, String[]> opening;
    private String[] defaultValue;

    public LangRule() {
        this.opening = new HashMap<>();
        this.openByDefaultAsError = true;
    }

    public LangRule openByDefault(final String... value) {
        this.openByDefaultAsError = false;
        this.defaultValue = value;
        return this;
    }

    public LangRule openWith(final String terminal, final String... value) {
        opening.put(terminal, value);
        return this;
    }

    public String[] getProductionValue(String terminal) {
        if(opening.containsKey(terminal)) {
            String[] value = opening.get(terminal);
            String[] tmp = new String[value.length];
            System.arraycopy(value, 0, tmp, 0, value.length);
            return tmp;
        } else {
            return openByDefaultAsError
                    ? new String[] {"ERROR"}
                    : defaultValue;
        }
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        opening.forEach((terminal, value) -> out.append("\t").append(terminal).append(" --> ").append(Arrays.toString(value)).append("\n"));
        return out.toString();
    }
}
