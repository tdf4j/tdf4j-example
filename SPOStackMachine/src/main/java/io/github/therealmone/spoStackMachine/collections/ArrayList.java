package ru.therealmone.spoStackMachine.collections;

public interface ArrayList extends Collection {
    void add(double value);

    double get(int index);

    void remove(int index);

    void rewrite(int index, double value);
}
