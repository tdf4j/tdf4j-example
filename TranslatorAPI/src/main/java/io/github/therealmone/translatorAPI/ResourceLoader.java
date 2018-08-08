package io.github.therealmone.translatorAPI;

import com.opencsv.CSVReader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public final class ResourceLoader {
    private static Map<String, Pattern> lexemes;
    private static Map<String, Integer> lexemePriority;
    private static Map<Integer, String[]> langRules;
    private static Map<String, Map<String, Integer>> analyzeTable;
    private static Map<String, Pattern> commands;

    public synchronized static void initialize() {
        Thread current = Thread.currentThread();

        loadLexemes(current.getContextClassLoader().getResourceAsStream("lexemes.xml"));
        loadLangRules(current.getContextClassLoader().getResourceAsStream("langRules.csv"));
        loadAnalyzeTable(current.getContextClassLoader().getResourceAsStream("analyzeTable.csv"));
        loadCommands(current.getContextClassLoader().getResourceAsStream("commands.xml"));
    }

    private static void loadLexemes(final InputStream is) {
        lexemes = new HashMap<>();
        lexemePriority = new HashMap<>();

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbf.newDocumentBuilder();
            Document doc = docBuilder.parse(is);

            Node root = doc.getDocumentElement();
            NodeList childes = root.getChildNodes();


            for (int i = 0; i < childes.getLength(); i++) {
                Node node = childes.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String type = element.getElementsByTagName("type").item(0).getTextContent();
                    String template = element.getElementsByTagName("template").item(0).getTextContent();
                    String priority = element.getAttribute("priority");
                    addLexeme(type, template, Integer.parseInt(priority));
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
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
                    langRules.put(Integer.parseInt(nextLine[0].trim()), nextLine[2].split("\\s\\+\\s"));
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
                    tmp.put(description[i], Integer.parseInt(nextLine[i]));
                }
                analyzeTable.put(nextLine[0], tmp);
            }

            csvReader.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadCommands(final InputStream is) {
        commands = new HashMap<>();

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbf.newDocumentBuilder();
            Document doc = docBuilder.parse(is);

            Node root = doc.getDocumentElement();
            NodeList childes = root.getChildNodes();


            for (int i = 0; i < childes.getLength(); i++) {
                Node node = childes.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String type = element.getElementsByTagName("type").item(0).getTextContent();
                    String template = element.getElementsByTagName("template").item(0).getTextContent();
                    commands.put(type, Pattern.compile(template));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void addLexeme(final String type, final String pattern, final Integer priority) {
        if (!lexemes.containsKey(type)) {
            lexemes.put(type, Pattern.compile(pattern));
            lexemePriority.put(type, priority);
        }
    }

    public static Map<String, Pattern> getLexemes() {
        return lexemes;
    }

    public static Map<String, Integer> getLexemePriority() {
        return lexemePriority;
    }

    public static Map<Integer, String[]> getLangRules() {
        return langRules;
    }

    public static Map<String, Map<String, Integer>> getAnalyzeTable() {
        return analyzeTable;
    }

    public static Map<String, Pattern> getCommands() {
        return commands;
    }
}
