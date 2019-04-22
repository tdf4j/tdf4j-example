package io.github.therealmone.core.utils;

import io.github.therealmone.core.beans.CommandBean;
import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.Rule;

import java.io.*;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public final class ResourceLoader {
    private static Set<CommandBean> commands;
    private static boolean loaded = false;

    public synchronized static void initialize() {
        if(!loaded) {
            Thread current = Thread.currentThread();

            try {
                loadCommands(current.getContextClassLoader().getResourceAsStream("commands.xml"));
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }

            loaded = true;
        }
    }

    private static void loadCommands(final InputStream is) throws Throwable {
        commands = new HashSet<>();

        Digester digester = new Digester();
        digester.setValidating(false);
        digester.addObjectCreate("commands/command", CommandBean.class);
        digester.addRule("commands/command", new CommandRule());
        digester.addRule("commands/command/type", new CommandTypeRule());
        digester.addRule("commands/command/template", new CommandTemplateRule());
        digester.parse(is);
    }

    public synchronized static Set<CommandBean> getCommands() {
        return new HashSet<>(commands);
    }

    private static class CommandRule extends Rule {
        @Override
        public void end(String namespace, String name) throws Exception {
            commands.add(getDigester().peek());
        }
    }

    private static class CommandTypeRule extends Rule {
        @Override
        public void body(String namespace, String name, String text) throws Exception {
            CommandBean command = getDigester().peek();
            command.setType(text);
        }
    }

    private static class CommandTemplateRule extends Rule {
        @Override
        public void body(String namespace, String name, String text) throws Exception {
            CommandBean command = getDigester().peek();
            command.setPattern(Pattern.compile(text));
        }
    }
}
