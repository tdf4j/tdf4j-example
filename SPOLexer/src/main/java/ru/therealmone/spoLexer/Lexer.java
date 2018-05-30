package ru.therealmone.spoLexer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ru.therealmone.translatorAPI.Exceptions.LexerException;
import ru.therealmone.translatorAPI.Token;
import ru.therealmone.spoLexer.exceptions.UnexpectedSymbolException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.*;

public class Lexer {
    public ArrayList<Token> tokens = new ArrayList<>();
    public Map<String, Pattern> lexemes = new HashMap<>();
    private Map<String, Integer> priority = new HashMap<>();

    private static final char END_SYMBOL = '$';

    public Lexer(String fileDir) throws LexerException {
            try {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = dbf.newDocumentBuilder();
                Document doc = docBuilder.parse(fileDir);

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
            } catch (FileNotFoundException e) {
                throw new LexerException("Can't find lexemes.xml", e);
            } catch (ParserConfigurationException e) {
                throw new LexerException("DocumentBuilder cannot be created which satisfies the configuration requested", e);
            } catch(SAXException e) {
                throw new LexerException("XML parse error", e);
            } catch (IOException e) {
                throw new LexerException("I/O exception", e);
            }
    }

    void addLexeme(String type, String pattern, Integer priority) {
        if (!lexemes.containsKey(type)) {
            this.lexemes.put(type, Pattern.compile(pattern));
            this.priority.put(type, priority);
        } else
            System.out.println("Lexeme " + type + " already exists!");
    }

    public void showLexemes() {
        System.out.println("LEXEMES: ");
        lexemes.forEach( (lexeme, pattern) -> System.out.println(lexeme + " --> " + pattern));
    }

    public void generateTokens(String input) throws LexerException {
        try {
            input = input.replaceAll("\\$", "");
            input += END_SYMBOL;

            tokens.clear();
            StringBuilder tempString = new StringBuilder();

            while (input.charAt(0) != END_SYMBOL) {
                input = input.trim();
                tempString.append(input.charAt(tempString.length()));

                if (!checkLexemes(tempString.toString())) {
                    if (tempString.length() > 1) {
                        tempString.deleteCharAt(tempString.length() - 1);
                        tokens.add(new Token(chooseLexeme(tempString.toString()), tempString.toString()));
                        input = input.substring(tempString.length());
                        tempString.delete(0, tempString.length());
                    } else {
                        throw new UnexpectedSymbolException(input, tokens);
                    }
                }
            }

            tokens.add(new Token("$", "$"));

        } catch (UnexpectedSymbolException e) {
            e.message();
            throw new LexerException("Can't generate tokens", e);
        }
    }
    
    private boolean checkLexemes(String str) {
        for(Map.Entry<String, Pattern> entry: lexemes.entrySet()) {
           if(match(entry.getKey(), str))
               return true;
        }
        return false;
    }

    private String chooseLexeme(String str) {
        int tmpPriv = 0;
        String lexType = "";

        for(Map.Entry<String, Pattern> entry: lexemes.entrySet()) {
            if(match(entry.getKey(), str)) {
                if(priority.get(entry.getKey()) > tmpPriv) {
                    lexType = entry.getKey();
                    tmpPriv = priority.get(entry.getKey());
                }
            }
        }
        return lexType;
    }

    public void showTokens() {
        System.out.println("GENERATED TOKENS: ");
        tokens.forEach(System.out::println);
    }

    boolean match(String lexeme, String string) {
        Pattern p = lexemes.get(lexeme);
        Matcher m = p.matcher(string);
        return m.matches();
    }
}
