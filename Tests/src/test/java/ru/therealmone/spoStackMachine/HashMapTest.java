package ru.therealmone.spoStackMachine;

import org.junit.Test;
import ru.therealmone.spoStackMachine.collections.hashset.HashSetImpl;
import ru.therealmone.spoStackMachine.collections.hashset.exceptions.KeyAlreadyExistsException;
import ru.therealmone.spoStackMachine.collections.hashset.exceptions.NoSuchElementException;

import static org.junit.Assert.*;

public class HashMapTest {
    private HashSetImpl hashSet;

    @Test
    public void addAndGetMethodsTest() {
        hashSet = new HashSetImpl();
        try {
            hashSet.add("1", 1);
            hashSet.add("1", 1);
            fail();
        } catch (KeyAlreadyExistsException e) {}

        hashSet = new HashSetImpl();
        try {
            hashSet.add("1", 1);
            assertEquals(1, hashSet.get("1"), 0);
            hashSet.add("2", 2);
            assertEquals(2, hashSet.get("2"), 0);
            hashSet.add("qwerty", 100);
            assertEquals(100, hashSet.get("qwerty"), 0);
        } catch (NoSuchElementException | KeyAlreadyExistsException e) {
            fail();
        }
    }

    @Test
    public void deleteMethodTest() {
        hashSet = new HashSetImpl();
        try {
            hashSet.add("1", 1);
            hashSet.get("1");
        } catch (KeyAlreadyExistsException e) {
            fail();
        } catch (NoSuchElementException e) {}

        hashSet = new HashSetImpl();
        try {
            hashSet.add("1", 1);
            hashSet.add("2", 2);
            hashSet.add("qwerty", 100);
            hashSet.remove("qwerty");
            hashSet.get("qwerty");
        } catch (KeyAlreadyExistsException e) {
            fail();
        } catch (NoSuchElementException e) {}
    }

    @Test
    public void containsMethodTest() {
        hashSet = new HashSetImpl();
        try {
            hashSet.add("1", 1);
            assertTrue(hashSet.contains("1"));
        } catch (KeyAlreadyExistsException e) {
            fail();
        }

        hashSet = new HashSetImpl();
        try {
            hashSet.add("1", 1);
            hashSet.add("2", 2);
            hashSet.add("qwerty", 100);
            assertTrue(hashSet.contains("1"));
            assertTrue(hashSet.contains("2"));
            assertTrue(hashSet.contains("qwerty"));
        } catch (KeyAlreadyExistsException e) {
            fail();
        }
    }

    @Test
    public void rewriteMethodTest() {
        hashSet = new HashSetImpl();
        try {
            hashSet.add("1", 1);
            hashSet.rewrite("1", 1000);
            assertEquals(1000, hashSet.get("1"), 0);
        } catch (KeyAlreadyExistsException | NoSuchElementException e) {
            fail();
        }

        hashSet = new HashSetImpl();
        try {
            hashSet.add("1", 1);
            hashSet.add("2", 2);
            hashSet.add("qwerty", 100);
            hashSet.rewrite("qwerty", 1_000_000);
            assertEquals(1_000_000, hashSet.get("qwerty"), 0);
        } catch (KeyAlreadyExistsException | NoSuchElementException e) {
            fail();
        }
    }

    @Test
    public void resizeMethodTest() {
        hashSet = new HashSetImpl();
        try {
            int buckets_count = hashSet.getBucketCount();

            for (int i = 0; i < 1000; i++) {
                hashSet.add("" + i, i);
            }

            if (buckets_count == hashSet.getBucketCount()) {
                fail();
            } else {
                for (int i = 0; i < 1000; i++) {
                    double tmp = hashSet.get("" + i);
                }
            }

        } catch (KeyAlreadyExistsException | NoSuchElementException e) {
            fail();
        }
    }
}
