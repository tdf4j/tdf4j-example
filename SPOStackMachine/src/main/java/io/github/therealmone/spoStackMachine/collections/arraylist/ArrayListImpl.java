package io.github.therealmone.spoStackMachine.collections.arraylist;

import io.github.therealmone.spoStackMachine.collections.arraylist.exceptions.IndexOutOfBoundsException;

class ArrayListImpl implements ArrayList {
    private int size;
    private double[] data;
    private int pointer;

    private static final int DATA_INIT_SIZE = 10;

    ArrayListImpl() {
        size = 0;
        data = new double[DATA_INIT_SIZE];
        pointer = 0;
    }

    @Override
    public void add(double value) {
        if (pointer >= data.length) {
            resize();
        }

        data[pointer] = value;
        pointer++;
        size++;
    }

    @Override
    public double get(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException(index, size);
        }

        return data[index];
    }

    @Override
    public void rewrite(int index, double value) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException(index, size);
        }

        data[index] = value;
    }

    @Override
    public void remove(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException(index, size);
        }

        double[] tempData = new double[data.length - 1];

        if (index == 0) {
            System.arraycopy(data, 1, tempData, 0, data.length - 1);
        } else if (index == data.length - 1) {
            System.arraycopy(data, 0, tempData, 0, data.length - 1);
        } else {
            System.arraycopy(data, 0, tempData, 0, index);
            System.arraycopy(data, index + 1, tempData, index, data.length - index - 1);
        }

        data = tempData;
        size--;
        pointer--;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("ArrayList@").append(this.hashCode()).append(":\n");
        for (int i = 0; i < size; i++) {
            out.append("\tindex = ")
                    .append(i)
                    .append(", value = ")
                    .append(data[i])
                    .append("\n");
        }

        return out.toString();
    }

    private void resize() {
        double[] tempData = new double[data.length + 10];
        System.arraycopy(data, 0, tempData, 0, data.length);

        data = tempData;
    }
}


