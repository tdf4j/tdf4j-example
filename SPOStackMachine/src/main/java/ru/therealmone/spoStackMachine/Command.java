package ru.therealmone.spoStackMachine;

import ru.therealmone.translatorAPI.Exceptions.StackMachineException;

@FunctionalInterface
public interface Command {
    void execute(String command) throws StackMachineException;
}
