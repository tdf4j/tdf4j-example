package ru.therealmone.spoStackMachine;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ru.therealmone.spoStackMachine.collections.ArrayList;
import ru.therealmone.spoStackMachine.collections.Collection;
import ru.therealmone.spoStackMachine.collections.HashSet;
import ru.therealmone.spoStackMachine.collections.arraylist.ArrayListImpl;
import ru.therealmone.spoStackMachine.exceptions.NoVariableException;
import ru.therealmone.spoStackMachine.exceptions.WrongTypeException;
import ru.therealmone.spoStackMachine.exceptions.UnknownCommandException;
import ru.therealmone.spoStackMachine.collections.hashset.HashSetImpl;
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
    private Map<String, Pattern> commands;
    private Map<String, Command> executions;
    private Map<String, Object> variables;
    private Stack<String> stack;
    private int cursor;

    @Override
    public void visit(Token token) {}

    @Override
    public void visit(String opn) {
        calculate(opn);
    }

    public StackMachine(String commandsDir) {
        commands = new HashMap<>();
        executions = new HashMap<>();
        variables = new HashMap<>();
        stack = new Stack<>();
        cursor = 0;

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

    private void calculate(String opn) {
        String[] splittedOPN = opn.split(",");

        while (!splittedOPN[cursor].equals("$")) {
            executions.get(match(splittedOPN[cursor])).execute(splittedOPN[cursor]);
        }
    }

    private String match(String com) {
        try {
            for (Map.Entry<String, Pattern> entry : commands.entrySet()) {
                Matcher m = entry.getValue().matcher(com);
                if (m.matches()) {
                    return entry.getKey();
                }
            }
            throw new UnknownCommandException(com);
        } catch (UnknownCommandException e) {
            throw new StackMachineException("Unknown command", e);
        }
    }

    private void initExecutions() {
        commands.forEach( (command, pattern) -> {
            switch (command) {

                case "DIGIT" : {
                    executions.put(command, com -> {
                        stack.push("" + Double.parseDouble(com));
                        cursor++;
                    });

                    break;
                }

                case "DOUBLE" : {
                    executions.put(command, com -> {
                        stack.push("" + Double.parseDouble(com));
                        cursor++;
                    });

                    break;
                }

                case "TAKE_VALUE" : {
                    executions.put(command, com -> {
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
                    });

                    break;
                }

                case "TAKE_VAR_NAME" : {
                    executions.put(command, com -> {
                        stack.push(com.substring(1, com.length()));
                        cursor++;
                    });

                    break;
                }

                case "GOTO_ON_FALSE" : {
                    executions.put(command, com -> {
                        if(!Boolean.parseBoolean(stack.pop()))
                            cursor = Integer.parseInt(com.substring(3, com.length()));
                        else
                            cursor++;
                    });

                    break;
                }

                case "GOTO_ON_TRUE" : {
                    executions.put(command, com -> {
                        if(Boolean.parseBoolean(stack.pop()))
                            cursor = Integer.parseInt(com.substring(3, com.length()));
                        else
                            cursor++;
                    });

                    break;
                }

                case "GOTO" : {
                    executions.put(command, com ->
                        cursor = Integer.parseInt(com.substring(2, com.length())));

                    break;
                }

                case "DEL" : {
                    executions.put(command, com -> {
                        double p2 = Double.parseDouble(stack.pop());
                        double p1 = Double.parseDouble(stack.pop());
                        stack.push("" + (p1 / p2));
                        cursor++;
                    });

                    break;
                }

                case "MUL" : {
                    executions.put(command, com -> {
                        double p2 = Double.parseDouble(stack.pop());
                        double p1 = Double.parseDouble(stack.pop());
                        stack.push("" + (p1 * p2));
                        cursor++;
                    });

                    break;
                }

                case "PLUS" : {
                    executions.put(command, com -> {
                        double p2 = Double.parseDouble(stack.pop());
                        double p1 = Double.parseDouble(stack.pop());
                        stack.push("" + (p1 + p2));
                        cursor++;

                    });

                    break;
                }

                case "MINUS" : {
                    executions.put(command, com -> {
                        double p2 = Double.parseDouble(stack.pop());
                        double p1 = Double.parseDouble(stack.pop());
                        stack.push("" + (p1 - p2));
                        cursor++;

                    });

                    break;
                }

                case "LESS" : {
                    executions.put(command, com -> {
                        double p2 = Double.parseDouble(stack.pop());
                        double p1 = Double.parseDouble(stack.pop());
                        stack.push("" + (p1 < p2));
                        cursor++;

                    });

                    break;
                }

                case "MORE" : {
                    executions.put(command, com -> {
                        double p2 = Double.parseDouble(stack.pop());
                        double p1 = Double.parseDouble(stack.pop());
                        stack.push("" + (p1 > p2));
                        cursor++;

                    });

                    break;
                }

                case "LESS_OR_EQUALS" : {
                    executions.put(command, com -> {
                        double p2 = Double.parseDouble(stack.pop());
                        double p1 = Double.parseDouble(stack.pop());
                        stack.push("" + (p1 <= p2));
                        cursor++;

                    });

                    break;
                }

                case "MORE_OR_EQUALS" : {
                    executions.put(command, com -> {
                        double p2 = Double.parseDouble(stack.pop());
                        double p1 = Double.parseDouble(stack.pop());
                        stack.push("" + (p1 >= p2));
                        cursor++;
                    });

                    break;
                }

                case "EQUALS" : {
                    executions.put(command, com -> {
                        double p2 = Double.parseDouble(stack.pop());
                        double p1 = Double.parseDouble(stack.pop());
                        stack.push("" + (p1 == p2));
                        cursor++;
                    });
                    break;
                }

                case "NOT_EQUALS" : {
                    executions.put(command, com -> {
                        double p2 = Double.parseDouble(stack.pop());
                        double p1 = Double.parseDouble(stack.pop());
                        stack.push("" + (p1 != p2));
                        cursor++;
                    });

                    break;
                }

                case "AND" : {
                    executions.put(command, com -> {
                        boolean p2 = Boolean.parseBoolean(stack.pop());
                        boolean p1 = Boolean.parseBoolean(stack.pop());
                        stack.push("" + (p1 && p2));
                        cursor++;

                    });

                    break;
                }

                case "OR" : {
                    executions.put(command, com -> {
                        boolean p2 = Boolean.parseBoolean(stack.pop());
                        boolean p1 = Boolean.parseBoolean(stack.pop());
                        stack.push("" + (p1 || p2));
                        cursor++;
                    });

                    break;
                }

                case "XOR" : {
                    executions.put(command, com -> {
                        boolean p2 = Boolean.parseBoolean(stack.pop());
                        boolean p1 = Boolean.parseBoolean(stack.pop());
                        stack.push("" + (p1 ^ p2));
                        cursor++;
                    });

                    break;
                }

                case "ASSIGN" : {
                    executions.put(command, com -> {
                        Double value = Double.parseDouble(stack.pop());
                        String varName = stack.pop();
                        if(variables.containsKey(varName))
                            variables.replace(varName, value);
                        else {
                            throw new NoVariableException("Can't find variable " + varName);
                        }

                        cursor++;
                    });

                    break;
                }

                case "NEW" : {
                    executions.put(command, com -> {
                        variables.put(stack.peek(), null);
                        cursor++;
                    });

                    break;
                }

                case "TYPEOF" : {
                    executions.put(command, com -> {
                        String type = stack.pop();
                        switch (type) {
                            case "hashset" : {
                                variables.replace(
                                        stack.pop(),
                                        new HashSetImpl());
                                break;
                            }
                            case "arraylist" : {
                                variables.replace(
                                        stack.pop(),
                                        new ArrayListImpl());
                                break;
                            }
                            default: {
                                throw new WrongTypeException("Unknown type " + type);
                            }
                        }
                        cursor++;
                    });

                    break;
                }

                case "PUT" : {
                    executions.put(command, com -> {
                        String parameter = stack.pop();
                        String collectionName = stack.pop();

                        Collection collection = (Collection) variables.get(collectionName);
                        put(collection, parameter);

                        cursor++;
                    });

                    break;
                }

                case "GET" : {
                    executions.put(command, com -> {
                        String parameter = stack.pop();
                        String collectionName = stack.pop();

                        Collection collection = (Collection) variables.get(collectionName);
                        get(collection, parameter);

                        cursor++;
                    });

                    break;
                }

                case "REMOVE" : {
                    executions.put(command, com -> {
                        String parameter = stack.pop();
                        String collectionName = stack.pop();

                        Collection collection = (Collection) variables.get(collectionName);
                        remove(collection, parameter);

                        cursor++;
                    });

                    break;
                }

                case "REWRITE" : {
                    executions.put(command, com -> {
                        String parameter1 = stack.pop();
                        String parameter0 = stack.pop();
                        String collectionName = stack.pop();

                        Collection collection = (Collection) variables.get(collectionName);
                        rewrite(collection, parameter0, parameter1);

                        cursor++;
                    });

                    break;
                }

                case "PRINT" : {
                    executions.put(command, com -> {
                        double value = Double.parseDouble(stack.pop());
                        System.out.println(value);

                        cursor++;
                    });

                    break;
                }

                case "$" : {executions.put(command, com -> System.out.println("EXECUTION SUCCESS")); break;}
            }
        });
    }

    public void showVariables() {
        System.out.println("Current variables: ");
        variables.forEach( (name, obj) -> System.out.println(name + ": " + obj));
    }

    private void put(Collection collection, String parameter) {
        if (collection instanceof HashSet) {
            if(!variables.containsKey(parameter)) {
                throw new NoVariableException("Can't find variable: " + parameter);
            }

            ((HashSet) collection).add(parameter, (double) variables.get(parameter));
        }
        else if (collection instanceof ArrayList) {
            double value = getValueFromParameter(parameter);

            ((ArrayList) collection).add(value);
        }
    }

    private void get(Collection collection, String parameter) {
        if (collection instanceof HashSet) {
            double value = ((HashSet) collection).get(parameter);
            stack.push("" + value);
        }
        else if (collection instanceof ArrayList) {
            int index = getIndexFromParameter(parameter);

            double value = ((ArrayList) collection).get(index);
            stack.push("" + value);
        }
    }

    private void remove(Collection collection, String parameter) {
        if (collection instanceof HashSet) {
            ((HashSet) collection).remove(parameter);
        }
        else if(collection instanceof ArrayList) {
            int index = getIndexFromParameter(parameter);
            ((ArrayList) collection).remove(index);
        }
    }

    private void rewrite(Collection collection, String ... parameters) {
        if(collection instanceof HashSet) {
            String varName = parameters[0];
            double value = getValueFromParameter(parameters[1]);

            ((HashSet) collection).rewrite(varName, value);
        }
        else if(collection instanceof ArrayList) {
            int index = getIndexFromParameter(parameters[0]);
            double value = getValueFromParameter(parameters[1]);

            ((ArrayList) collection).rewrite(index, value);
        }
    }

    private int getIndexFromParameter(String parameter) {
        int index;

        try {
            if (variables.containsKey(parameter)) {
                index = ((Double) variables.get(parameter)).intValue();
            } else {
                index = ((Double) Double.parseDouble(parameter)).intValue();
            }
        } catch (NumberFormatException | ClassCastException e) {
            throw new WrongTypeException("Wrong type of: " + parameter, e);
        }

        return index;
    }

    private double getValueFromParameter(String parameter) {
        double value;

        try {
            value = Double.parseDouble(parameter);
        } catch (NumberFormatException e) {
            throw new WrongTypeException("Wrong type of: " + parameter, e);
        }

        return value;
    }
}
