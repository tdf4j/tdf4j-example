package ru.therealmone.SPOParser;

import com.opencsv.CSVReader;
import ru.therealmone.TranslatorAPI.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

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
    public void visit(String opn) {}

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
            System.out.println("Can't find file in: \n" + langRulesDir + "\n or: \n" + analyzeTableDir);
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void showLangRules() {
        System.out.println("LANG RULES; ");
        for(Map.Entry<Integer, String[]> entry: langRules.entrySet()) {
            System.out.println("Lang rule #" + entry.getKey() + ": " + Arrays.toString(entry.getValue()));
        }
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
                        System.out.println("Success " + token.getValue());
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
            System.exit(1);
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
            String[] tmp = langRules.get(analyzeTable.get(stack.pop()).get(token.getType()));
            stackForCNReturns.push(tmp.length);

            for (int i = 0; i < tmp.length; i++) {
                if(!tmp[i].equals("EMPTY")) {
                    stack.push(tmp[tmp.length - i - 1]);
                    currentTreeNode.addChild(new TreeNode(tmp[i], currentTreeNode));
                } else {
                    moveCN();
                }
            }

        } catch (NullPointerException e) {
            System.out.println("Seems like tables aren't complete. \nCan't find rule for " + peek + " (token = " + token.getType() + ").");
            e.printStackTrace();
            System.exit(1);
        }

    }

    public String getOPN() {
        return OPNConverter.convertToOPN(root);
    }
}
