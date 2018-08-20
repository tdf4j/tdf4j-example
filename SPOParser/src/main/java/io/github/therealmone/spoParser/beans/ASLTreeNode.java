package io.github.therealmone.spoParser.beans;

import io.github.therealmone.core.beans.Node;
import io.github.therealmone.core.beans.Token;

import java.util.ArrayList;
import java.util.List;

public class ASLTreeNode extends Node {
    private List<ASLTreeNode> childes;
    private Token token;

    public ASLTreeNode(final String value) {
        super(value);
        this.childes = new ArrayList<>();
    }

    public ASLTreeNode(final String value, final ASLTreeNode parent) {
        super(value, parent);
        this.childes = new ArrayList<>();
    }

    public void addChild(final ASLTreeNode child) {
        this.childes.add(child);
    }

    public Token getToken() {
        return this.token;
    }

    public void setToken(final Token token) {
        this.token = token;
    }

    public List<ASLTreeNode> getChildes() {
        return this.childes;
    }
}
