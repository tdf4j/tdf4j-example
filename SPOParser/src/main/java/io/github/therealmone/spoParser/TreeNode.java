package io.github.therealmone.spoParser;

import io.github.therealmone.translatorAPI.Beans.Node;
import io.github.therealmone.translatorAPI.Beans.Token;

import java.util.ArrayList;

class TreeNode extends Node {
    private ArrayList<TreeNode> childes;
    private Token token;

    TreeNode(final String value) {
        super(value);
        this.childes = new ArrayList<>();
    }

    TreeNode(final String value, final TreeNode parent) {
        super(value, parent);
        this.childes = new ArrayList<>();
    }

    void addChild(final TreeNode child) {
        this.childes.add(child);
    }

    Token getToken() {
        return this.token;
    }

    void setToken(final Token token) {
        this.token = token;
    }

    ArrayList<TreeNode> getChildes() {
        return this.childes;
    }
}
