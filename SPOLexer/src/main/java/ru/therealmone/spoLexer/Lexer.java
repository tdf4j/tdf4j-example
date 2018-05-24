package ru.therealmone.spoLexer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ru.therealmone.translatorAPI.Token;
import ru.therealmone.translatorAPI.UnexpectedSymbolException;

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

    public Lexer(String fileDir) {
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
                System.out.println("Can't find lexemes.xml file in: \n" + fileDir);
                e.printStackTrace();
                System.exit(1);
            } catch (ParserConfigurationException e) {
                System.out.println("DocumentBuilder cannot be created which satisfies the configuration requested.");
                e.printStackTrace();
                System.exit(1);
            } catch(SAXException e) {
                System.out.println("XML parse error.");
                e.printStackTrace();
                System.exit(1);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
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
        for(Map.Entry<String, Pattern> entry: lexemes.entrySet()) {
            System.out.println("Lexeme #" + priority.get(entry.getKey()) + ": " + entry.getKey() + " --> " + entry.getValue().pattern());
        }
    }

    public void generateTokens(String input) {
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
            System.exit(1);
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
        for(Token token: tokens) {
            System.out.println("Token №" + tokens.indexOf(token) + " : " + token.getType() + " --> " + token.getValue());
        }
    }

    boolean match(String lexeme, String string) {
        Pattern p = lexemes.get(lexeme);
        Matcher m = p.matcher(string);
        return m.matches();
    }
}
