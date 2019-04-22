package io.github.therealmone.stackmachine;

import io.github.therealmone.core.interfaces.IConfig;
import io.github.therealmone.core.utils.SavePrinter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Pattern;

public abstract class AbstractStackMachineConfig implements IConfig {
    private Stack<Object> stack;
    private long cursor;
    private Map<Pattern, Execution> commands;

    protected AbstractStackMachineConfig() {
        stack = new Stack<>();
        commands = new HashMap<>();
        cursor = 0;
        configure();
        editConfig();
    }

    @Override
    public void editConfig() {
        /*Override it*/
    }

    protected void addExecution(final String template, final Execution command) {
        if(!containsPattern(template)) {
            commands.put(Pattern.compile(template), command);
        } else {
            SavePrinter.savePrintln("[addExecution] Already contains pattern: " + template);
        }
    }

    protected void removeExecution(final String template) {
        for(Pattern pattern : commands.keySet()) {
            if(pattern.pattern().equals(template)) {
                commands.remove(pattern);
            }
        }

        SavePrinter.savePrintln("[removeExecution] Pattern " + template + " wasn't found");
    }

    protected void editExecution(final String template, final Execution command) {
        for(Pattern pattern: commands.keySet()) {
            if (pattern.pattern().equals(template)) {
                commands.replace(pattern, command);
                return;
            }
        }

        SavePrinter.savePrintln("[editExecution] Pattern " + template + " wasn't found");
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

    protected Object stackPeek() {
        if(stack.size() != 0) {
            return stack.peek();
        }

        return null;
    }

    protected Object stackGet(final int index) {
        if(index >= 0 && index <= stack.size()) {
            return stack.get(index);
        }

        return null;
    }

    protected void increaseCursor() {
        cursor++;
    }

    protected void decreaseCursor() {
        cursor--;
    }

    protected void setCursor(long index) {
        cursor = index;
    }

    long getCursor() {
        return cursor;
    }

    private boolean containsPattern(final String template) {
        for(Pattern pattern: commands.keySet()) {
            if(pattern.pattern().equals(template)) {
                return true;
            }
        }

        return false;
    }


    public Stack<Object> getStack() {
        return stack;
    }

    public Map<Pattern, Execution> getCommands() {
        return Collections.unmodifiableMap(commands);
    }
}
