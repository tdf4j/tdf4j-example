package org.tdf4j.example.stackmachine;

import org.tdf4j.example.core.Module;
import org.tdf4j.example.core.utils.SavePrinter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;

public abstract class AbstractStackMachineModule implements Module {
    private Map<Pattern, BiConsumer<String, StackMachine.Context>> commands;

    protected AbstractStackMachineModule() {
        commands = new HashMap<>();
        configure();
    }

    protected void addExecution(final String template, final BiConsumer<String, StackMachine.Context> command) {
        if(!containsPattern(template)) {
            commands.put(Pattern.compile(template), command);
        } else {
            SavePrinter.savePrintln("[addExecution] Already contains pattern: " + template);
        }
    }

    private boolean containsPattern(final String template) {
        for(Pattern pattern: commands.keySet()) {
            if(pattern.pattern().equals(template)) {
                return true;
            }
        }

        return false;
    }

    public Map<Pattern, BiConsumer<String, StackMachine.Context>> getCommands() {
        return Collections.unmodifiableMap(commands);
    }
}
