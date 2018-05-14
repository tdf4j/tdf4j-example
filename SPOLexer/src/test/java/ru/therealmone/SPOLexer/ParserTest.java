package ru.therealmone.SPOLexer;

import com.opencsv.CSVReader;
import org.junit.Test;
import static org.junit.Assert.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.therealmone.SPOParser.Parser;
import ru.therealmone.TranslatorAPI.Token;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Pattern;

public class ParserTest {
    private static HashMap<String, Pattern> lexemes = new HashMap<>();
    private static HashMap<String, Integer> priorities = new HashMap<>();
    private static HashMap<Integer, String[]> langRules = new HashMap<>();
    private static HashMap<String, Map<String, Integer>> analyzeTable = new HashMap<>();


    @Test
    public void parserTests() {
        loadLexemes();
        loadLang();

        Lexer lexer = new Lexer(lexemes, priorities);
        HashSet<String> terminals = new HashSet<>(lexer.lexemes.keySet());

        //while tests
        lexer.generateTokens("while() {}");
        assertFalse(check(lexer.tokens, new Parser(langRules, analyzeTable, lexemes.keySet())));

        lexer.generateTokens("while(a > b) {}");
        assertTrue(check(lexer.tokens, new Parser(langRules, analyzeTable, lexemes.keySet())));

        lexer.generateTokens("while((a > b) & (c < d)) {}");
        assertTrue(check(lexer.tokens, new Parser(langRules, analyzeTable, lexemes.keySet())));

        lexer.generateTokens("while((((((a > b))))) & (((((c < d)))))) {}");
        assertTrue(check(lexer.tokens, new Parser(langRules, analyzeTable, lexemes.keySet())));

        lexer.generateTokens("while((a == b) & (c < d * (5 + 1)) | ((10 *(s + 150) <= 300))){}");
        assertTrue(check(lexer.tokens, new Parser(langRules, analyzeTable, lexemes.keySet())));

        lexer.generateTokens("while(((((((((((c < d))))))))))) {}");
        assertTrue(check(lexer.tokens, new Parser(langRules, analyzeTable, lexemes.keySet())));

        lexer.generateTokens("while(a < b & c > d) {}");
        assertTrue(check(lexer.tokens, new Parser(langRules, analyzeTable, lexemes.keySet())));

        lexer.generateTokens("while(a == b & c < d * (5 + 1) | 10 * (s + 150) <= 300){}");
        assertTrue(check(lexer.tokens, new Parser(langRules, analyzeTable, lexemes.keySet())));

        //if tests
        lexer.generateTokens("if() {}");
        assertFalse(check(lexer.tokens, new Parser(langRules, analyzeTable, lexemes.keySet())));

        lexer.generateTokens("if(a > b) {}");
        assertTrue(check(lexer.tokens, new Parser(langRules, analyzeTable, lexemes.keySet())));

        lexer.generateTokens("if((a > b) & (c < d)) {}");
        assertTrue(check(lexer.tokens, new Parser(langRules, analyzeTable, lexemes.keySet())));

        lexer.generateTokens("if((((((a > b))))) & (((((c < d)))))) {}");
        assertTrue(check(lexer.tokens, new Parser(langRules, analyzeTable, lexemes.keySet())));

        lexer.generateTokens("if((a == b) & (c < d * (5 + 1)) | ((10 *(s + 150) <= 300))){}");
        assertTrue(check(lexer.tokens, new Parser(langRules, analyzeTable, lexemes.keySet())));

        lexer.generateTokens("if(((((((((((c < d))))))))))) {}");
        assertTrue(check(lexer.tokens, new Parser(langRules, analyzeTable, lexemes.keySet())));

        lexer.generateTokens("if(a < b & c > d) {}");
        assertTrue(check(lexer.tokens, new Parser(langRules, analyzeTable, lexemes.keySet())));

        lexer.generateTokens("if(a == b & c < d * (5 + 1) | 10 * (s + 150) <= 300){}");
        assertTrue(check(lexer.tokens, new Parser(langRules, analyzeTable, lexemes.keySet())));

        //for tests
        lexer.generateTokens("for(i = 0; ; i = i + 1){}");
        assertFalse(check(lexer.tokens, new Parser(langRules, analyzeTable, lexemes.keySet())));

        lexer.generateTokens("for(i = 0; i < 100; i = i + 1) {}");
        assertTrue(check(lexer.tokens, new Parser(langRules, analyzeTable, lexemes.keySet())));

        lexer.generateTokens("for(i = (((((a + b))))) * (((((c + d))))); i > 100; i = i + 1){}");
        assertTrue(check(lexer.tokens, new Parser(langRules, analyzeTable, lexemes.keySet())));

        lexer.generateTokens("for(i = (((1 + 1) * (1 + 2)) * (1 + 1)) * (100 - 10); i > 100; i = i + 1){}");
        assertTrue(check(lexer.tokens, new Parser(langRules, analyzeTable, lexemes.keySet())));

        //do-while tests
        lexer.generateTokens("do{}while()");
        assertFalse(check(lexer.tokens, new Parser(langRules, analyzeTable, lexemes.keySet())));

        lexer.generateTokens("do{}while(a > b)");
        assertTrue(check(lexer.tokens, new Parser(langRules, analyzeTable, lexemes.keySet())));

        lexer.generateTokens("do{}while((a > b) & (c < d))");
        assertTrue(check(lexer.tokens, new Parser(langRules, analyzeTable, lexemes.keySet())));

        lexer.generateTokens("do{}while((((((a > b))))) & (((((c < d))))))");
        assertTrue(check(lexer.tokens, new Parser(langRules, analyzeTable, lexemes.keySet())));

        lexer.generateTokens("do{}while((a == b) & (c < d * (5 + 1)) | ((10 *(s + 150) <= 300)))");
        assertTrue(check(lexer.tokens, new Parser(langRules, analyzeTable, lexemes.keySet())));

        lexer.generateTokens("do{}while(((((((((((c < d)))))))))))");
        assertTrue(check(lexer.tokens, new Parser(langRules, analyzeTable, lexemes.keySet())));

        lexer.generateTokens("do{}while(a < b & c > d)");
        assertTrue(check(lexer.tokens, new Parser(langRules, analyzeTable, lexemes.keySet())));

        lexer.generateTokens("do{}while(a == b & c < d * (5 + 1) | 10 * (s + 150) <= 300)");
        assertTrue(check(lexer.tokens, new Parser(langRules, analyzeTable, lexemes.keySet())));

        //Combinations
        lexer.generateTokens("while(a > b) {if (a < d) {do{i = i;}while(a < b)} else {for(i = i; i < 100; i = i + 1){i = i;}}}");
        assertTrue(check(lexer.tokens, new Parser(langRules, analyzeTable, lexemes.keySet())));

        lexer.generateTokens("a = b; c = a; a = a;");
        assertTrue(check(lexer.tokens, new Parser(langRules, analyzeTable, lexemes.keySet())));
    }

