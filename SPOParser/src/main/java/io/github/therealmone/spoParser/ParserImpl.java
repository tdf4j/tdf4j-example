package io.github.therealmone.spoParser;

import io.github.therealmone.translatorAPI.Beans.Lexeme;
import io.github.therealmone.translatorAPI.Utils.ResourceLoader;
import io.github.therealmone.translatorAPI.Utils.SavePrinter;
import io.github.therealmone.translatorAPI.Beans.Token;
import io.github.therealmone.spoParser.exceptions.UnexpectedTokenException;
import io.github.therealmone.translatorAPI.Exceptions.ParserException;
import io.github.therealmone.translatorAPI.Interfaces.Visitor;

import java.util.*;

//TODO: Реализовать оператор !
class ParserImpl implements Parser {

    private Stack<String> stack;
    private Stack<Integer> stackForCNReturns;
    private final Map<Integer, String[]> langRules;
    private final Map<String, Map<String, Integer>> analyzeTable;
    private final Set<Lexeme> terminals;
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
        v.visit(getRPN());
    }

    ParserImpl(final Set<Lexeme> terminals) {
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

    @Override
    public void showLangRules() {
        SavePrinter.savePrintln("\u001B[33mLANG RULES:\u001B[0m");
        langRules.forEach((num, rules) -> SavePrinter.savePrintf("%-20d%-10s%-40s%n", num, "-->", Arrays.toString(rules)));
    }

    private void parse(final Token token) {
        while (!isTerminal(stack.peek())) {
            if (!stack.peek().equals("lang")) {
                for (final TreeNode child : currentTreeNode.getChildes()) {
                    if (child.getName().equals(stack.peek()) && child.getChildes().size() == 0) {
                        currentTreeNode = child;
                        break;
                    }
                }
            }

            openNonTerminal(token);
        }

        if (stack.peek().equals(token.getType())) {
            for (final TreeNode child : currentTreeNode.getChildes()) {
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

    private void openNonTerminal(final Token token) {
        if(analyzeTable.get(stack.peek()) == null) {
            throw new ParserException("Can't find rule for " + stack.peek() + "(token = " + token.getType() + ")");
        }

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
    }

    @Override
    public List<String> getRPN() {
        return RPNOptimizer.optimize(RPNConverter.convertToRPN(root));
    }

    private boolean isTerminal(String lexeme) {
        for(Lexeme terminal : terminals) {
            if(terminal.getType().equals(lexeme)) {
                return true;
            }
        }

        return false;
    }
}
