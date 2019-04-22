package io.github.therealmone.stackmachine;

import io.github.therealmone.core.interfaces.Visitor;

public interface StackMachine extends Visitor {
    void showVariables();

    static StackMachine getInstance() {
        return new StackMachineImpl();
    }
}
