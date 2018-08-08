package io.github.therealmone.spoStackMachine.collections.arraylist;

import io.github.therealmone.spoStackMachine.collections.arraylist.exceptions.IndexOutOfBoundsException;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class ArrayListTest {
    private ArrayList arrayList;

    @Test
    public void testAddAndGetMethod() {
        arrayList = ArrayList.getInstance();

        arrayList.add(1);
        arrayList.add(2);
        arrayList.add(3);
        assertEquals(1, arrayList.get(0), 0);
        assertEquals(2, arrayList.get(1), 0);
        assertEquals(3, arrayList.get(2), 0);
        assertEquals(3, arrayList.size(), 0);

        try {
            arrayList.get(-1);
        } catch (IndexOutOfBoundsException e) {
            Assert.assertEquals("Index out of bound: -1. Length = 3", e.getMessage());
        }

        try {
            arrayList.get(4);
        } catch (IndexOutOfBoundsException e) {
            Assert.assertEquals("Index out of bound: 4. Length = 3", e.getMessage());
        }
    }

    @Test
    public void testRemoveMethod() {
        arrayList = ArrayList.getInstance();

        //Beginning
        arrayList.add(1);
        arrayList.add(2);
        arrayList.add(3);
        arrayList.remove(0);

        assertEquals(2, arrayList.get(0), 0);
        assertEquals(3, arrayList.get(1), 0);
        assertEquals(2, arrayList.size());

        //Middle
        arrayList = ArrayList.getInstance();

        arrayList.add(1);
        arrayList.add(2);
        arrayList.add(3);
        arrayList.remove(1);

        assertEquals(1, arrayList.get(0), 0);
        assertEquals(3, arrayList.get(1), 0);
        assertEquals(2, arrayList.size());

        //End
        arrayList = ArrayList.getInstance();

        arrayList.add(1);
        arrayList.add(2);
        arrayList.add(3);
        arrayList.remove(2);

        assertEquals(1, arrayList.get(0), 0);
        assertEquals(2, arrayList.get(1), 0);
        assertEquals(2, arrayList.size());
    }

    @Test
    public void testRewriteMethod() {
        arrayList = ArrayList.getInstance();

        arrayList.add(1);
        arrayList.add(2);
        arrayList.add(3);
        arrayList.rewrite(0, 3);
        arrayList.rewrite(1, 2);
        arrayList.rewrite(2, 1);

        assertEquals(3, arrayList.get(0), 0);
        assertEquals(2, arrayList.get(1), 0);
        assertEquals(1, arrayList.get(2), 0);
        assertEquals(3, arrayList.size());
    }

    @Test
    public void testResizeMethod() throws Throwable {
        arrayList = ArrayList.getInstance();

        Field dataField = arrayList.getClass().getDeclaredField("data");
        dataField.setAccessible(true);

        double[] dataBefore = (double[]) dataField.get(arrayList);
        assertEquals(10, dataBefore.length);

        for (int i = 0; i < 10000; i++) {
            arrayList.add(i);
        }
        arrayList.add(1);

        double[] dataAfter = (double[]) dataField.get(arrayList);
        assertNotEquals(dataBefore.length, dataAfter.length);
        assertEquals(10010, dataAfter.length);

        for (int i = 0; i < 10000; i++) {
            assertEquals(i, arrayList.get(i), 0);
        }
        assertEquals(1, arrayList.get(arrayList.size() - 1), 0);
    }
}
