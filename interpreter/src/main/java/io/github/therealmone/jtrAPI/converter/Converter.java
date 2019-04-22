package io.github.therealmone.jtrAPI.converter;

import io.github.therealmone.tdf4j.parser.model.ast.ASTElement;

import java.util.List;

public interface Converter {
    List<String> convert(final ASTElement element);
}
