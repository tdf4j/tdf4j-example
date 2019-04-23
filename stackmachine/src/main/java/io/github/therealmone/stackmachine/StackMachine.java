package io.github.therealmone.stackmachine;

import io.github.therealmone.core.interfaces.Visitor;

import java.util.List;

public interface StackMachine extends Visitor<List<String>> {
    void showVariables();

    static StackMachine getInstance() {
        return new StackMachineImpl();
    }
}
