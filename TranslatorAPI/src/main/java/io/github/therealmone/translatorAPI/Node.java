package ru.therealmone.translatorAPI;

public class Node {
    private String name;
    private Node next;
    private Node parent;

    public Node() {
    }

    public Node(String name) {
        this.name = name;
    }

    public Node(String name, Node parent) {
        this.name = name;
        this.parent = parent;
    }

    public Node getParent() {
        return this.parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public String getName() {
        return this.name;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public Node getNext() {
        return this.next;
    }
}
