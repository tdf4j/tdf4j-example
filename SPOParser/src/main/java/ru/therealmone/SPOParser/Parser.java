package ru.therealmone.SPOParser;

import com.opencsv.CSVReader;
import ru.therealmone.TranslatorAPI.Token;
import ru.therealmone.TranslatorAPI.Visitor;

import java.io.FileReader;
import java.util.*;

public class Parser implements Visitor {

    private Stack<String> stack = new Stack<>();
    private Map<Integer, String[]> langRules = new HashMap<>();
    private Map<String, Map<String, Integer>> analyzeTable = new HashMap<>();
    private HashSet<String> terminals;
    public static boolean ERROR = false;

    @Override
    public void visit(Token token) {
        ERROR = !parse(token);
    }

    public Parser(HashSet<String> terminals) {
        this.terminals = terminals;
        terminals.add("$");
        stack.push("lang");

        try {
            CSVReader csvReader = new CSVReader(new FileReader("SPOParser/src/main/resources/langRules.csv"));
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

            csvReader = new CSVReader(new FileReader("SPOParser/src/main/resources/analyzeTable.csv"));
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

    private boolean parse(Token token) {
        while(!terminals.contains(stack.peek())) {
            openNonTerminal(token);
        }
        return stack.pop().equals(token.getType());
    }

    public void showLangRules() {
        System.out.println("LANG RULES; ");
        for(Map.Entry<Integer, String[]> entry: langRules.entrySet()) {
            System.out.println("Lang rule №" + entry.getKey() + ": " + Arrays.toString(entry.getValue()));
        }
    }
        //TODO: Сформировать синтаксическое дерево
    private void openNonTerminal(Token token) {
        String peek = stack.peek();
        try {
            String[] tmp = langRules.get(analyzeTable.get(stack.pop()).get(token.getType()));
            for (int i = tmp.length - 1; i >= 0; i--) {
                if(!tmp[i].equals("EMPTY")) {
                    stack.push(tmp[i]);
                }
            }
        } catch (NullPointerException e) {
            System.out.println("Seems like tables aren't complete \nCan't find rule for " + peek + " (token = " + token.getType() + ")");
            e.printStackTrace();
            System.exit(1);
        }

    }

}
