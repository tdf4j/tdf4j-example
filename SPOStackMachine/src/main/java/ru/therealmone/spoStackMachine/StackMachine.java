package ru.therealmone.spoStackMachine;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ru.therealmone.translatorAPI.Exceptions.KeyAlreadyExistsException;
import ru.therealmone.translatorAPI.Exceptions.NoSuchElementException;
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
    private Map<String, Object> variables = new HashMap<>();
    private Stack<String> stack = new Stack<>();
    private int cursor = 0;

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

        while (!splittedOPN[cursor].equals("$")) {
            executions.get(match(splittedOPN[cursor])).execute(splittedOPN[cursor]);
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

                case "DIGIT" : {executions.put(command, com -> {
                    stack.push("" + Double.parseDouble(com));
                    cursor++;
                }); break;}

                case "TAKE_VALUE" : {executions.put(command, com -> {
                    String varName = com.substring(1, com.length());

                    if(variables.containsKey(varName)) {
                        if (variables.get(varName) instanceof Double) {
                            Double tmp = (Double) variables.get(varName);
                            stack.push("" + tmp);
                        } else {
                            System.out.println("Wrong type of " + varName);
                            System.exit(1);
                        }
                    } else {
                        System.out.println("Can't find variable " + varName);
                    }

                    cursor++;
                }); break;}

                case "TAKE_VAR_NAME" : {executions.put(command, com -> {
                    stack.push(com.substring(1, com.length()));
                    cursor++;
                }); break;}

                case "GOTO_ON_FALSE" : {executions.put(command, com -> {
                    if(!Boolean.parseBoolean(stack.pop()))
                        cursor = Integer.parseInt(com.substring(3, com.length()));
                    else
                        cursor++;
                }); break;}

                case "GOTO_ON_TRUE" : {executions.put(command, com -> {
                    if(Boolean.parseBoolean(stack.pop()))
                        cursor = Integer.parseInt(com.substring(3, com.length()));
                    else
                        cursor++;
                }); break;}

                case "GOTO" : {executions.put(command, com ->
                        cursor = Integer.parseInt(com.substring(2, com.length())));
                break;}

                case "DEL" : {executions.put(command, com -> {
                    double p2 = Double.parseDouble(stack.pop());
                    double p1 = Double.parseDouble(stack.pop());
                    stack.push("" + (p1 / p2));
                    cursor++;
                }); break;}

                case "MUL" : {executions.put(command, com -> {
                    double p2 = Double.parseDouble(stack.pop());
                    double p1 = Double.parseDouble(stack.pop());
                    stack.push("" + (p1 * p2));
                    cursor++;
                }); break;}

                case "PLUS" : {executions.put(command, com -> {
                    double p2 = Double.parseDouble(stack.pop());
                    double p1 = Double.parseDouble(stack.pop());
                    stack.push("" + (p1 + p2));
                    cursor++;
                }); break;}

                case "MINUS" : {executions.put(command, com -> {
                    double p2 = Double.parseDouble(stack.pop());
                    double p1 = Double.parseDouble(stack.pop());
                    stack.push("" + (p1 - p2));
                    cursor++;
                }); break;}

                case "LESS" : {executions.put(command, com -> {
                    double p2 = Double.parseDouble(stack.pop());
                    double p1 = Double.parseDouble(stack.pop());
                    stack.push("" + (p1 < p2));
                    cursor++;
                }); break;}

                case "MORE" : {executions.put(command, com -> {
                    double p2 = Double.parseDouble(stack.pop());
                    double p1 = Double.parseDouble(stack.pop());
                    stack.push("" + (p1 > p2));
                    cursor++;
                }); break;}

                case "LESS_OR_EQUALS" : {executions.put(command, com -> {
                    double p2 = Double.parseDouble(stack.pop());
                    double p1 = Double.parseDouble(stack.pop());
                    stack.push("" + (p1 <= p2));
                    cursor++;
                }); break;}

                case "MORE_OR_EQUALS" : {executions.put(command, com -> {
                    double p2 = Double.parseDouble(stack.pop());
                    double p1 = Double.parseDouble(stack.pop());
                    stack.push("" + (p1 >= p2));
                    cursor++;
                }); break;}

                case "EQUALS" : {executions.put(command, com -> {
                    double p2 = Double.parseDouble(stack.pop());
                    double p1 = Double.parseDouble(stack.pop());
                    stack.push("" + (p1 == p2));
                    cursor++;
                }); break;}

                case "AND" : {executions.put(command, com -> {
                    boolean p2 = Boolean.parseBoolean(stack.pop());
                    boolean p1 = Boolean.parseBoolean(stack.pop());
                    stack.push("" + (p1 && p2));
                    cursor++;
                }); break;}

                case "OR" : {executions.put(command, com -> {
                    boolean p2 = Boolean.parseBoolean(stack.pop());
                    boolean p1 = Boolean.parseBoolean(stack.pop());
                    stack.push("" + (p1 || p2));
                    cursor++;
                }); break;}

                case "XOR" : {executions.put(command, com -> {
                    boolean p2 = Boolean.parseBoolean(stack.pop());
                    boolean p1 = Boolean.parseBoolean(stack.pop());
                    stack.push("" + (p1 ^ p2));
                    cursor++;
                }); break;}

                case "ASSIGN" : {executions.put(command, com -> {
                    Double value = Double.parseDouble(stack.pop());
                    String varName = stack.pop();
                    if(variables.containsKey(varName))
                        variables.replace(varName, value);
                    else {
                        System.out.println("Can't find variable " + varName);
                        System.exit(1);
                    }
                    cursor++;
                }); break;}

                case "NEW" : {executions.put(command, com -> {
                    variables.put(stack.peek(), null);
                    cursor++;
                }); break;}

                case "TYPEOF" : {executions.put(command, com -> {
                    String type = stack.pop();
                    switch (type) {
                        case "hashmap" : {
                            variables.replace(
                                    stack.pop(),
                                    new ru.therealmone.spoStackMachine.HashMap());
                            break;
                        }
                        default: {
                            System.out.println("Unknown type " + type);
                            System.exit(1);
                        }
                    }
                    cursor++;
                }); break;}

                case "PUT" : {executions.put(command, com -> {
                    String varName = stack.pop();
                    String collection = stack.pop();

                    Double var = (Double) variables.get(varName);
                    ru.therealmone.spoStackMachine.HashMap col =
                            (ru.therealmone.spoStackMachine.HashMap) variables.get(collection);

                    try {
                        col.add(varName, var);
                    } catch (KeyAlreadyExistsException e) {
                        e.message();
                        System.exit(1);
                    }

                    cursor++;
                }); break;}

                case "GET" : {executions.put(command, com -> {
                    String varName = stack.pop();
                    String collection = stack.pop();

                    ru.therealmone.spoStackMachine.HashMap col =
                            (ru.therealmone.spoStackMachine.HashMap) variables.get(collection);

                    try {
                        stack.push("" + col.get(varName));
                    } catch (NoSuchElementException e) {
                        e.message();
                        System.exit(1);
                    }

                    cursor++;
                }); break;}

                case "REMOVE" : {executions.put(command, com -> {
                    String varName = stack.pop();
                    String collection = stack.pop();

                    ru.therealmone.spoStackMachine.HashMap col =
                            (ru.therealmone.spoStackMachine.HashMap) variables.get(collection);

                    try {
                        col.remove(varName);
                    } catch (NoSuchElementException e) {
                        e.message();
                        System.exit(1);
                    }

                    cursor++;
                }); break;}

                case "REWRITE" : {executions.put(command, com -> {
                    double value = Double.parseDouble(stack.pop());
                    String varName = stack.pop();
                    String collection = stack.pop();

                    ru.therealmone.spoStackMachine.HashMap col =
                            (ru.therealmone.spoStackMachine.HashMap) variables.get(collection);

                    try {
                        col.rewrite(varName, value);
                    } catch (NoSuchElementException e) {
                        e.message();
                        System.exit(1);
                    }

                    cursor++;
                }); break;}

                case "$" : {executions.put(command, com -> System.exit(0)); break;}
            }
        });
    }

    public void showVariables() {
        System.out.println("Current variables: ");
        variables.forEach( (name, obj) -> System.out.println(name + ": " + obj));
    }
}
