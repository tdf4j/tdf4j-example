package io.github.therealmone.spoStackMachine;

import io.github.therealmone.spoStackMachine.collections.arraylist.ArrayList;
import io.github.therealmone.spoStackMachine.collections.Collection;
import io.github.therealmone.spoStackMachine.collections.hashset.HashSet;
import io.github.therealmone.spoStackMachine.exceptions.NoVariableException;
import io.github.therealmone.spoStackMachine.exceptions.UnknownCommandException;
import io.github.therealmone.spoStackMachine.exceptions.WrongTypeException;
import io.github.therealmone.translatorAPI.Beans.CommandBean;
import io.github.therealmone.translatorAPI.Utils.Caster;
import io.github.therealmone.translatorAPI.Utils.ResourceLoader;
import io.github.therealmone.translatorAPI.Utils.SavePrinter;

import java.util.*;

class StackMachineImpl implements StackMachine {
    private final Caster caster;
    private final Set<CommandBean> commands;
    private final Map<String, Command> executions;
    private Map<String, Object> variables;
    private Stack<String> stack;
    private int cursor;


    @Override
    public void visit(Object object) {
        if (object instanceof List) {
            calculate((List<String>) object);
        } else {
            throw new IllegalArgumentException("Illegal argument: " + object);
        }
    }

    StackMachineImpl() {
        caster = new Caster();
        commands = ResourceLoader.getCommands();
        executions = new HashMap<>();
        variables = new HashMap<>();
        stack = new Stack<>();
        cursor = 0;

        initExecutions();
    }

    private void calculate(final List<String> rpn) {
        while (!rpn.get(cursor).equals("$")) {
            executions.get(match(rpn.get(cursor))).execute(rpn.get(cursor));
        }
    }

    private String match(final String com) {
        for (CommandBean commandBean : commands) {
            if (commandBean.getPattern().matcher(com).matches()) {
                return commandBean.getType();
            }
        }

        throw new UnknownCommandException(com);
    }

