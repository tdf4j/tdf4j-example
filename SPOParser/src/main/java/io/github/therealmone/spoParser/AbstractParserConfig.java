package io.github.therealmone.spoParser;

import io.github.therealmone.core.utils.SavePrinter;
import io.github.therealmone.spoParser.beans.LangRule;

import java.util.*;

public abstract class AbstractParserConfig {
    private Map<String, LangRule> langRules;
    private String initProduction = "lang";

    protected AbstractParserConfig() {
        langRules = new HashMap<>();
        configure();
        editConfig();
    }

    public abstract void configure();

    public void editConfig() {
        /*Override it*/
    }

    protected LangRule addLangRule(String productionName) {
        LangRule rule = new LangRule();
        langRules.put(productionName, rule);
        return rule;
    }

    protected void removeLangRule(final String productionName) {
        if(langRules.containsKey(productionName)) {
            langRules.remove(productionName);
        } else {
            SavePrinter.savePrintln("[removeLangRule] Lang rule " + productionName + " wasn't found");
        }
    }

    protected LangRule editLangRule(final String productionName) {
        if(langRules.containsKey(productionName)) {
            return langRules.get(productionName);
        } else {
            SavePrinter.savePrintln("[editLangRule] Lang rule " + productionName + " wasn't found. (translator adds new rule automatically...)");
            LangRule newRule = new LangRule();
            langRules.put(productionName, newRule);
            return newRule;
        }
    }

    protected void setInitProduction(final String production) {
        this.initProduction = production;
    }

    protected String getInitProduction() {
        return initProduction;
    }

    protected Map<String, LangRule> getLangRules() {
        return Collections.unmodifiableMap(langRules);
    }
}
