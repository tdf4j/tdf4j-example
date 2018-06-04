package ru.therealmone.spoStackMachine;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ru.therealmone.spoStackMachine.exceptions.NoVariableException;
import ru.therealmone.spoStackMachine.exceptions.WrongTypeException;
import ru.therealmone.spoStackMachine.exceptions.UnknownCommandException;
import ru.therealmone.translatorAPI.Exceptions.StackMachineException;
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
    public void visit(String opn) throws StackMachineException {
        try {
            calculate(opn);
        } catch (NumberFormatException e) {
            throw new StackMachineException("Unexpected value type " + stack.peek(), e);
        }
    }

    public StackMachine(String commandsDir) throws StackMachineException {
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
            throw new StackMachineException("Can't find commands.xml file", e);
        } catch (ParserConfigurationException e) {
            throw new StackMachineException("DocumentBuilder cannot be created which satisfies the configuration requested", e);
        } catch(SAXException e) {
            throw new StackMachineException("XML parse error", e);
        } catch (IOException e) {
            throw new StackMachineException("I/O exception", e);
        }

        initExecutions();
    }

    private void calculate(String opn) throws StackMachineException {
        String[] splittedOPN = opn.split(",");

        while (!splittedOPN[cursor].equals("$")) {
            executions.get(match(splittedOPN[cursor])).execute(splittedOPN[cursor]);
        }
    }

    private String match(String com) throws StackMachineException {
        try {
            for (Map.Entry<String, Pattern> entry : commands.entrySet()) {
                Matcher m = entry.getValue().matcher(com);
                if (m.matches())
                    return entry.getKey();
            }
            throw new UnknownCommandException(com);
        } catch (UnknownCommandException e) {
            throw new StackMachineException("Unknown command", e);
        }
    }

    private void initExecutions() {
        commands.forEach( (command, pattern) -> {
            switch (command) {

                case "DIGIT" : {executions.put(command, com -> {
                    stack.push("" + Double.parseDouble(com));
                    cursor++;
                }); break;}

                case "DOUBLE" : {executions.put(command, com -> {
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
                            throw new WrongTypeException("Wrong type of " + varName);
                        }
                    } else {
                        throw new NoVariableException("Can't find variable " + varName);
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

                case "NOT_EQUALS" : {executions.put(command, com -> {
                    double p2 = Double.parseDouble(stack.pop());
                    double p1 = Double.parseDouble(stack.pop());
                    stack.push("" + (p1 != p2));
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
                        throw new NoVariableException("Can't find variable " + varName);
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
                                    new ru.therealmone.spoStackMachine.hashmap.HashMap());
                            break;
                        }
                        default: {
                            throw new WrongTypeException("Unknown type " + type);
                        }
                    }
                    cursor++;
                }); break;}

                case "PUT" : {executions.put(command, com -> {
                    String varName = stack.pop();
                    String collection = stack.pop();
                    ru.therealmone.spoStackMachine.hashmap.HashMap hashMap;
                    Double var;

                    if(variables.containsKey(varName)) {
                        if (variables.get(varName) instanceof Double) {
                            var = (Double) variables.get(varName);
                        } else {
                            throw new WrongTypeException("Wrong type of " + varName);
                        }
                    } else {
                        throw new NoVariableException("Can't find variable " + varName);
                    }

                    try {
                        hashMap = (ru.therealmone.spoStackMachine.hashmap.HashMap) variables.get(collection);
                    } catch (ClassCastException e) {
                        throw new WrongTypeException("Wrong type of " + collection);
                    }

                    try {
                        hashMap.add(varName, var);
                    } catch (NullPointerException e) {
                        throw new NoVariableException("Can't find variable " + collection, e);
                    }

                    cursor++;
                }); break;}

                case "GET" : {executions.put(command, com -> {
                    String varName = stack.pop();
                    String collection = stack.pop();
                    ru.therealmone.spoStackMachine.hashmap.HashMap hashMap;

                    try {
                        hashMap = (ru.therealmone.spoStackMachine.hashmap.HashMap) variables.get(collection);
                    } catch (ClassCastException e) {
                        throw new WrongTypeException("Wrong type of " + collection);
                    }

                    try {
                        stack.push("" + hashMap.get(varName));
                    } catch (NullPointerException e) {
                        throw new NoVariableException("Can't find variable " + collection, e);
                    }

                    cursor++;
                }); break;}

                case "REMOVE" : {executions.put(command, com -> {
                    String varName = stack.pop();
                    String collection = stack.pop();

                    ru.therealmone.spoStackMachine.hashmap.HashMap hashMap =
                            (ru.therealmone.spoStackMachine.hashmap.HashMap) variables.get(collection);

                    try {
                        hashMap.remove(varName);
                    } catch (NullPointerException e) {
                        throw new NoVariableException("Can't find variable " + collection, e);
                    }

                    cursor++;
                }); break;}

                case "REWRITE" : {executions.put(command, com -> {
                    double value = Double.parseDouble(stack.pop());
                    String varName = stack.pop();
                    String collection = stack.pop();

                    ru.therealmone.spoStackMachine.hashmap.HashMap col =
                            (ru.therealmone.spoStackMachine.hashmap.HashMap) variables.get(collection);

                    try {
                        col.rewrite(varName, value);
                    } catch (NullPointerException e) {
                        throw new NoVariableException("Can't find variable " + collection, e);
                    }

                    cursor++;
                }); break;}

                case "$" : {executions.put(command, com -> System.out.println("EXECUTION SUCCESS")); break;}
            }
        });
    }

    public void showVariables() {
        System.out.println("Current variables: ");
        variables.forEach( (name, obj) -> System.out.println(name + ": " + obj));
    }
}
