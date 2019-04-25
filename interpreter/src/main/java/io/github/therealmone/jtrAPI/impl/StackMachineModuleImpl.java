package io.github.therealmone.jtrAPI.impl;

import io.github.therealmone.core.collections.Collection;
import io.github.therealmone.core.collections.arraylist.ArrayList;
import io.github.therealmone.core.collections.hashset.HashSet;
import io.github.therealmone.core.utils.Caster;
import io.github.therealmone.core.utils.SavePrinter;
import io.github.therealmone.jtrAPI.StackMachineModule;
import io.github.therealmone.stackmachine.StackMachine;
import io.github.therealmone.stackmachine.exceptions.NoVariableException;
import io.github.therealmone.stackmachine.exceptions.WrongTypeException;

public class StackMachineModuleImpl extends StackMachineModule {
    private Caster caster = new Caster();

    @Override
    public void configure() {
        //STRING
        addExecution("^\"(.*?)\"$", (command, context) -> {
            context.stackPush(command);
            context.increaseCursor();
        });

        //DIGIT
        addExecution("^-?(0|([1-9][0-9]*))$", (command, context) -> {
            context.stackPush(caster.castToDouble(command));
            context.increaseCursor();
        });

        //DOUBLE
        addExecution("^-?((0[.][0-9]*)|([1-9][0-9]*[.][0-9]*))$", (command, context) -> {
            context.stackPush(caster.castToDouble(command));
            context.increaseCursor();
        });

        //TAKE_VALUE
        addExecution("^%[a-z]+$", (command, context) -> {
            final String varName = command.substring(1);
            if (context.getVariables().containsKey(varName)) {
                String value = caster.autoCastToString(context.getVariables().get(varName));
                context.stackPush(value);
            } else {
                throw new NoVariableException("Can't find variable " + varName);
            }
            context.increaseCursor();
        });

        //TAKE_VAR_NAME
        addExecution("^#[a-z]+$", (command, context) -> {
            context.stackPush(command.substring(1));
            context.increaseCursor();
        });

        //GOTO_ON_FALSE
        addExecution("^!F@[0-9]+$", (command, context) -> {
            if (!caster.castToBoolean(context.stackPop())) {
                context.setCursor(caster.castToInt(command.substring(3)));
            } else {
                context.increaseCursor();
            }
        });

        //GOTO_ON_TRUE
        addExecution("^!T@[0-9]+$", (command, context) -> {
            if (caster.castToBoolean(context.stackPop())) {
                context.setCursor(caster.castToInt(command.substring(3)));
            } else {
                context.increaseCursor();
            }
        });

        //GOTO
        addExecution("^!@[0-9]+$", (command, context) -> {
            context.setCursor(caster.castToInt(command.substring(2)));
        });

        //DEL
        addExecution("^\\/$", (command, context) -> {
            double p2 = caster.castToDouble(context.stackPop());
            double p1 = caster.castToDouble(context.stackPop());
            context.stackPush(p1 / p2);
            context.increaseCursor();
        });

        //MUL
        addExecution("^div$", (command, context) -> {
            double p2 = caster.castToDouble(context.stackPop());
            double p1 = caster.castToDouble(context.stackPop());
            context.stackPush((int) (p1 / p2));
            context.increaseCursor();
        });

        //DIV
        addExecution("^mod$", (command, context) -> {
            double p2 = caster.castToDouble(context.stackPop());
            double p1 = caster.castToDouble(context.stackPop());
            context.stackPush(p1 % p2);
            context.increaseCursor();
        });

        //MOD
        addExecution("^\\*$", (command, context) -> {
            double p2 = caster.castToDouble(context.stackPop());
            double p1 = caster.castToDouble(context.stackPop());
            context.stackPush(p1 * p2);
            context.increaseCursor();
        });

        //PLUS
        addExecution("^\\+$", (command, context) -> {
            double p2 = caster.castToDouble(context.stackPop());
            double p1 = caster.castToDouble(context.stackPop());
            context.stackPush(p1 + p2);
            context.increaseCursor();
        });

        //CONCAT
        addExecution("^\\+\\+$", (command, context) -> {
            String p2 = caster.autoCastToString(context.stackPop()).replaceAll("\"", "");
            String p1 = caster.autoCastToString(context.stackPop()).replaceAll("\"", "");
            context.stackPush("\"" + (p1 + p2) + "\"");
            context.increaseCursor();
        });

        //MINUS
        addExecution("^\\-$", (command, context) -> {
            double p2 = caster.castToDouble(context.stackPop());
            double p1 = caster.castToDouble(context.stackPop());
            context.stackPush(p1 - p2);
            context.increaseCursor();
        });

        //LESS
        addExecution("^<$", (command, context) -> {
            double p2 = caster.castToDouble(context.stackPop());
            double p1 = caster.castToDouble(context.stackPop());
            context.stackPush(p1 < p2);
            context.increaseCursor();
        });

        //MORE
        addExecution("^>$", (command, context) -> {
            double p2 = caster.castToDouble(context.stackPop());
            double p1 = caster.castToDouble(context.stackPop());
            context.stackPush(p1 > p2);
            context.increaseCursor();
        });

        //LESS_OR_EQUALS
        addExecution("^<=$", (command, context) -> {
            double p2 = caster.castToDouble(context.stackPop());
            double p1 = caster.castToDouble(context.stackPop());
            context.stackPush(p1 <= p2);
            context.increaseCursor();
        });

        //MORE_OR_EQUALS
        addExecution("^>=$", (command, context) -> {
            double p2 = caster.castToDouble(context.stackPop());
            double p1 = caster.castToDouble(context.stackPop());
            context.stackPush(p1 >= p2);
            context.increaseCursor();
        });

        //EQUALS
        addExecution("^==$", (command, context) -> {
            double p2 = caster.castToDouble(context.stackPop());
            double p1 = caster.castToDouble(context.stackPop());
            context.stackPush(p1 == p2);
            context.increaseCursor();
        });

        //NOT_EQUALS
        addExecution("^!=$", (command, context) -> {
            double p2 = caster.castToDouble(context.stackPop());
            double p1 = caster.castToDouble(context.stackPop());
            context.stackPush(p1 == p2);
            context.increaseCursor();
        });

        //AND
        addExecution("^&$", (command, context) -> {
            boolean p2 = caster.castToBoolean(context.stackPop());
            boolean p1 = caster.castToBoolean(context.stackPop());
            context.stackPush(p1 && p2);
            context.increaseCursor();
        });

        //OR
        addExecution("^\\|&", (command, context) -> {
            boolean p2 = caster.castToBoolean(context.stackPop());
            boolean p1 = caster.castToBoolean(context.stackPop());
            context.stackPush(p1 || p2);
            context.increaseCursor();
        });

        //XOR
        addExecution("^\\^&", (command, context) -> {
            boolean p2 = caster.castToBoolean(context.stackPop());
            boolean p1 = caster.castToBoolean(context.stackPop());
            context.stackPush(p1 ^ p2);
            context.increaseCursor();
        });

        //ASSIGN
        addExecution("^=$", (command, context) -> {
            String parameter = caster.autoCastToString(context.stackPop());
            String varName = caster.autoCastToString(context.stackPop());
            if (context.getVariables().containsKey(varName))
                context.getVariables().replace(varName, caster.autoCastToObject(parameter));
            else {
                throw new NoVariableException("Can't find variable " + varName);
            }
            context.increaseCursor();
        });

        //NEW
        addExecution("^new$", (command, context) -> {
            context.getVariables().put(caster.autoCastToString(context.stackPeek()), null);
            context.increaseCursor();
        });

        //TYPEOF
        addExecution("^typeof$", (command, context) -> {
            String type = caster.autoCastToString(context.stackPop());
            switch (type) {
                case "hashset": {
                    context.getVariables().replace(
                            caster.autoCastToString(context.stackPop()),
                            HashSet.getInstance());
                    break;
                }
                case "arraylist": {
                    context.getVariables().replace(
                            caster.autoCastToString(context.stackPop()),
                            ArrayList.getInstance());
                    break;
                }
                default: {
                    throw new WrongTypeException("Unknown type " + type);
                }
            }
            context.increaseCursor();
        });

        //PUT
        addExecution("^put$", (command, context) -> {
            String parameter = caster.autoCastToString(context.stackPop());
            String collectionName = caster.autoCastToString(context.stackPop());
            Collection collection = getCollectionByName(collectionName, context);
            put(collection, parameter, context);
            context.increaseCursor();
        });

        //GET
        addExecution("^get$", (command, context) -> {
            final String parameter = caster.autoCastToString(context.stackPop());
            final String collectionName = caster.autoCastToString(context.stackPop());
            final Collection collection = getCollectionByName(collectionName, context);
            get(collection, parameter, context);
            context.increaseCursor();
        });

        //SIZE
        addExecution("^size$", (command, context) -> {
            final String collectionName = caster.autoCastToString(context.stackPop());
            final Collection collection = getCollectionByName(collectionName, context);
            size(collection, context);
            context.increaseCursor();
        });

        //REMOVE
        addExecution("^remove$", (command, context) -> {
            final String parameter = caster.autoCastToString(context.stackPop());
            final String collectionName = caster.autoCastToString(context.stackPop());
            final Collection collection = getCollectionByName(collectionName, context);
            remove(collection, parameter, context);
            context.increaseCursor();
        });

        //REWRITE
        addExecution("^rewrite$", (command, context) -> {
            final String parameter1 = caster.autoCastToString(context.stackPop());
            final String parameter0 = caster.autoCastToString(context.stackPop());
            final String collectionName = caster.autoCastToString(context.stackPop());
            final Collection collection = getCollectionByName(collectionName, context);
            rewrite(collection, context, parameter0, parameter1);
            context.increaseCursor();
        });

        //PRINT
        addExecution("^print$", (command, context) -> {
            print(caster.autoCastToString(context.stackPop()), context);
            context.increaseCursor();
        });
    }

