package io.github.therealmone.spoParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

//Есть смысл оптимизировать только математические операции, так как все остальные всегда требуют ссылку на переменную
final class RPNOptimizer {
    private static Stack<String> stack;
    private static final List<String> operatorsToOptimize;

    static {
        operatorsToOptimize = new ArrayList<String>() {{
            add("*");
            add("/");
            add("-");
            add("+");
            add("div");
            add("mod");
        }};
    }

    synchronized static List<String> optimize(final List<String> rpn) {
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

    private static void optimizeOperand(final String command) {
        switch (command) {

            case "*": {
                try {
                    double op2 = Double.parseDouble(stack.peek());
                    double op1 = Double.parseDouble(stack.get(stack.size() - 2));

                    stack.pop();
                    stack.pop();

                    stack.push("" + (op1 * op2));
                } catch (NumberFormatException e) {
                    stack.push(command);
                }

                break;
            }

            case "/": {
                try {
                    double op2 = Double.parseDouble(stack.peek());
                    double op1 = Double.parseDouble(stack.get(stack.size() - 2));

                    stack.pop();
                    stack.pop();

                    stack.push("" + (op1 / op2));
                } catch (NumberFormatException e) {
                    stack.push(command);
                }

                break;
            }

            case "-": {
                try {
                    double op2 = Double.parseDouble(stack.peek());
                    double op1 = Double.parseDouble(stack.get(stack.size() - 2));

                    stack.pop();
                    stack.pop();

                    stack.push("" + (op1 - op2));
                } catch (NumberFormatException e) {
                    stack.push(command);
                }

                break;
            }

            case "+": {
                try {
                    double op2 = Double.parseDouble(stack.peek());
                    double op1 = Double.parseDouble(stack.get(stack.size() - 2));

                    stack.pop();
                    stack.pop();

                    stack.push("" + (op1 + op2));
                } catch (NumberFormatException e) {
                    stack.push(command);
                }

                break;
            }

            case "div": {
                try {
                    double op2 = Double.parseDouble(stack.peek());
                    double op1 = Double.parseDouble(stack.get(stack.size() - 2));

                    stack.pop();
                    stack.pop();

                    stack.push("" + ((int) (op1 / op2)));
                } catch (NumberFormatException e) {
                    stack.push(command);
                }

                break;
            }

            case "mod": {
                try {
                    double op2 = Double.parseDouble(stack.peek());
                    double op1 = Double.parseDouble(stack.get(stack.size() - 2));

                    stack.pop();
                    stack.pop();

                    stack.push("" + (op1 % op2));
                } catch (NumberFormatException e) {
                    stack.push(command);
                }

                break;
            }
        }
    }
}