    private void initExecutions() {
        commands.forEach(commandBean -> {
            switch (commandBean.getType()) {

                case "STRING": {
                    executions.put(commandBean.getType(), com -> {
                        stack.push(com);
                        cursor++;
                    });
                    break;
                }

                case "DIGIT": {
                    executions.put(commandBean.getType(), com -> {
                        stack.push("" + caster.castToDouble(com));
                        cursor++;
                    });

                    break;
                }

                case "DOUBLE": {
                    executions.put(commandBean.getType(), com -> {
                        stack.push("" + caster.castToDouble(com));
                        cursor++;
                    });

                    break;
                }

                case "TAKE_VALUE": {
                    executions.put(commandBean.getType(), com -> {
                        String varName = com.substring(1, com.length());

                        if (variables.containsKey(varName)) {
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

                case "TAKE_VAR_NAME": {
                    executions.put(commandBean.getType(), com -> {
                        stack.push(com.substring(1, com.length()));
                        cursor++;
                    });

                    break;
                }

                case "GOTO_ON_FALSE": {
                    executions.put(commandBean.getType(), com -> {
                        if (!caster.castToBoolean(stack.pop()))
                            cursor = caster.castToInt(com.substring(3, com.length()));
                        else
                            cursor++;
                    });

                    break;
                }

                case "GOTO_ON_TRUE": {
                    executions.put(commandBean.getType(), com -> {
                        if (caster.castToBoolean(stack.pop()))
                            cursor = caster.castToInt(com.substring(3, com.length()));
                        else
                            cursor++;
                    });

                    break;
                }

                case "GOTO": {
                    executions.put(commandBean.getType(), com ->
                            cursor = caster.castToInt(com.substring(2, com.length())));

                    break;
                }

                case "DEL": {
                    executions.put(commandBean.getType(), com -> {
                        double p2 = caster.castToDouble(stack.pop());
                        double p1 = caster.castToDouble(stack.pop());
                        stack.push("" + (p1 / p2));
                        cursor++;
                    });

                    break;
                }

                case "MUL": {
                    executions.put(commandBean.getType(), com -> {
                        double p2 = caster.castToDouble(stack.pop());
                        double p1 = caster.castToDouble(stack.pop());
                        stack.push("" + (p1 * p2));
                        cursor++;
                    });

                    break;
                }

                case "DIV": {
                    executions.put(commandBean.getType(), com -> {
                        double p2 = caster.castToDouble(stack.pop());
                        double p1 = caster.castToDouble(stack.pop());
                        stack.push("" + ((int) (p1 / p2)));
                        cursor++;
                    });

                    break;
                }

                case "MOD": {
                    executions.put(commandBean.getType(), com -> {
                        double p2 = caster.castToDouble(stack.pop());
                        double p1 = caster.castToDouble(stack.pop());
                        stack.push("" + (p1 % p2));
                        cursor++;
                    });

                    break;
                }

                case "PLUS": {
                    executions.put(commandBean.getType(), com -> {
                        double p2 = caster.castToDouble(stack.pop());
                        double p1 = caster.castToDouble(stack.pop());
                        stack.push("" + (p1 + p2));
                        cursor++;

                    });

                    break;
                }

                case "CONCAT": {
                    executions.put(commandBean.getType(), com -> {
                        String p2 = stack.pop().replaceAll("\"", "");
                        String p1 = stack.pop().replaceAll("\"", "");
                        stack.push("\"" + (p1 + p2) + "\"");
                        cursor++;

                    });

                    break;
                }

                case "MINUS": {
                    executions.put(commandBean.getType(), com -> {
                        double p2 = caster.castToDouble(stack.pop());
                        double p1 = caster.castToDouble(stack.pop());
                        stack.push("" + (p1 - p2));
                        cursor++;

                    });

                    break;
                }

                case "LESS": {
                    executions.put(commandBean.getType(), com -> {
                        double p2 = caster.castToDouble(stack.pop());
                        double p1 = caster.castToDouble(stack.pop());
                        stack.push("" + (p1 < p2));
                        cursor++;

                    });

                    break;
                }

                case "MORE": {
                    executions.put(commandBean.getType(), com -> {
                        double p2 = caster.castToDouble(stack.pop());
                        double p1 = caster.castToDouble(stack.pop());
                        stack.push("" + (p1 > p2));
                        cursor++;

                    });

                    break;
                }

                case "LESS_OR_EQUALS": {
                    executions.put(commandBean.getType(), com -> {
                        double p2 = caster.castToDouble(stack.pop());
                        double p1 = caster.castToDouble(stack.pop());
                        stack.push("" + (p1 <= p2));
                        cursor++;

                    });

                    break;
                }

                case "MORE_OR_EQUALS": {
                    executions.put(commandBean.getType(), com -> {
                        double p2 = caster.castToDouble(stack.pop());
                        double p1 = caster.castToDouble(stack.pop());
                        stack.push("" + (p1 >= p2));
                        cursor++;
                    });

                    break;
                }

                case "EQUALS": {
                    executions.put(commandBean.getType(), com -> {
                        double p2 = caster.castToDouble(stack.pop());
                        double p1 = caster.castToDouble(stack.pop());
                        stack.push("" + (p1 == p2));
                        cursor++;
                    });
                    break;
                }

                case "NOT_EQUALS": {
                    executions.put(commandBean.getType(), com -> {
                        double p2 = caster.castToDouble(stack.pop());
                        double p1 = caster.castToDouble(stack.pop());
                        stack.push("" + (p1 != p2));
                        cursor++;
                    });

                    break;
                }

                case "AND": {
                    executions.put(commandBean.getType(), com -> {
                        boolean p2 = caster.castToBoolean(stack.pop());
                        boolean p1 = caster.castToBoolean(stack.pop());
                        stack.push("" + (p1 && p2));
                        cursor++;

                    });

                    break;
                }

                case "OR": {
                    executions.put(commandBean.getType(), com -> {
                        boolean p2 = caster.castToBoolean(stack.pop());
                        boolean p1 = caster.castToBoolean(stack.pop());
                        stack.push("" + (p1 || p2));
                        cursor++;
                    });

                    break;
                }

                case "XOR": {
                    executions.put(commandBean.getType(), com -> {
                        boolean p2 = caster.castToBoolean(stack.pop());
                        boolean p1 = caster.castToBoolean(stack.pop());
                        stack.push("" + (p1 ^ p2));
                        cursor++;
                    });

                    break;
                }

                case "ASSIGN": {
                    executions.put(commandBean.getType(), com -> {
                        Double value = caster.castToDouble(stack.pop());
                        String varName = stack.pop();
                        if (variables.containsKey(varName))
                            variables.replace(varName, value);
                        else {
                            throw new NoVariableException("Can't find variable " + varName);
                        }

                        cursor++;
                    });

                    break;
                }

                case "NEW": {
                    executions.put(commandBean.getType(), com -> {
                        variables.put(stack.peek(), null);
                        cursor++;
                    });

                    break;
                }

                case "TYPEOF": {
                    executions.put(commandBean.getType(), com -> {
                        String type = stack.pop();
                        switch (type) {
                            case "hashset": {
                                variables.replace(
                                        stack.pop(),
                                        HashSet.getInstance());
                                break;
                            }
                            case "arraylist": {
                                variables.replace(
                                        stack.pop(),
                                        ArrayList.getInstance());
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

                case "PUT": {
                    executions.put(commandBean.getType(), com -> {
                        String parameter = stack.pop();
                        String collectionName = stack.pop();

                        Collection collection = getCollectionByName(collectionName);
                        put(collection, parameter);

                        cursor++;
                    });

                    break;
                }

                case "GET": {
                    executions.put(commandBean.getType(), com -> {
                        String parameter = stack.pop();
                        String collectionName = stack.pop();

                        Collection collection = getCollectionByName(collectionName);
                        get(collection, parameter);

                        cursor++;
                    });

                    break;
                }

                case "SIZE": {
                    executions.put(commandBean.getType(), com -> {
                        String collectionName = stack.pop();

                        Collection collection = getCollectionByName(collectionName);
                        size(collection);

                        cursor++;
                    });

                    break;
                }

                case "REMOVE": {
                    executions.put(commandBean.getType(), com -> {
                        String parameter = stack.pop();
                        String collectionName = stack.pop();

                        Collection collection = getCollectionByName(collectionName);
                        remove(collection, parameter);

                        cursor++;
                    });

                    break;
                }

                case "REWRITE": {
                    executions.put(commandBean.getType(), com -> {
                        String parameter1 = stack.pop();
                        String parameter0 = stack.pop();
                        String collectionName = stack.pop();

                        Collection collection = getCollectionByName(collectionName);
                        rewrite(collection, parameter0, parameter1);

                        cursor++;
                    });

                    break;
                }

                case "PRINT": {
                    executions.put(commandBean.getType(), com -> {
                        print(stack.pop());

                        cursor++;
                    });

                    break;
                }
            }
        });
    }

    @Override
    public void showVariables() {
        SavePrinter.savePrintln("Current variables: ");
        variables.forEach((name, obj) -> SavePrinter.savePrintln(name + ": " + obj));
    }

    private void print(final String parameter) {
        if(variables.containsKey(parameter)) {
            SavePrinter.savePrintln(variables.get(parameter).toString());
        } else if (parameter.matches("^\"(.*?)\"$")) {
            SavePrinter.savePrintln(parameter.replaceAll("\"", ""));
        } else {
            double value = getValueFromParameter(parameter);
            SavePrinter.savePrintln("" + value);
        }
    }

    private void put(final Collection collection, final String parameter) {
        if (collection instanceof HashSet) {
            if (!variables.containsKey(parameter)) {
                throw new NoVariableException("Can't find variable: " + parameter);
            }

            ((HashSet) collection).add(parameter, (double) variables.get(parameter));
        } else if (collection instanceof ArrayList) {
            double value = getValueFromParameter(parameter);

            ((ArrayList) collection).add(value);
        }
    }

    private void get(final Collection collection, final String parameter) {
        if (collection instanceof HashSet) {
            double value = ((HashSet) collection).get(parameter);
            stack.push("" + value);
        } else if (collection instanceof ArrayList) {
            int index = getIndexFromParameter(parameter);

            double value = ((ArrayList) collection).get(index);
            stack.push("" + value);
        }
    }

    private void size(final Collection collection) {
        stack.push("" + collection.size());
    }

    private void remove(final Collection collection, final String parameter) {
        if (collection instanceof HashSet) {
            ((HashSet) collection).remove(parameter);
        } else if (collection instanceof ArrayList) {
            int index = getIndexFromParameter(parameter);
            ((ArrayList) collection).remove(index);
        }
    }

    private void rewrite(final Collection collection, final String... parameters) {
        if (collection instanceof HashSet) {
            String varName = parameters[0];
            double value = getValueFromParameter(parameters[1]);

            ((HashSet) collection).rewrite(varName, value);
        } else if (collection instanceof ArrayList) {
            int index = getIndexFromParameter(parameters[0]);
            double value = getValueFromParameter(parameters[1]);

            ((ArrayList) collection).rewrite(index, value);
        }
    }

    private int getIndexFromParameter(final String parameter) {
        if(variables.containsKey(parameter)) {
            if(!(variables.get(parameter) instanceof Double)) {
                throw new WrongTypeException("Wrong type of: " + parameter);
            }

            return caster.castToInt((Double) variables.get(parameter));
        } else {
            return caster.castToInt(parameter);
        }
    }

    private double getValueFromParameter(final String parameter) {
        if(variables.containsKey(parameter)) {
            if(!(variables.get(parameter) instanceof Double)) {
                throw new WrongTypeException("Wrong type of: " + parameter);
            }

            return (Double) variables.get(parameter);
        } else {
            return caster.castToDouble(parameter);
        }
    }

    private Collection getCollectionByName(final String collectionName) {
        if(!(variables.get(collectionName) instanceof Collection)) {
            throw new WrongTypeException("Wrong type of: " + collectionName);
        }

        return (Collection) variables.get(collectionName);
    }

}
