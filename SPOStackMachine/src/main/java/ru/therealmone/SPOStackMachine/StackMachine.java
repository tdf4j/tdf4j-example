package ru.therealmone.SPOStackMachine;

import com.opencsv.CSVReader;
import ru.therealmone.TranslatorAPI.Token;
import ru.therealmone.TranslatorAPI.Visitor;

import java.io.FileReader;
import java.util.Stack;

public class StackMachine implements Visitor {
    private HashMap variables;
    private HashMap ops;
    private Stack<String> stack = new Stack<>();

    @Override
    public void visit(Token token) {}

    @Override
    public void visit(String opn) {
        calculate(opn);
    }

    public StackMachine() {
        variables = new HashMap();
        ops = new HashMap();

        try {
            CSVReader csvReader = new CSVReader(new FileReader("context.csv"));
            String[] nextLine;
            csvReader.readNext();

            while((nextLine = csvReader.readNext()) != null) {
                try {
                    variables.add(nextLine[0], Integer.parseInt(nextLine[1]));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            csvReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        ops.add("/", 5);
        ops.add("*", 5);
        ops.add("+", 4);
        ops.add("-", 4);
        ops.add("<", 3);
        ops.add(">", 3);
        ops.add("<=", 3);
        ops.add(">=", 3);
        ops.add("==", 3);
        ops.add("&", 2);
        ops.add("|", 2);
        ops.add("^", 2);
    }

    private void calculate(String opn) {
        String[] str = opn.split(",");

        for (int i = 0; i < str.length; i++) {
            if (variables.contains(str[i])) {
                stack.push(str[i]);
                continue;
            }

            if(ops.contains(str[i])) {
                int op1;
                int op2;

                try {
                    op1 = Integer.parseInt(stack.peek());
                    stack.pop();
                } catch (Exception e) {
                    op1 = variables.get(stack.pop());
                }

                try {
                    op2 = Integer.parseInt(stack.peek());
                    stack.pop();
                } catch (Exception e) {
                    op2 = variables.get(stack.pop());
                }

                switch (str[i]) {
                    case "/" : {
                        stack.push(String.valueOf(op1 / op2));
                        break;
                    }

                    case "*" : {
                        stack.push(String.valueOf(op1 * op2));
                        break;
                    }

                    case "+" : {
                        stack.push(String.valueOf(op1 + op2));
                        break;
                    }

                    case "-" : {
                        stack.push(String.valueOf(op1 - op2));
                        break;
                    }

                    case "<" : {
                        stack.push(String.valueOf(op1 > op2));
                        break;
                    }

                    case ">" : {
                        stack.push(String.valueOf(op1 < op2));
                        break;
                    }

                    case ">=" : {
                        stack.push(String.valueOf(op1 <= op2));
                        break;
                    }

                    case "<=" : {
                        stack.push(String.valueOf(op1 >= op2));
                        break;
                    }

                    case "==" : {
                        stack.push(String.valueOf(op1 == op2));
                        break;
                    }
                }
                continue;
            }

            switch(str[i]) {
                case "&" : {
                    boolean op1 = Boolean.valueOf(stack.pop());
                    boolean op2 = Boolean.valueOf(stack.pop());
                    stack.push(String.valueOf(op1 && op2));
                    continue;
                }

                case "|" : {
                    boolean op1 = Boolean.valueOf(stack.pop());
                    boolean op2 = Boolean.valueOf(stack.pop());
                    stack.push(String.valueOf(op1 || op2));
                    continue;
                }

                case "^" : {
                    boolean op1 = Boolean.valueOf(stack.pop());
                    boolean op2 = Boolean.valueOf(stack.pop());
                    stack.push(String.valueOf(op1 ^ op2));
                    continue;
                }

                case "=" : {
                    int value;
                    try {
                        value = Integer.parseInt(stack.peek());
                        stack.pop();
                    } catch (Exception e) {
                        value = variables.get(stack.pop());
                    }
                    String var = stack.pop();
                    variables.rewrite(var, value);
                    stack.push(var);
                    continue;
                }
            }

            if(str[i].contains("!F@")) {
                i = Boolean.valueOf(stack.pop()) ? i : Integer.parseInt(str[i].substring(3, str[i].length())) - 1;
                continue;
            }

            if(str[i].contains("!@")) {
                i = Integer.parseInt(str[i].substring(2, str[i].length())) - 1;
                continue;
            }

            if(str[i].contains("!T@")) {
                i = !Boolean.valueOf(stack.pop()) ? i : Integer.parseInt(str[i].substring(3, str[i].length())) - 1;
                continue;
            }

            try{
                stack.push(String.valueOf(Integer.parseInt(str[i])));
            } catch (Exception e) {
                System.out.println("Unknown command: " + str[i]);
                System.exit(1);
            }
        }
    }

    public void showVariables() {
        System.out.println("Current variables: ");
        variables.show();
    }
}
