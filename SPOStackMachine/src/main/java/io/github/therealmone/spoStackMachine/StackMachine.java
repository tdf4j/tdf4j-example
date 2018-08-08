package io.github.therealmone.spoStackMachine;

import io.github.therealmone.translatorAPI.Interfaces.Visitor;

public interface StackMachine extends Visitor {
    void showVariables();

    static StackMachine getInstance() {
        return new StackMachineImpl();
    }
}
