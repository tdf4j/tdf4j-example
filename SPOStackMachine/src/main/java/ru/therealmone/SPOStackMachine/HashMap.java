package ru.therealmone.SPOStackMachine;

import ru.therealmone.TranslatorAPI.KeyAlreadyExistsException;
import ru.therealmone.TranslatorAPI.NoSuchElementException;

class HashMap {
    private int BUCKET_COUNT = 17;
    private static final int MAX_ELEMENTS_IN_BUCKET = 7;
    private Bucket[] buckets;

    HashMap() {
        buckets = new Bucket[BUCKET_COUNT];
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = new Bucket();
        }
    }

    void add(String key, double value) throws KeyAlreadyExistsException {
        if(!contains(key)) {
            int index = getIndex(key);

            while (buckets[index].getEleCount() >= MAX_ELEMENTS_IN_BUCKET) {
                resize();
                index = getIndex(key);
            }

            putToBucket(index, new Element(key, value));
        } else {throw new KeyAlreadyExistsException(key);}
    }

    double get(String key) throws NoSuchElementException {
        int index = getIndex(key);
        if(buckets[index].getEleCount() == 0) {throw new NoSuchElementException(key);}

        Element current = (Element) buckets[index].getNext();

        while(current != null) {
            if(current.getName().equals(key) && current.getHashCode() == key.hashCode()) {
                return current.getValue();
            }
            current = (Element) current.getNext();
        }

        throw new NoSuchElementException(key);
    }

    boolean contains(String key) {
        int index = getIndex(key);
        if(buckets[index].getEleCount() == 0) {return false;}

        Element current = (Element) buckets[index].getNext();

        while(current != null) {
            if(current.getName().equals(key) && current.getHashCode() == key.hashCode()) {
                return true;
            }
            current = (Element) current.getNext();
        }

        return false;
    }

    void delete(String key) throws NoSuchElementException {
        if(contains(key)) {

            int index = getIndex(key);
            Element current = (Element) buckets[index].getNext();

            while (!(current.getName().equals(key) && current.getHashCode() == key.hashCode())) {
                current = (Element) current.getNext();
            }

            current.getNext().setParent(current.getParent());
            current.getParent().setNext(current.getNext());
            buckets[index].decCount();
        } else {throw new NoSuchElementException(key);}
    }

    void rewrite(String key, double value) throws NoSuchElementException {
        if(contains(key)) {

            int index = getIndex(key);
            Element current = (Element) buckets[index].getNext();

            while (current != null) {
                if (current.getName().equals(key) && current.getHashCode() == key.hashCode()) {
                    current.setValue(value);
                    break;
                }
                current = (Element) current.getNext();
            }

        } else {
            throw new NoSuchElementException(key
            );
        }
    }

    private void resize() {
        //TODO: Реализовать
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

    void show() {
        for (int i = 0; i < BUCKET_COUNT; i++) {
            Element current = (Element) buckets[i].getNext();
            while(current != null) {
                System.out.println(current.getName() + " -> " + current.getValue());
                current = (Element) current.getNext();
            }
        }
    }
}
