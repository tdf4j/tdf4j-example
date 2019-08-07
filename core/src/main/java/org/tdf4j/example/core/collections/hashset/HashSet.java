package org.tdf4j.example.core.collections.hashset;

import org.tdf4j.example.core.collections.Collection;

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
