package io.github.therealmone.spoStackMachine;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Pattern;

public abstract class AbstractStackMachineConfig {
    private Stack<Object> stack;
    private Map<Pattern, Command> commands;

    protected AbstractStackMachineConfig() {
        stack = new Stack<>();
        commands = new HashMap<>();
        configure();
        editConfig();
    }

    public abstract void configure();

    public void editConfig() {
        /*Override it*/
    }

    protected void addCommand(final String template, final Command command) {
        for(Pattern pattern: commands.keySet()) {
            if(pattern.pattern().equals(template)) {
                return;
            }
        }
    }

    protected void removeCommand(final String template) {

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
}
