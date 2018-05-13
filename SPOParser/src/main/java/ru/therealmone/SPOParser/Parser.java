package ru.therealmone.SPOParser;

import com.opencsv.CSVReader;
import ru.therealmone.TranslatorAPI.Token;
import ru.therealmone.TranslatorAPI.Visitor;
import ru.therealmone.TranslatorAPI.Node;

import java.io.FileReader;
import java.util.*;

public class Parser implements Visitor {

    private Stack<String> stack = new Stack<>();
    private Stack<Integer> stackForCNReturns = new Stack<>();
    private Map<Integer, String[]> langRules = new HashMap<>();
    private Map<String, Map<String, Integer>> analyzeTable = new HashMap<>();
    private HashSet<String> terminals;
    public boolean ERROR = false;
    private Node root;
    private Node currentNode;

    @Override
    public void visit(Token token) {
        ERROR = !parse(token);
    }

    @Override
    public void visit(Node root) {}

    public Parser(HashSet<String> terminals) {
        this.terminals = terminals;
        terminals.add("$");

        stack.push("lang");
        root = new Node("lang");
        currentNode = root;

        try {
            CSVReader csvReader = new CSVReader(new FileReader("D:/JavaProjects/SPOTranslator/SPOParser/src/main/resources/langRules.csv"));
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

            csvReader = new CSVReader(new FileReader("D:/JavaProjects/SPOTranslator/SPOParser/src/main/resources/analyzeTable.csv"));
            String[] description = csvReader.readNext();

            while((nextLine = csvReader.readNext()) != null) {
                Map<String, Integer> tmp = new HashMap<>();
                for (int i = 1; i < nextLine.length; i++) {
                    tmp.put(description[i], Integer.parseInt(nextLine[i]));
                }
                analyzeTable.put(nextLine[0], tmp);
            }
            csvReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("----------------------------------PARSER----------------------------------------");
        showLangRules();
        System.out.println("--------------------------------------------------------------------------------");
    }

    public void showLangRules() {
        System.out.println("LANG RULES; ");
        for(Map.Entry<Integer, String[]> entry: langRules.entrySet()) {
            System.out.println("Lang rule #" + entry.getKey() + ": " + Arrays.toString(entry.getValue()));
        }
    }

    private boolean parse(Token token) {
        while(!terminals.contains(stack.peek())) {

            if(!stack.peek().equals("lang")) {
                for(Node child: currentNode.getChildes()) {
                    if(child.getValue().equals(stack.peek()) && child.getChildes().size() == 0) {
                        currentNode = child;
                        break;
                    }
                }
            }
            openNonTerminal(token);
        }

        if(stack.peek().equals(token.getType())) {
            for(Node child: currentNode.getChildes()) {
                if(child.getValue().equals(token.getType()) && child.getToken() == null) {
                    moveCN();
                    child.setToken(token);
                    System.out.println("Success " + token.getValue());
                    stack.pop();
                    return true;
                }
            }
        }
        return false;
    }

    private void moveCN() {
        int eleCount = stackForCNReturns.pop() - 1;
        if(eleCount != 0) {
            stackForCNReturns.push(eleCount);
        } else if(stackForCNReturns.size() != 0) {
            currentNode = currentNode.getParent();
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
                    currentNode.addChild(new Node(tmp[i], currentNode));
                } else {
                    moveCN();
                }
            }

        } catch (NullPointerException e) {
            System.out.println("Seems like tables aren't complete \nCan't find rule for " + peek + " (token = " + token.getType() + ")");
            e.printStackTrace();
            System.exit(1);
        }

    }

    public Node getRoot() {
        return this.root;
    }
}
