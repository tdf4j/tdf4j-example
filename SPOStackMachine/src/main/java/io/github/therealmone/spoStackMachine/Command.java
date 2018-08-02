package io.github.therealmone.spoStackMachine;

@FunctionalInterface
public interface Command {
    void execute(String command);
}
