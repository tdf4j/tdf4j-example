package io.github.therealmone.spoParser;

import io.github.therealmone.core.beans.Lexeme;
import io.github.therealmone.core.utils.SavePrinter;
import io.github.therealmone.core.beans.Token;
import io.github.therealmone.spoParser.beans.ASLTreeNode;
import io.github.therealmone.spoParser.beans.LangRule;
import io.github.therealmone.spoParser.exceptions.UnexpectedTokenException;
import io.github.therealmone.core.exceptions.ParserException;
import io.github.therealmone.core.interfaces.Visitor;
import io.github.therealmone.spoParser.utils.RPNConverter;
import io.github.therealmone.spoParser.utils.RPNOptimizer;

import java.util.*;

//TODO: Реализовать оператор !
class ParserImpl implements Parser {

    private Stack<String> stack;
    private Stack<Integer> stackForCNReturns;
    private final AbstractParserConfig config;
    private final Set<Lexeme> terminals;
    private ASLTreeNode root;
    private ASLTreeNode currentTreeNode;
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
        this.stack = new Stack<>();
        this.stackForCNReturns = new Stack<>();
        this.history = new StringBuilder();
        this.config = new DefaultParserConfig();

        stack.push(config.getInitProduction());
        root = new ASLTreeNode(config.getInitProduction());
        currentTreeNode = root;
    }

    ParserImpl(final Set<Lexeme> terminals, AbstractParserConfig config) {
        this.terminals = terminals;
        this.stack = new Stack<>();
        this.stackForCNReturns = new Stack<>();
        this.history = new StringBuilder();
        this.config = config;

        stack.push(config.getInitProduction());
        root = new ASLTreeNode(config.getInitProduction());
        currentTreeNode = root;
    }

    @Override
    public void showLangRules() {
        SavePrinter.savePrintln("\u001B[33mLANG RULES:\u001B[0m");
        config.getLangRules().forEach((name, rule) -> SavePrinter.savePrintln(name + ": \n" + rule.toString()));

    }

    private void parse(final Token token) {
        while (!isTerminal(stack.peek())) {
            if (!stack.peek().equals("lang")) {
                for (final ASLTreeNode child : currentTreeNode.getChildes()) {
                    if (child.getName().equals(stack.peek()) && child.getChildes().size() == 0) {
                        currentTreeNode = child;
                        break;
                    }
                }
            }

            openNonTerminal(token);
        }

        if (stack.peek().equals(token.getType())) {
            for (final ASLTreeNode child : currentTreeNode.getChildes()) {
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
            currentTreeNode = (ASLTreeNode) currentTreeNode.getParent();
            moveCN();
        }
    }

    private void openNonTerminal(final Token token) {
        LangRule rule = config.getLangRules().get(stack.peek());

        if(rule == null) {
            throw new ParserException("Can't find rule: " + stack.peek());
        }

        stack.pop();
        String[] tmp = rule.getProductionValue(token.getType());
        stackForCNReturns.push(tmp.length);

        for (int i = 0; i < tmp.length; i++) {
            if(tmp[i].equals("ERROR")) {
                throw new UnexpectedTokenException(token, history.toString());
            } else if (!tmp[i].equals("EMPTY")) {
                stack.push(tmp[tmp.length - i - 1]);
                currentTreeNode.addChild(new ASLTreeNode(tmp[i], currentTreeNode));
            } else {
                moveCN();
            }
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
