package ru.therealmone.spoStackMachine;

import com.opencsv.CSVReader;
import ru.therealmone.translatorAPI.Token;
import ru.therealmone.translatorAPI.Visitor;

import java.io.FileReader;
import ru.therealmone.translatorAPI.NoSuchElementException;
import ru.therealmone.translatorAPI.KeyAlreadyExistsException;

import java.io.IOException;
import java.util.Stack;

public class StackMachine implements Visitor {
    private HashMap variables;
    private Stack<String> stack = new Stack<>();

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

    public StackMachine(String contextDir) {
        variables = new HashMap();

        try {
            CSVReader csvReader = new CSVReader(new FileReader(contextDir));
            String[] nextLine;
            csvReader.readNext();

            while((nextLine = csvReader.readNext()) != null) {
                variables.add(nextLine[0], Integer.parseInt(nextLine[1]));
            }
            csvReader.close();

        } catch (KeyAlreadyExistsException e) {
            e.message();
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private void calculate(String opn) throws NumberFormatException {
        String[] str = opn.split(",");

        for (int i = 0; i < str.length; i++) {
            try {
                double tmp = Double.parseDouble(str[i]);
                stack.push("" + tmp);

            } catch (NumberFormatException e) {

                switch (str[i]) {
                    case "/": {
                        double p2 = Double.parseDouble(stack.pop());
                        double p1 = Double.parseDouble(stack.pop());
                        stack.push("" + (p1 / p2));
                        break;
                    }

                    case "*": {
                        double p2 = Double.parseDouble(stack.pop());
                        double p1 = Double.parseDouble(stack.pop());
                        stack.push("" + (p1 * p2));
                        break;
                    }

                    case "+": {
                        double p2 = Double.parseDouble(stack.pop());
                        double p1 = Double.parseDouble(stack.pop());
                        stack.push("" + (p1 + p2));
                        break;
                    }

                    case "-": {
                        double p2 = Double.parseDouble(stack.pop());
                        double p1 = Double.parseDouble(stack.pop());
                        stack.push("" + (p1 - p2));
                        break;
                    }

                    case "<": {
                        double p2 = Double.parseDouble(stack.pop());
                        double p1 = Double.parseDouble(stack.pop());
                        stack.push("" + (p1 < p2));
                        break;
                    }

                    case ">": {
                        double p2 = Double.parseDouble(stack.pop());
                        double p1 = Double.parseDouble(stack.pop());
                        stack.push("" + (p1 > p2));
                        break;
                    }

                    case "<=": {
                        double p2 = Double.parseDouble(stack.pop());
                        double p1 = Double.parseDouble(stack.pop());
                        stack.push("" + (p1 <= p2));
                        break;
                    }

                    case ">=": {
                        double p2 = Double.parseDouble(stack.pop());
                        double p1 = Double.parseDouble(stack.pop());
                        stack.push("" + (p1 >= p2));
                        break;
                    }

                    case "==": {
                        double p2 = Double.parseDouble(stack.pop());
                        double p1 = Double.parseDouble(stack.pop());
                        stack.push("" + (p1 == p2));
                        break;
                    }

                    case "&": {
                        boolean p2 = Boolean.parseBoolean(stack.pop());
                        boolean p1 = Boolean.parseBoolean(stack.pop());
                        stack.push("" + (p1 && p2));
                        break;
                    }

                    case "|": {
                        boolean p2 = Boolean.parseBoolean(stack.pop());
                        boolean p1 = Boolean.parseBoolean(stack.pop());
                        stack.push("" + (p1 || p2));
                        break;
                    }

                    case "^": {
                        boolean p2 = Boolean.parseBoolean(stack.pop());
                        boolean p1 = Boolean.parseBoolean(stack.pop());
                        stack.push("" + (p1 ^ p2));
                        break;
                    }

                    case "=": {
                        double p2 = Double.parseDouble(stack.pop());
                        String variable = stack.pop();
                        try {
                            variables.rewrite(variable, p2);
                        } catch (NoSuchElementException e1) {
                            e1.message();
                            System.exit(1);
                        }
                        break;
                    }

                    default: {
                        if (str[i].contains("!F@")) {
                            i = Boolean.valueOf(stack.pop()) ? i : Integer.parseInt(str[i].substring(3, str[i].length())) - 1;
                        } else if (str[i].contains("!@")) {
                            i = Integer.parseInt(str[i].substring(2, str[i].length())) - 1;
                        } else if (str[i].contains("!T@")) {
                            i = !Boolean.valueOf(stack.pop()) ? i : Integer.parseInt(str[i].substring(3, str[i].length())) - 1;
                        } else if (str[i].contains("#")) {
                            String tmp = str[i].substring(1, str[i].length());
                            stack.push(tmp);
                        } else if (!str[i].equals("$")) {
                            try {
                                stack.push("" + variables.get(str[i]));
                            } catch (NoSuchElementException e1) {
                                e1.message();
                                System.exit(1);
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    public void showVariables() {
        System.out.println("Current variables: ");
        variables.show();
    }
}
