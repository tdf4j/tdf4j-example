package io.github.therealmone.core.utils;

import com.opencsv.CSVReader;
import io.github.therealmone.core.beans.CommandBean;
import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.Rule;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public final class ResourceLoader {
    private static Caster caster;
    private static Map<Integer, String[]> langRules;
    private static Map<String, Map<String, Integer>> analyzeTable;
    private static Set<CommandBean> commands;
    private static boolean loaded = false;

    static {
        caster = new Caster();
    }

    public synchronized static void initialize() {
        if(!loaded) {
            Thread current = Thread.currentThread();

            try {
                loadLangRules(current.getContextClassLoader().getResourceAsStream("langRules.csv"));
                loadAnalyzeTable(current.getContextClassLoader().getResourceAsStream("analyzeTable.csv"));
                loadCommands(current.getContextClassLoader().getResourceAsStream("commands.xml"));
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }

            loaded = true;
        }
    }

    private static void loadLangRules(final InputStream is) {
        langRules = new HashMap<>();

        try {
            CSVReader csvReader = new CSVReader(new InputStreamReader(is));
            String[] nextLine;
            csvReader.readNext();

            while ((nextLine = csvReader.readNext()) != null) {
                try {
                    langRules.put(caster.castToInt(nextLine[0].trim()), nextLine[2].split("\\s\\+\\s"));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            csvReader.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadAnalyzeTable(final InputStream is) {
        analyzeTable = new HashMap<>();

        try {
            CSVReader csvReader = new CSVReader(new InputStreamReader(is));
            String[] description = csvReader.readNext();
            String[] nextLine;

            while ((nextLine = csvReader.readNext()) != null) {
                Map<String, Integer> tmp = new HashMap<>();
                for (int i = 1; i < nextLine.length; i++) {
                    tmp.put(description[i], caster.castToInt(nextLine[i]));
                }
                analyzeTable.put(nextLine[0], tmp);
            }

            csvReader.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
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

    public synchronized static Map<Integer, String[]> getLangRules() {
        return new HashMap<>(langRules);
    }

    public synchronized static Map<String, Map<String, Integer>> getAnalyzeTable() {
        return new HashMap<>(analyzeTable);
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
