package io.github.therealmone.spoParser;

import io.github.therealmone.spoParser.beans.LangRule;

import java.util.*;

public abstract class AbstractParserConfig {
    private Map<String, LangRule> langRules;

    public AbstractParserConfig() {
        langRules = new HashMap<>();
        configure();
    }

    public abstract void configure();

    public LangRule addLangRule(String productionName) {
        LangRule rule = new LangRule();
        langRules.put(productionName, rule);
        return rule;
    }

    public Map<String, LangRule> getLangRules() {
        return Collections.unmodifiableMap(langRules);
    }
}
