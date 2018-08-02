package ru.therealmone.spoStackMachine.collections.hashset;

import ru.therealmone.spoStackMachine.collections.HashSet;
import ru.therealmone.spoStackMachine.collections.hashset.exceptions.KeyAlreadyExistsException;
import ru.therealmone.spoStackMachine.collections.hashset.exceptions.NoSuchElementException;

public class HashSetImpl implements HashSet {
    private int BUCKET_COUNT = 17;
    private static final int MAX_ELEMENTS_IN_BUCKET = 7;
    private Bucket[] buckets;

    public HashSetImpl() {
        buckets = new Bucket[BUCKET_COUNT];
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = new Bucket();
        }
    }

    @Override
    public int size() {
        int size = 0;
        for (Bucket bucket : buckets) {
            size += bucket.getEleCount();
        }

        return size;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();

        out.append("\n\tHashSet@")
                .append(this.hashCode())
                .append(": \n");

        for (int i = 0; i < BUCKET_COUNT; i++) {
            Element current = (Element) buckets[i].getNext();
            while (current != null) {
                out.append("\t")
                        .append(current.getName())
                        .append(" -> ")
                        .append(current.getValue())
                        .append("\n");
                current = (Element) current.getNext();
            }
        }

        out.replace(out.length() - 1, out.length(), "");
        return out.toString();
    }

    @Override
    public void add(String key, double value) {
        if (!contains(key)) {
            int index = getIndex(key);

            while (buckets[index].getEleCount() >= MAX_ELEMENTS_IN_BUCKET) {
                resize();
                index = getIndex(key);
            }

            putToBucket(index, new Element(key, value));
        } else {
            throw new KeyAlreadyExistsException(key);
        }
    }

    @Override
    public double get(String key) {
        int index = getIndex(key);

        if (buckets[index].getEleCount() == 0) {
            throw new NoSuchElementException(key);
        }

        Element current = (Element) buckets[index].getNext();

        while (current != null) {
            if (current.getHashCode() == key.hashCode() && current.getName().equals(key)) {
                return current.getValue();
            }
            current = (Element) current.getNext();
        }

        throw new NoSuchElementException(key);
    }

    @Override
    public boolean contains(String key) {
        int index = getIndex(key);

        if (buckets[index].getEleCount() == 0) {
            return false;
        }

        Element current = (Element) buckets[index].getNext();

        while (current != null) {
            if (current.getHashCode() == key.hashCode() && current.getName().equals(key)) {
                return true;
            }
            current = (Element) current.getNext();
        }

        return false;
    }

    @Override
    public void remove(String key) {
        if (contains(key)) {
            int index = getIndex(key);
            Element current = (Element) buckets[index].getNext();

            while (!(current.getHashCode() == key.hashCode() && current.getName().equals(key))) {
                current = (Element) current.getNext();
            }

            if (current.getNext() != null) {
                current.getNext().setParent(current.getParent());
            }

            current.getParent().setNext(current.getNext());
            buckets[index].decCount();
        } else {
            throw new NoSuchElementException(key);
        }
    }

    @Override
    public void rewrite(String key, double value) {
        if (contains(key)) {
            int index = getIndex(key);
            Element current = (Element) buckets[index].getNext();

            while (current != null) {
                if (current.getHashCode() == key.hashCode() && current.getName().equals(key)) {
                    current.setValue(value);
                    break;
                }
                current = (Element) current.getNext();
            }

        } else {
            throw new NoSuchElementException(key);
        }
    }

    private void resize() {
        BUCKET_COUNT++;
        Bucket[] tempBucketsArray = buckets;
        buckets = new Bucket[BUCKET_COUNT];

        for (int i = 0; i < BUCKET_COUNT; i++) {
            buckets[i] = new Bucket();
        }

        for (Bucket bucket : tempBucketsArray) {
            Element current = (Element) bucket.getNext();

            while (current != null) {
                int newIndex = getIndex(current.getName());
                putToBucket(newIndex, current);

                Element tempCurrent = (Element) current.getNext();
                current.setNext(null);
                current = tempCurrent;
            }
        }
    }

    private void putToBucket(int index, Element element) {
        if (buckets[index].getNext() != null) {
            Element current = (Element) buckets[index].getNext();

            while (current.getNext() != null) {
                current = (Element) current.getNext();
            }

            current.setNext(element);
            element.setParent(current);
        } else {
            buckets[index].setNext(element);
            element.setParent(buckets[index]);
        }

        buckets[index].incCount();
    }

    private int getIndex(String key) {
        return Math.abs(key.hashCode() % BUCKET_COUNT);
    }

    public int getBucketCount() {
        return this.BUCKET_COUNT;
    }
}