    private void print(final String parameter, final StackMachine.Context context) {
        if(context.getVariables().containsKey(parameter)) {
            SavePrinter.savePrintln(context.getVariables().get(parameter).toString());
        } else if (parameter.matches("^\"(.*?)\"$")) {
            SavePrinter.savePrintln(parameter.replaceAll("\"", ""));
        } else {
            SavePrinter.savePrintln(String.valueOf(caster.castToDouble(getValueFromParameter(parameter, context))));
        }
    }

    private void put(final Collection collection, final String parameter, final StackMachine.Context context) {
        if (collection instanceof HashSet) {
            if (!context.getVariables().containsKey(parameter)) {
                throw new NoVariableException("Can't find variable: " + parameter);
            }
            ((HashSet) collection).add(parameter, (double) context.getVariables().get(parameter));
        } else if (collection instanceof ArrayList) {
            ((ArrayList) collection).add(caster.castToDouble(getValueFromParameter(parameter, context)));
        }
    }

    private void get(final Collection collection, final String parameter, final StackMachine.Context context) {
        if (collection instanceof HashSet) {
            context.stackPush(((HashSet) collection).get(parameter));
        } else if (collection instanceof ArrayList) {
            context.stackPush(((ArrayList) collection).get(getIndexFromParameter(parameter, context)));
        }
    }

