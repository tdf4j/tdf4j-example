package io.github.therealmone.jtrAPI;

import io.github.therealmone.tdf4j.parser.config.AbstractParserModule;

import java.util.List;

public abstract class ParserModule extends AbstractParserModule {
    public abstract List<String> getRPN();
}
