package ru.therealmone.spoParser;

import com.opencsv.CSVReader;
import ru.therealmone.translatorAPI.*;
import ru.therealmone.spoParser.exceptions.UnexpectedTokenException;
import ru.therealmone.translatorAPI.Exceptions.ParserException;
import ru.therealmone.translatorAPI.Interfaces.Visitable;
import ru.therealmone.translatorAPI.Interfaces.Visitor;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

//TODO: Реализовать оператор !
public class Parser implements Visitor, Visitable {

    private Stack<String> stack = new Stack<>();
    private Stack<Integer> stackForCNReturns = new Stack<>();
    private Map<Integer, String[]> langRules = new HashMap<>();
    private Map<String, Map<String, Integer>> analyzeTable = new HashMap<>();
    private HashSet<String> terminals;
    private TreeNode root;
    private TreeNode currentTreeNode;
    private StringBuilder history = new StringBuilder();

    @Override
    public void visit(Token token) {
        parse(token);
    }

    @Override
    public void visit(String opn) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void accept(Visitor v) {
        v.visit(OPNConverter.convertToOPN(root));
    }

    public Parser(String langRulesDir, String analyzeTableDir, HashSet<String> terminals) {
        this.terminals = terminals;
        terminals.add("$");

        stack.push("lang");
        root = new TreeNode("lang");
        currentTreeNode = root;

        try {

            CSVReader csvReader = new CSVReader(new FileReader(langRulesDir));
            String[] nextLine;
            csvReader.readNext();

            while((nextLine = csvReader.readNext()) != null) {
                try {
                    langRules.put(Integer.parseInt(nextLine[0].trim()), nextLine[2].split("\\s\\+\\s"));
                } catch (NumberFormatException e) {
                        e.printStackTrace();
                }
            }

            csvReader.close();

            csvReader = new CSVReader(new FileReader(analyzeTableDir));
            String[] description = csvReader.readNext();

            while((nextLine = csvReader.readNext()) != null) {
                Map<String, Integer> tmp = new HashMap<>();
                for (int i = 1; i < nextLine.length; i++) {
                    tmp.put(description[i], Integer.parseInt(nextLine[i]));
                }
                analyzeTable.put(nextLine[0], tmp);
            }

            csvReader.close();

        } catch (FileNotFoundException e) {
            throw new ParserException("Can't find langRules.csv or analzeTable.csv", e);
        } catch (IOException e) {
            throw new ParserException("I/O exception", e);
        }
    }

    public void showLangRules() {
        System.out.println("\u001B[33mLANG RULES:\u001B[0m");
        langRules.forEach( (num, rules) -> System.out.printf("%-20d%-10s%-40s%n", num, "-->", Arrays.toString(rules)));
    }

    private void parse(Token token) {
        try {
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
                        //System.out.println("Success " + token.getValue());
                        history.append(token.getValue()).append(" ");
                        stack.pop();
                        break;
                    }
                }
            } else {
                throw new UnexpectedTokenException(token, stack.peek(), history.toString());
            }

        } catch (UnexpectedTokenException e) {
            e.message();
            throw new ParserException("Can't parse token " + token.getType() + " -> " + token.getValue(), e);
        }
    }

    private void moveCN() {
        int eleCount = stackForCNReturns.pop() - 1;
        if(eleCount != 0) {
            stackForCNReturns.push(eleCount);
        } else if(stackForCNReturns.size() != 0) {
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
        return OPNConverter.convertToOPN(root);
    }
}
