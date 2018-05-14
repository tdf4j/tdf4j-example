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
        for (int i = 0; i < 8; i++) {
            variables.add("var" + i + "able" + i, i);
        }
        System.out.println(variables.get("var5able5"));
    }
}
