package ru.therealmone.spoParser;

import ru.therealmone.translatorAPI.ResourceLoader;
import ru.therealmone.translatorAPI.*;
import ru.therealmone.spoParser.exceptions.UnexpectedTokenException;
import ru.therealmone.translatorAPI.Exceptions.ParserException;
import ru.therealmone.translatorAPI.Interfaces.Visitable;
import ru.therealmone.translatorAPI.Interfaces.Visitor;

import java.util.*;

//TODO: Реализовать оператор !
public class Parser implements Visitor, Visitable {

    private Stack<String> stack;
    private Stack<Integer> stackForCNReturns;
    private final Map<Integer, String[]> langRules;
    private final Map<String, Map<String, Integer>> analyzeTable;
    private final HashSet<String> terminals;
    private TreeNode root;
    private TreeNode currentTreeNode;
    private StringBuilder history;

    @Override
    public void visit(Object object) {
        if (object instanceof Token) {
            parse((Token) object);
        } else {
            throw new IllegalArgumentException("Illegal argument: " + object);
        }
    }

    @Override
    public void accept(Visitor v) {
        v.visit(getOPN());
    }

    public Parser(final HashSet<String> terminals) {
        this.terminals = terminals;

        stack = new Stack<>();
        stackForCNReturns = new Stack<>();
        langRules = ResourceLoader.getLangRules();
        analyzeTable = ResourceLoader.getAnalyzeTable();
        history = new StringBuilder();

        stack.push("lang");
        root = new TreeNode("lang");
        currentTreeNode = root;
    }

    public void showLangRules() {
        SavePrinter.savePrintln("\u001B[33mLANG RULES:\u001B[0m");
        langRules.forEach((num, rules) -> SavePrinter.savePrintf("%-20d%-10s%-40s%n", num, "-->", Arrays.toString(rules)));
    }

    private void parse(Token token) {
        while (!terminals.contains(stack.peek())) {
            if (!stack.peek().equals("lang")) {
                for (TreeNode child : currentTreeNode.getChildes()) {
                    if (child.getName().equals(stack.peek()) && child.getChildes().size() == 0) {
                        currentTreeNode = child;
                        break;
                    }
                }
            }

            openNonTerminal(token);
        }

        if (stack.peek().equals(token.getType())) {
            for (TreeNode child : currentTreeNode.getChildes()) {
                if (child.getName().equals(token.getType()) && child.getToken() == null) {
                    moveCN();
                    child.setToken(token);
                    history.append(token.getValue()).append(" ");
                    stack.pop();
                    break;
                }
            }
        } else {
            throw new UnexpectedTokenException(token, stack.peek(), history.toString());
        }
    }

    private void moveCN() {
        int eleCount = stackForCNReturns.pop() - 1;
        if (eleCount != 0) {
            stackForCNReturns.push(eleCount);
        } else if (stackForCNReturns.size() != 0) {
            currentTreeNode = (TreeNode) currentTreeNode.getParent();
            moveCN();
        }
    }

    private void openNonTerminal(Token token) {
        String peek = stack.peek();

        try {
            int ruleNumber = analyzeTable.get(stack.pop()).get(token.getType());

            if (ruleNumber != 0) {
                String[] tmp = langRules.get(ruleNumber);
                stackForCNReturns.push(tmp.length);

                for (int i = 0; i < tmp.length; i++) {
                    if (!tmp[i].equals("EMPTY")) {
                        stack.push(tmp[tmp.length - i - 1]);
                        currentTreeNode.addChild(new TreeNode(tmp[i], currentTreeNode));
                    } else {
                        moveCN();
                    }
                }

            } else {
                throw new UnexpectedTokenException(token, history.toString());
            }

        } catch (NullPointerException e) {
            throw new ParserException("Can't find rule for " + peek + "(token = " + token.getType() + ")", e);
        }
    }

    public String getOPN() {
        return OPNOptimizer.optimize(OPNConverter.convertToOPN(root));
    }
}
