package ru.therealmone.translatorAPI.Interfaces;

import ru.therealmone.translatorAPI.Exceptions.TranslatorException;

public interface Visitable {
    void accept(Visitor v) throws TranslatorException;
}
