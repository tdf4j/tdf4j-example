package io.github.therealmone.spoStackMachine.collections;

public interface HashSet extends Collection {
    void add(String variable, double value);

    double get(String variable);

    void rewrite(String variable, double value);

    void remove(String variable);

    boolean contains(String variable);
}
