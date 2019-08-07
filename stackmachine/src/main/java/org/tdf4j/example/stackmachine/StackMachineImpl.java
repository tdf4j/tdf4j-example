package org.tdf4j.example.stackmachine;

import org.tdf4j.example.stackmachine.exceptions.UnknownCommandException;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;

class StackMachineImpl implements StackMachine {
    private final Map<Pattern, BiConsumer<String, Context>> commands;

    StackMachineImpl(final AbstractStackMachineModule module) {
        this.commands = module.getCommands();
    }

    @Override
    public void process(final List<String> rpn) {
        final Context context = new Context();
        while (!rpn.get(context.getCursor()).equals("$")) {
            find(rpn.get(context.getCursor())).accept(rpn.get(context.getCursor()), context);
        }
    }

    private BiConsumer<String, Context> find(final String command) {
        for(final Map.Entry<Pattern, BiConsumer<String, Context>> entry : commands.entrySet()) {
            if(entry.getKey().matcher(command).matches()) {
                return entry.getValue();
            }
        }
        throw new UnknownCommandException(command);
    }
}
