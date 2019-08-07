package org.tdf4j.example.jtrAPI.utils;

import org.tdf4j.example.core.utils.Caster;

import java.util.*;

//Есть смысл оптимизировать только математические операции, так как все остальные всегда требуют ссылку на переменную
public final class RPNOptimizer {
    private Stack<String> stack;
    private final List<String> operatorsToOptimize;
    private final Caster caster;

    public RPNOptimizer() {
        caster = new Caster();
        operatorsToOptimize = List.of("*", "/", "-", "+", "div", "mod");
    }

    public List<String> optimize(final List<String> rpn) {
        stack = new Stack<>();

        for (final String command : rpn) {
            if (operatorsToOptimize.contains(command)) {
                optimizeOperand(command);
            } else {
                stack.push(command);
            }
        }

        return new ArrayList<>(stack);
    }

    private void optimizeOperand(final String command) {
        switch (command) {

            case "*": {
                if(caster.isNotDouble(stack.peek()) || caster.isNotDouble(stack.get(stack.size() - 2))) {
                    stack.push(command);
                } else {
                    double op2 = caster.castToDouble(stack.pop());
                    double op1 = caster.castToDouble(stack.pop());

                    stack.push("" + (op1 * op2));
                }

                break;
            }

            case "/": {
                if(caster.isNotDouble(stack.peek()) || caster.isNotDouble(stack.get(stack.size() - 2))) {
                    stack.push(command);
                } else {
                    double op2 = caster.castToDouble(stack.pop());
                    double op1 = caster.castToDouble(stack.pop());

                    stack.push("" + (op1 / op2));
                }

                break;
            }

            case "-": {
                if(caster.isNotDouble(stack.peek()) || caster.isNotDouble(stack.get(stack.size() - 2))) {
                    stack.push(command);
                } else {
                    double op2 = caster.castToDouble(stack.pop());
                    double op1 = caster.castToDouble(stack.pop());

                    stack.push("" + (op1 - op2));
                }

                break;
            }

            case "+": {
                if(caster.isNotDouble(stack.peek()) || caster.isNotDouble(stack.get(stack.size() - 2))) {
                    stack.push(command);
                } else {
                    double op2 = caster.castToDouble(stack.pop());
                    double op1 = caster.castToDouble(stack.pop());

                    stack.push("" + (op1 + op2));
                }

                break;
            }

            case "div": {
                if(caster.isNotDouble(stack.peek()) || caster.isNotDouble(stack.get(stack.size() - 2))) {
                    stack.push(command);
                } else {
                    double op2 = caster.castToDouble(stack.pop());
                    double op1 = caster.castToDouble(stack.pop());

                    stack.push("" + ((int) (op1 / op2)));
                }

                break;
            }

            case "mod": {
                if(caster.isNotDouble(stack.peek()) || caster.isNotDouble(stack.get(stack.size() - 2))) {
                    stack.push(command);
                } else {
                    double op2 = caster.castToDouble(stack.pop());
                    double op1 = caster.castToDouble(stack.pop());

                    stack.push("" + (op1 % op2));
                }

                break;
            }
        }
    }
}
