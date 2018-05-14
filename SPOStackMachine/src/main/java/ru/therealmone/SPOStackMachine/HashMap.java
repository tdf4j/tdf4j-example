package ru.therealmone.SPOStackMachine;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.NoSuchElementException;

class HashMap {
    private int BUCKET_COUNT = 16;
    private static final int MAX_ELEMENTS_IN_BUCKET = 8;
    private Bucket[] buckets;

    HashMap() {
        buckets = new Bucket[BUCKET_COUNT];
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = new Bucket();
        }
    }

    void add(String key, int value) throws KeyAlreadyExistsException {
        try {
            get(key);
            throw new KeyAlreadyExistsException();

        } catch (NoSuchElementException e) {

            int index = getIndex(key);

            while(buckets[index].getEleCount() >= MAX_ELEMENTS_IN_BUCKET) {
                resize();
                index = getIndex(key);
            }

            putToBucket(index, new Element(key, value));
        }
    }

    int get(String key) throws NoSuchElementException {
        int index = getIndex(key);
        if(buckets[index].getEleCount() == 0) {throw new NoSuchElementException();}

        Element current = (Element) buckets[index].getNext();

        while(current != null) {
            if(current.getName().equals(key) && current.getHashCode() == key.hashCode()) {
                return current.getValue();
            }
            current = (Element) current.getNext();
        }

        throw new NoSuchElementException();
    }

    void delete(String key) throws NoSuchElementException {
        get(key);

        int index = getIndex(key);
        Element current = (Element) buckets[index].getNext();

        while(!(current.getName().equals(key) && current.getHashCode() == key.hashCode())) {
            current = (Element) current.getNext();
        }

        current.getNext().setParent(current.getParent());
        current.getParent().setNext(current.getNext());
        buckets[index].decCount();
    }

    private void resize() {
        System.out.println("Resized");
    }

    private void putToBucket(int index, Element element) {
        if(buckets[index].getNext() != null) {
            Element current = (Element) buckets[index].getNext();

            while(current.getNext() != null) {
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
}
