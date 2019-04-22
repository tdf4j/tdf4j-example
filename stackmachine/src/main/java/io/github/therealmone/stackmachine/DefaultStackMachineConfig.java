package io.github.therealmone.stackmachine;

import io.github.therealmone.core.collections.Collection;
import io.github.therealmone.core.collections.arraylist.ArrayList;
import io.github.therealmone.core.collections.hashset.HashSet;
import io.github.therealmone.core.utils.Caster;
import io.github.therealmone.core.utils.SavePrinter;
import io.github.therealmone.stackmachine.exceptions.NoVariableException;
import io.github.therealmone.stackmachine.exceptions.WrongTypeException;

import java.util.HashMap;
import java.util.Map;

public class DefaultStackMachineConfig extends AbstractStackMachineConfig {
    Caster caster = new Caster();
    Map<String, Object> variables = new HashMap<>();

    @Override
    public void configure() {
        addExecution("^\"(.*?)\"$", command -> {
            stackPush(command);
            increaseCursor();
        });

        addExecution("^-?(0|([1-9][0-9]*))$", command -> {
            stackPush(caster.castToDouble(command));
            increaseCursor();
        });

        addExecution("^-?((0[.][0-9]*)|([1-9][0-9]*[.][0-9]*))$", command -> {
            stackPush(caster.castToDouble("command"));
            increaseCursor();
        });

        addExecution("^%[a-z]+$", command -> {
            String varName = command.substring(1);

            if (variables.containsKey(varName)) {
                String value = caster.autoCastToString(variables.get(varName));
                stackPush(value);
            } else {
                throw new NoVariableException("Can't find variable " + varName);
            }

            increaseCursor();
        });

        addExecution("^#[a-z]+$", command -> {
            stackPush(command.substring(1));
            increaseCursor();
        });

        addExecution("^!F@[0-9]+$", command -> {
            if (!caster.castToBoolean(stackPop()))
                setCursor(caster.castToInt(command.substring(3)));
            else
                increaseCursor();
        });

        addExecution("^!T@[0-9]+$", command -> {
            if (caster.castToBoolean(stackPop()))
                setCursor(caster.castToInt(command.substring(3)));
            else
                increaseCursor();
        });

        addExecution("^!@[0-9]+$", command -> {
            setCursor(caster.castToInt(command.substring(2)));
        });

        addExecution("^\\/$", command -> {
            double p2 = caster.castToDouble(stackPop());
            double p1 = caster.castToDouble(stackPop());
            stackPush(p1 / p2);
            increaseCursor();
        });

        addExecution("^div$", command -> {
            double p2 = caster.castToDouble(stackPop());
            double p1 = caster.castToDouble(stackPop());
            stackPush((int) (p1 / p2));
            increaseCursor();
        });

        addExecution("^mod$", command -> {
            double p2 = caster.castToDouble(stackPop());
            double p1 = caster.castToDouble(stackPop());
            stackPush(p1 % p2);
            increaseCursor();
        });

        addExecution("^\\*$", command -> {
            double p2 = caster.castToDouble(stackPop());
            double p1 = caster.castToDouble(stackPop());
            stackPush(p1 * p2);
            increaseCursor();
        });

        addExecution("^\\+$", command -> {
            double p2 = caster.castToDouble(stackPop());
            double p1 = caster.castToDouble(stackPop());
            stackPush(p1 + p2);
            increaseCursor();
        });

        addExecution("^\\+\\+$", command -> {
            String p2 = caster.autoCastToString(stackPop()).replaceAll("\"", "");
            String p1 = caster.autoCastToString(stackPop()).replaceAll("\"", "");
            stackPush("\"" + (p1 + p2) + "\"");
            increaseCursor();
        });

        addExecution("^\\-$", command -> {
            double p2 = caster.castToDouble(stackPop());
            double p1 = caster.castToDouble(stackPop());
            stackPush(p1 - p2);
            increaseCursor();
        });

        addExecution("^<$", command -> {
            double p2 = caster.castToDouble(stackPop());
            double p1 = caster.castToDouble(stackPop());
            stackPush(p1 < p2);
            increaseCursor();
        });

        addExecution("^>$", command -> {
            double p2 = caster.castToDouble(stackPop());
            double p1 = caster.castToDouble(stackPop());
            stackPush(p1 > p2);
            increaseCursor();
        });

        addExecution("^=$", command -> {
            double p2 = caster.castToDouble(stackPop());
            double p1 = caster.castToDouble(stackPop());
            stackPush(p1 <= p2);
            increaseCursor();
        });

        addExecution("^>=$", command -> {
            double p2 = caster.castToDouble(stackPop());
            double p1 = caster.castToDouble(stackPop());
            stackPush(p1 >= p2);
            increaseCursor();
        });

        addExecution("^==$", command -> {
            double p2 = caster.castToDouble(stackPop());
            double p1 = caster.castToDouble(stackPop());
            stackPush(p1 == p2);
            increaseCursor();
        });

        addExecution("^!=$", command -> {
            double p2 = caster.castToDouble(stackPop());
            double p1 = caster.castToDouble(stackPop());
            stackPush(p1 == p2);
            increaseCursor();
        });

        addExecution("^&$", command -> {
            boolean p2 = caster.castToBoolean(stackPop());
            boolean p1 = caster.castToBoolean(stackPop());
            stackPush(p1 && p2);
            increaseCursor();
        });

        addExecution("^\\|&", command -> {
            boolean p2 = caster.castToBoolean(stackPop());
            boolean p1 = caster.castToBoolean(stackPop());
            stackPush(p1 || p2);
            increaseCursor();
        });

        addExecution("^\\^&", command -> {
            boolean p2 = caster.castToBoolean(stackPop());
            boolean p1 = caster.castToBoolean(stackPop());
            stackPush(p1 ^ p2);
            increaseCursor();
        });

        addExecution("^=$", command -> {
            String parameter = caster.autoCastToString(stackPop());
            String varName = caster.autoCastToString(stackPop());
            if (variables.containsKey(varName))
                variables.replace(varName, caster.autoCastToObject(parameter));
            else {
                throw new NoVariableException("Can't find variable " + varName);
            }

            increaseCursor();
        });

        addExecution("^new$", command -> {
            variables.put(caster.autoCastToString(stackPeek()), null);
            increaseCursor();
        });

        addExecution("^typeof$", command -> {
            String type = caster.autoCastToString(stackPop());
            switch (type) {
                case "hashset": {
                    variables.replace(
                            caster.autoCastToString(stackPop()),
                            HashSet.getInstance());
                    break;
                }
                case "arraylist": {
                    variables.replace(
                            caster.autoCastToString(stackPop()),
                            ArrayList.getInstance());
                    break;
                }
                default: {
                    throw new WrongTypeException("Unknown type " + type);
                }
            }
            increaseCursor();
        });

        addExecution("^put$", command -> {
            String parameter = caster.autoCastToString(stackPop());
            String collectionName = caster.autoCastToString(stackPop());

            Collection collection = getCollectionByName(collectionName);
            put(collection, parameter);

            increaseCursor();
        });
    }

