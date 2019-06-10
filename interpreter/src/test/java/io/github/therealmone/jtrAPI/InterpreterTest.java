package io.github.therealmone.jtrAPI;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import io.github.therealmone.jtrAPI.impl.InterpreterImpl;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import static org.junit.Assert.*;

public class InterpreterTest {
    private static Interpreter interpreter;
    private static ByteArrayOutputStream out;

    @BeforeClass
    public static void beforeClass() {
        interpreter = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(Interpreter.class).to(InterpreterImpl.class);
                bind(InputStream.class).toInstance(Thread.currentThread().getContextClassLoader().getResourceAsStream("grammar.tdf"));
            }
        }).getInstance(Interpreter.class);
        out = new ByteArrayOutputStream();
    }

    @Before
    public void before() {
        out = new ByteArrayOutputStream();
    }

    @Test
    public void collectionsTest() {
        out.reset();
        interpreter.process(
                "new a typeof hashset;" +
                        "new one = 1;" +
                        "new two = 2;" +
                        "put(a, one);" +
                        "put(a, two);" +
                        "print(get(a, one));" +
                        "print(get(a, two));" +
                        "remove(a, one);" +
                        "print(get(a, one));$",
                out
        );
        assertEquals(
                "1.0" +
                        "2.0" +
                        "Can't find key one.",
                out.toString().replaceAll("[\r\n]", "")
        );


        out.reset();
        interpreter.process(
                "new a typeof hashset;" +
                        "new i = 100;" +
                        "put(a, i);" +
                        "put(a, i);$",
                out
        );
        assertEquals("Key i already exists.", out.toString().replaceAll("[\r\n]", ""));


        out.reset();
        interpreter.process(
                "new a typeof arraylist;" +
                        "new i = 0;" +
                        "for(i = 0; i < 5; i = i + 1) {" +
                        "   put(a, i);" +
                        "}" +
                        "" +
                        "for(i = 0; i < 5; i = i + 1) {" +
                        "   print(get(a, i));" +
                        "}" +
                        "print(get(a, -1));$",
                out
        );
        assertEquals(
                "0.0" +
                        "1.0" +
                        "2.0" +
                        "3.0" +
                        "4.0" +
                        "Index out of bound: -1. Length = 5",
                out.toString().replaceAll("[\r\n]", "")
        );


        out.reset();
        interpreter.process(
                "new a typeof hashset;" +
                        "new i = get(a, 100);$",
                out
        );
        assertEquals("Can't find key 100.0.", out.toString().replaceAll("[\r\n]", ""));


        out.reset();
        interpreter.process(
                "new a typeof arraylist;" +
                        "new i = get(a, i);$",
                out
        );
        assertEquals("Wrong type of: i", out.toString().replaceAll("[\r\n]", ""));
    }

    @Test
    public void printTest() {
        out.reset();
        interpreter.process(
                "new a typeof hashset;" +
                        "new i = 100;" +
                        "put(a, i);" +
                        "print(get(a, i));$",
                out
        );
        assertEquals("100.0", out.toString().replaceAll("[\r\n]", ""));


        out.reset();
        interpreter.process(
                "new a typeof arraylist;" +
                        "put(a, 100);" +
                        "print(get(a, 0));$",
                out
        );
        assertEquals("100.0", out.toString().replaceAll("[\r\n]", ""));


        out.reset();
        interpreter.process("print(\"Test String\");$", out);
        assertEquals("Test String", out.toString().replaceAll("[\r\n]", ""));


        out.reset();
        interpreter.process(
                "new i = 100;" +
                        "print(\"This value equals \" ++ i);$",
                out
        );
        assertEquals("This value equals 100.0", out.toString().replaceAll("[\r\n]", ""));


        out.reset();
        interpreter.process(
                "new a typeof hashset;" +
                        "new i = 100;" +
                        "put(a, i);" +
                        "print(\"This value equals \" ++ i ++ \": \" ++ get(a, i));$",
                out
        );
        assertEquals("This value equals 100.0: 100.0", out.toString().replaceAll("[\r\n]", ""));


        out.reset();
        interpreter.process(
                "new a typeof hashset;" +
                        "print(a);$",
                out
        );
        assertEquals("HashSet@hash: [  ]", out.toString().replaceAll("@\\d+", "@hash").replaceAll("[\r\n]", ""));
    }

    @Test
    public void mathOperationsTest() {
        out.reset();
        interpreter.process("print(100 + 100);$", out);
        assertEquals("200.0", out.toString().replaceAll("[\r\n]", ""));


        out.reset();
        interpreter.process("print(100 * 10);$", out);
        assertEquals("1000.0", out.toString().replaceAll("[\r\n]", ""));


        out.reset();
        interpreter.process("print(100 - 101);$", out);
        assertEquals("-1.0", out.toString().replaceAll("[\r\n]", ""));


        out.reset();
        interpreter.process("print(100 / 5 * -1);$", out);
        assertEquals("-20.0", out.toString().replaceAll("[\r\n]", ""));


        out.reset();
        interpreter.process("print(100 div 10);$", out);
        assertEquals("10.0", out.toString().replaceAll("[\r\n]", ""));


        out.reset();
        interpreter.process("print(105 mod 20);$", out);
        assertEquals("5.0", out.toString().replaceAll("[\r\n]", ""));


        out.reset();
        interpreter.process("print(2 * 2 + 2);$", out);
        assertEquals("6.0", out.toString().replaceAll("[\r\n]", ""));


        out.reset();
        interpreter.process("print(2 * (2 + 2));$", out);
        assertEquals("8.0", out.toString().replaceAll("[\r\n]", ""));
    }

    @Test
    public void errorsTest() {
        out.reset();
        interpreter.process("new a = hashset;$", out);
        assertEquals(
                "Unexpected token: Token{tag=HASHSET, value=hashset, row=1, column=8}",
                out.toString().replaceAll("[\r\n]", "")
        );


        out.reset();
        interpreter.process("get(a, 1);$", out);
        assertEquals(
                "Unexpected token: Token{tag=GET, value=get, row=1, column=0}",
                out.toString().replaceAll("[\r\n]", "")
        );


        out.reset();
        interpreter.process("while(a & b);$", out);
        assertEquals(
                "Unexpected token: Token{tag=LOP, value=&, row=1, column=8}",
                out.toString().replaceAll("[\r\n]", "")
        );


        out.reset();
        interpreter.process("print(i);$", out);
        assertEquals("Can't find variable i", out.toString().replaceAll("[\r\n]", ""));


        out.reset();
        interpreter.process(
                "new a typeof hashset;" +
                        "print(get(a, i));$"
        , out);
        assertEquals("Can't find key i.", out.toString().replaceAll("[\r\n]", ""));


        out.reset();
        interpreter.process("@$", out);
        assertEquals(
                "Unexpected symbol: @ ( line 1, column 1 )",
                out.toString().replaceAll("[\r\n]", "")
        );


        out.reset();
        interpreter.process(
                "new a typeof hashset;" +
                        "new i = 0;" +
                        "put(a, i);" +
                        "put(a, i);$"
        , out);
        assertEquals("Key i already exists.", out.toString().replaceAll("[\r\n]", ""));


        out.reset();
        interpreter.process(
                "new a typeof hashset;" +
                        "print(get(a, i));$"
        , out);
        assertEquals("Can't find key i.", out.toString().replaceAll("[\r\n]", ""));


        out.reset();
        interpreter.process(
                "new a typeof hashset;" +
                        "put(a, i);$"
        , out);
        assertEquals("Can't find variable: i", out.toString().replaceAll("[\r\n]", ""));


        out.reset();
        interpreter.process(
                "new a typeof arraylist;" +
                        "new i typeof hashset;" +
                        "put(a, i);$"
        , out);
        assertEquals("Wrong type of: i", out.toString().replaceAll("[\r\n]", ""));
    }

    @Test
    public void typeCastTest() {
        out.reset();
        interpreter.process(
                "new a typeof arraylist;" +
                        "put(a, 1);" +
                        "put(a, 2);" +
                        "print(a);" +
                        "a = \"Test\";" +
                        "print(a);" +
                        "a = 100;" +
                        "print(a);" +
                        "a = 101.101;" +
                        "print(a);$"
        , out);
        assertEquals(
                "ArrayList@hash: [ (index = 0, value = 1.0)  (index = 1, value = 2.0) ]" +
                        "Test" +
                        "100.0" +
                        "101.101"
                ,out.toString().replaceAll("@\\d+", "@hash").replaceAll("[\r\n]", "")
        );


        out.reset();
        interpreter.process(
                "new a typeof arraylist;" +
                        "put(a, 1);" +
                        "put(a, 2);" +
                        "print(a);" +
                        "new b typeof hashset;" +
                        "a = b;" +
                        "print(a);" + //При присваивании переменной любого типа отличного от примитивных, сконвертирует все в строку
                        "put(a, 1);$"
        , out);
        assertEquals(
                "ArrayList@hash: [ (index = 0, value = 1.0)  (index = 1, value = 2.0) ]" +
                        "HashSet@hash: [  ]" +
                        "Wrong type of: a"
                ,out.toString().replaceAll("@\\d+", "@hash").replaceAll("[\r\n]", "")
        );
    }
}
