package ru.therealmone.spoStackMachine;

import org.junit.Test;
import ru.therealmone.translatorAPI.KeyAlreadyExistsException;
import ru.therealmone.translatorAPI.NoSuchElementException;

import static org.junit.Assert.*;

public class HashMapTest {
    private HashMap hashMap;

    @Test
    public void addAndGetMethodsTest() {
        hashMap = new HashMap();
        try {
            hashMap.add("1", 1);
            hashMap.add("1", 1);
            fail();
        } catch (KeyAlreadyExistsException e) {}

        hashMap = new HashMap();
        try {
            hashMap.add("1", 1);
            assertEquals(1, hashMap.get("1"), 0);
            hashMap.add("2", 2);
            assertEquals(2, hashMap.get("2"), 0);
            hashMap.add("qwerty", 100);
            assertEquals(100, hashMap.get("qwerty"), 0);
        } catch (NoSuchElementException | KeyAlreadyExistsException e) {
            fail();
        }
    }

    @Test
    public void deleteMethodTest() {
        hashMap = new HashMap();
        try {
            hashMap.add("1", 1);
            hashMap.get("1");
        } catch (KeyAlreadyExistsException e) {
            fail();
        } catch (NoSuchElementException e) {}

        hashMap = new HashMap();
        try {
            hashMap.add("1", 1);
            hashMap.add("2", 2);
            hashMap.add("qwerty", 100);
            hashMap.delete("qwerty");
            hashMap.get("qwerty");
        } catch (KeyAlreadyExistsException e) {
            fail();
        } catch (NoSuchElementException e) {}
    }

    @Test
    public void containsMethodTest() {
        hashMap = new HashMap();
        try {
            hashMap.add("1", 1);
            assertTrue(hashMap.contains("1"));
        } catch (KeyAlreadyExistsException e) {
            fail();
        }

        hashMap = new HashMap();
        try {
            hashMap.add("1", 1);
            hashMap.add("2", 2);
            hashMap.add("qwerty", 100);
            assertTrue(hashMap.contains("1"));
            assertTrue(hashMap.contains("2"));
            assertTrue(hashMap.contains("qwerty"));
        } catch (KeyAlreadyExistsException e) {
            fail();
        }
    }

    @Test
    public void rewriteMethodTest() {
        hashMap = new HashMap();
        try {
            hashMap.add("1", 1);
            hashMap.rewrite("1", 1000);
            assertEquals(1000, hashMap.get("1"), 0);
        } catch (KeyAlreadyExistsException | NoSuchElementException e) {
            fail();
        }

        hashMap = new HashMap();
        try {
            hashMap.add("1", 1);
            hashMap.add("2", 2);
            hashMap.add("qwerty", 100);
            hashMap.rewrite("qwerty", 1_000_000);
            assertEquals(1_000_000, hashMap.get("qwerty"), 0);
        } catch (KeyAlreadyExistsException | NoSuchElementException e) {
            fail();
        }
    }

    @Test
    public void resizeMethodTest() {
        hashMap = new HashMap();
        try {
            int buckets_count = hashMap.BUCKET_COUNT;

            for (int i = 0; i < 1000; i++) {
                hashMap.add("" + i, i);
            }

            if (buckets_count == hashMap.BUCKET_COUNT) {
                fail();
            } else {
                for (int i = 0; i < 1000; i++) {
                    double tmp = hashMap.get("" + i);
                }
            }

        } catch (KeyAlreadyExistsException | NoSuchElementException e) {
            fail();
        }
    }
}