    private void print(final String parameter) {
        if(variables.containsKey(parameter)) {
            SavePrinter.savePrintln(variables.get(parameter).toString());
        } else if (parameter.matches("^\"(.*?)\"$")) {
            SavePrinter.savePrintln(parameter.replaceAll("\"", ""));
        } else {
            double value = caster.castToDouble(getValueFromParameter(parameter));
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
            double value = caster.castToDouble(getValueFromParameter(parameter));

            ((ArrayList) collection).add(value);
        }
    }

    private void get(final Collection collection, final String parameter) {
        if (collection instanceof HashSet) {
            double value = ((HashSet) collection).get(parameter);
            stackPush("" + value);
        } else if (collection instanceof ArrayList) {
            int index = getIndexFromParameter(parameter);

            double value = ((ArrayList) collection).get(index);
            stackPush("" + value);
        }
    }

    private void size(final Collection collection) {
        stackPush("" + collection.size());
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
            double value = caster.castToDouble(getValueFromParameter(parameters[1]));

            ((HashSet) collection).rewrite(varName, value);
        } else if (collection instanceof ArrayList) {
            int index = getIndexFromParameter(parameters[0]);
            double value = caster.castToDouble(getValueFromParameter(parameters[1]));

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

    private String getValueFromParameter(final String parameter) {
        if(variables.containsKey(parameter)) {
            if(!(variables.get(parameter) instanceof Double)) {
                throw new WrongTypeException("Wrong type of: " + parameter);
            }

            return caster.autoCastToString(variables.get(parameter));
        } else {
            return caster.autoCastToString(parameter);
        }
    }

    private Collection getCollectionByName(final String collectionName) {
        if(!(variables.get(collectionName) instanceof Collection)) {
            throw new WrongTypeException("Wrong type of: " + collectionName);
        }

        return (Collection) variables.get(collectionName);
    }
}
