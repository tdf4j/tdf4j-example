package ru.therealmone.spoStackMachine;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ru.therealmone.translatorAPI.Exceptions.UnknownCommandException;
import ru.therealmone.translatorAPI.Token;
import ru.therealmone.translatorAPI.Interfaces.Visitor;

import java.io.FileNotFoundException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StackMachine implements Visitor {
    private Map<String, Pattern> commands = new HashMap<>();
    private Map<String, Command> executions = new HashMap<>();
    private Stack<String> stack = new Stack<>();

    @Override
    public void visit(Token token) {}

    @Override
    public void visit(String opn) {
        try {
            calculate(opn);
        } catch (NumberFormatException e) {
            System.out.println("Unexpected value type: " + stack.peek());
            e.printStackTrace();
            System.exit(1);
        }
    }

    public StackMachine(String commandsDir) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbf.newDocumentBuilder();
            Document doc = docBuilder.parse(commandsDir);

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
        } catch (FileNotFoundException e) {
            System.out.println("Can't find commands.xml file in: \n" + commandsDir);
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

        initExecutions();
    }

    private void calculate(String opn) throws NumberFormatException {
        String[] splittedOPN = opn.split(",");
        for(String com: splittedOPN) {
            executions.get(match(com)).execute(com);
        }
    }

    private String match(String com) {
        try {
            for (Map.Entry<String, Pattern> entry : commands.entrySet()) {
                Matcher m = entry.getValue().matcher(com);
                if (m.matches())
                    return entry.getKey();
            }
            throw new UnknownCommandException(com);
        } catch (UnknownCommandException e) {
            e.message();
            System.exit(1);
        }
        return com; //Unreachable ????
    }

    private void initExecutions() {
        commands.forEach( (command, pattern) -> {
            //TODO: Описать все команды
            switch (command) {
                case "TAKE_VALUE" : {executions.put(command, com -> {}); break;}
                case "TAKE_VAR_NAME" : {executions.put(command, com -> {}); break;}
                case "GOTO_ON_FALSE" : {executions.put(command, com -> {}); break;}
                case "GOTO_ON_TRUE" : {executions.put(command, com -> {}); break;}
                case "DEL" : {executions.put(command, com -> {}); break;}
                case "MUL" : {executions.put(command, com -> {}); break;}
                case "PLUS" : {executions.put(command, com -> {}); break;}
                case "MINUS" : {executions.put(command, com -> {}); break;}
                case "LESS" : {executions.put(command, com -> {}); break;}
                case "MORE" : {executions.put(command, com -> {}); break;}
                case "LESS_OR_EQUALS" : {executions.put(command, com -> {}); break;}
                case "MORE_OR_EQUALS" : {executions.put(command, com -> {}); break;}
                case "EQUALS" : {executions.put(command, com -> {}); break;}
                case "AND" : {executions.put(command, com -> {}); break;}
                case "OR" : {executions.put(command, com -> {}); break;}
                case "XOR" : {executions.put(command, com -> {}); break;}
                case "ASSIGN" : {executions.put(command, com -> {}); break;}
                case "TYPEOF" : {executions.put(command, com -> {}); break;}
                case "PUT" : {executions.put(command, com -> {}); break;}
                case "GET" : {executions.put(command, com -> {}); break;}
                case "REMOVE" : {executions.put(command, com -> {}); break;}
                case "REWRITE" : {executions.put(command, com -> {}); break;}
                case "$" : {executions.put(command, com -> System.exit(0)); break;}
            }
        });
    }

    public void showVariables() {
        System.out.println("Current variables: ");
        //TODO: Реализовать добавление переменных
        //variables.show();
    }
}
