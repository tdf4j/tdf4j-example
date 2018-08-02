package io.github.therealmone.translatorAPI;


import java.io.InputStream;

class ResourceFinder {
    private final InputStream lexemesIS;
    private final InputStream langRulesIS;
    private final InputStream analyzeTableIS;
    private final InputStream commandsIS;

    ResourceFinder() {
        lexemesIS = this.getClass().getResourceAsStream("/lexemes.xml");
        langRulesIS = this.getClass().getResourceAsStream("/langRules.csv");
        analyzeTableIS = this.getClass().getResourceAsStream("/analyzeTable.csv");
        commandsIS = this.getClass().getResourceAsStream("/commands.xml");
    }

    InputStream getLexemesIS() {
        return lexemesIS;
    }

    InputStream getLangRulesIS() {
        return langRulesIS;
    }

    InputStream getAnalyzeTableIS() {
        return analyzeTableIS;
    }

    InputStream getCommandsIS() {
        return commandsIS;
    }
}
