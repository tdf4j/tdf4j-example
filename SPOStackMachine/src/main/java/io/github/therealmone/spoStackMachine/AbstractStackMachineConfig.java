package io.github.therealmone.spoStackMachine;

import io.github.therealmone.core.interfaces.IConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Pattern;

public abstract class AbstractStackMachineConfig implements IConfig {
    private Stack<Object> stack;
    private Map<Pattern, Command> commands;

    protected AbstractStackMachineConfig() {
        stack = new Stack<>();
        commands = new HashMap<>();
        configure();
        editConfig();
    }

    @Override
    public void editConfig() {
        /*Override it*/
    }

    protected void addCommand(final String template, final Command command) {
        if(containsPattern(template)) {
            return;
        }

        commands.put(Pattern.compile(template), command);
    }

    protected void removeCommand(final String template) {
        for(Pattern pattern : commands.keySet()) {
            if(pattern.pattern().equals(template)) {}
        }
    }

    protected void editCommand(final String template, final Command command) {

    }

    protected void stackPush(final Object object) {
        stack.push(object);
    }

    protected Object stackPop() {
        if(stack.size() != 0) {
            return stack.pop();
        }

        return null;
    }

    protected Object stackGet(final int index) {
        if(index >= 0 && index <= stack.size()) {
            return stack.get(index);
        }

        return null;
    }

    private boolean containsPattern(final String template) {
        for(Pattern pattern: commands.keySet()) {
            if(pattern.pattern().equals(template)) {
                return true;
            }
        }

        return false;
    }
}