    private boolean check(ArrayList<Token> tokens, Parser parser) {
        for(Token token: tokens) {
            token.accept(parser);
            if (parser.ERROR) {
                System.out.println("ERROR: Unexpected token " + token.getType());
                return false;
            }
            System.out.println("Success: " + token.getType() + " " + token.getValue());
        }
        return true;
    }

    private static void loadLexemes() {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbf.newDocumentBuilder();
            Document doc = docBuilder.parse("D:/JavaProjects/SPOTranslator/SPOLexer/src/main/resources/lexemes.xml");

            Node root = doc.getDocumentElement();
            NodeList childes = root.getChildNodes();

            for (int i = 0; i < childes.getLength(); i++) {
                Node node = childes.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String type = element.getElementsByTagName("type").item(0).getTextContent();
                    String template = element.getElementsByTagName("template").item(0).getTextContent();
                    String priority = element.getAttribute("priority");

                    if (!lexemes.containsKey(type)) {
                        lexemes.put(type, Pattern.compile(template));
                        priorities.put(type, Integer.parseInt(priority));
                    } else
                        System.out.println("Lexeme " + type + " already exists!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadLang() {
        try {
            CSVReader csvReader = new CSVReader(new FileReader("D:/JavaProjects/SPOTranslator/SPOLexer/src/main/resources/langRules.csv"));
            String[] nextLine;
            csvReader.readNext();

            while((nextLine = csvReader.readNext()) != null) {
                try {
                    langRules.put(Integer.parseInt(nextLine[0].trim()), nextLine[2].split("\\s\\+\\s"));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            csvReader.close();

            csvReader = new CSVReader(new FileReader("D:/JavaProjects/SPOTranslator/SPOLexer/src/main/resources/analyzeTable.csv"));
            String[] description = csvReader.readNext();

            while((nextLine = csvReader.readNext()) != null) {
                Map<String, Integer> tmp = new HashMap<>();
                for (int i = 1; i < nextLine.length; i++) {
                    tmp.put(description[i], Integer.parseInt(nextLine[i]));
                }
                analyzeTable.put(nextLine[0], tmp);
            }
            csvReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
