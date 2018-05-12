package ru.therealmone.TranslatorAPI;

import java.util.ArrayList;

public class Node {
    private ArrayList<Node> childes;
    private String value;
    private Node parent;
    private Token token;

    public Node(String value) {
        this.value = value;
        this.childes = new ArrayList<>();
    }

    public Node(String value, Node parent) {
        this.value = value;
        this.parent = parent;
        this.childes = new ArrayList<>();
    }

    public void addChild(Node child) {
        this.childes.add(child);
    }

    public Token getToken() {
        return this.token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Node getParent() {
        return this.parent;
    }

    public ArrayList<Node> getChildes() {
        return this.childes;
    }

    public String getValue() {
        return this.value;
    }
}