    private void size(final Collection collection, final StackMachine.Context context) {
        context.stackPush(collection.size());
    }

    private void remove(final Collection collection, final String parameter, final StackMachine.Context context) {
        if (collection instanceof HashSet) {
            ((HashSet) collection).remove(parameter);
        } else if (collection instanceof ArrayList) {
            ((ArrayList) collection).remove(getIndexFromParameter(parameter, context));
        }
    }

    private void rewrite(final Collection collection, final StackMachine.Context context, final String... parameters) {
        if (collection instanceof HashSet) {
            ((HashSet) collection).rewrite(parameters[0], caster.castToDouble(getValueFromParameter(parameters[1], context)));
        } else if (collection instanceof ArrayList) {
            ((ArrayList) collection).rewrite(
                    getIndexFromParameter(parameters[0], context),
                    caster.castToDouble(getValueFromParameter(parameters[1], context))
            );
        }
    }

    private int getIndexFromParameter(final String parameter, final StackMachine.Context context) {
        if(context.getVariables().containsKey(parameter)) {
            if(!(context.getVariables().get(parameter) instanceof Double)) {
                throw new WrongTypeException("Wrong type of: " + parameter);
            }
            return caster.castToInt((Double) context.getVariables().get(parameter));
        } else {
            return caster.castToInt(parameter);
        }
    }

    private String getValueFromParameter(final String parameter, final StackMachine.Context context) {
        if(context.getVariables().containsKey(parameter)) {
            if(!(context.getVariables().get(parameter) instanceof Double)) {
                throw new WrongTypeException("Wrong type of: " + parameter);
            }
            return caster.autoCastToString(context.getVariables().get(parameter));
        } else {
            return caster.autoCastToString(parameter);
        }
    }

    private Collection getCollectionByName(final String collectionName, final StackMachine.Context context) {
        if(!(context.getVariables().get(collectionName) instanceof Collection)) {
            throw new WrongTypeException("Wrong type of: " + collectionName);
        }
        return (Collection) context.getVariables().get(collectionName);
    }
}
