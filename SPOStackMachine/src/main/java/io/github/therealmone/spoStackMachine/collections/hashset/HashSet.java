package io.github.therealmone.spoStackMachine.collections.hashset;

import io.github.therealmone.spoStackMachine.collections.Collection;

public interface HashSet extends Collection {
    void add(String variable, double value);

    double get(String variable);

    void rewrite(String variable, double value);

    void remove(String variable);

    boolean contains(String variable);

    static HashSet getInstance() {
        return new HashSetImpl();
    }
}
