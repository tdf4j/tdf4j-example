package ru.therealmone.TranslatorAPI;

import java.util.ArrayList;

public class Node {
    private ArrayList<Node> childes;
    private String value;

    public Node(String value) {
        this.value = value;
        this.childes = new ArrayList<>();
    }

    public void addChild(Node child) {
        this.childes.add(child);
    }

    public ArrayList<Node> getChildes() {
        return this.childes;
    }

    public String getValue() {
        return this.value;
    }
}
