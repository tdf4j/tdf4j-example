package ru.therealmone.spoStackMachine;

@FunctionalInterface
public interface Command {
    void execute(String command);
}
