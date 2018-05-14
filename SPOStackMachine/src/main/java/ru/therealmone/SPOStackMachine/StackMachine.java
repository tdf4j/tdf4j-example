package ru.therealmone.SPOStackMachine;

import ru.therealmone.TranslatorAPI.Token;
import ru.therealmone.TranslatorAPI.Visitor;

public class StackMachine implements Visitor {
    private HashMap variables;

    @Override
    public void visit(Token token) {}

    @Override
    public void visit(String opn) {
        calculate(opn);
    }

    public StackMachine() {
        variables = new HashMap();
    }

    private void calculate(String opn) {
        variables.add("y", 1);
        variables.add("x", 2);
        variables.add("z", 3);
        variables.add("n", 4);
        variables.add("a", 5);
        variables.add("b", 6);
        variables.add("o", 7);
        System.out.println(variables.get("a"));
    }
}
