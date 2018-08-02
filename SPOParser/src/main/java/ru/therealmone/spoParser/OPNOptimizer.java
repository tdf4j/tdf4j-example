package ru.therealmone.spoParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

//Есть смысл оптимизировать только математические операции, так как все остальные всегда требуют ссылку на переменную
final class OPNOptimizer {
    private static Stack<String> stack;
    private static final List<String> operatorsToOptimize;

    static {
        operatorsToOptimize = new ArrayList<String>() {{
            add("*");
            add("/");
            add("-");
            add("+");
        }};
    }

    static String optimize(String opn) {
        stack = new Stack<>();
        String[] commands = opn.split(",");

        for (String command : commands) {
            if (operatorsToOptimize.contains(command)) {
                optimizeOperand(command);
            } else {
                stack.push(command);
            }
        }

        return convertToString();
    }

    private static void optimizeOperand(String command) {
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
        }
    }

    private static String convertToString() {
        StringBuilder out = new StringBuilder();

        stack.forEach(command ->
                out.append(command).append(",")
        );

        out.deleteCharAt(out.length() - 1);

        return out.toString();
    }
}
