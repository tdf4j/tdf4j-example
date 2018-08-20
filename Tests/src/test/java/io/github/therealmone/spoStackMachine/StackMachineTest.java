package io.github.therealmone.spoStackMachine;

import io.github.therealmone.jtrAPI.Translator;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import static org.junit.Assert.*;

public class StackMachineTest {
    private static Translator translator;
    private static ByteArrayOutputStream out;

    @Before
    public void Before() {
        translator = Translator.getInstance();
        out = new ByteArrayOutputStream();
        Translator.setOutputStream(out);
    }

    @Test
    public void collectionsTest() {
        out.reset();
        translator.translate(
                "new a typeof hashset;" +
                        "new one = 1;" +
                        "new two = 2;" +
                        "put(a, one);" +
                        "put(a, two);" +
                        "print(get(a, one));" +
                        "print(get(a, two));" +
                        "remove(a, one);" +
                        "print(get(a, one));"
        );
        assertEquals(
                "1.0\r\n" +
                        "2.0\r\n" +
                        "Can't find key one.\r\n",
                out.toString()
        );


        out.reset();
        translator.translate(
                "new a typeof hashset;" +
                        "new i = 100;" +
                        "put(a, i);" +
                        "put(a, i);"
        );
        assertEquals("Key i already exists.\r\n", out.toString());


        out.reset();
        translator.translate(
                "new a typeof arraylist;" +
                        "new i = 0;" +
                        "for(i = 0; i < 5; i = i + 1) {" +
                        "   put(a, i);" +
                        "}" +
                        "" +
                        "for(i = 0; i < 5; i = i + 1) {" +
                        "   print(get(a, i));" +
                        "}" +
                        "print(get(a, -1));"
        );
        assertEquals(
                "0.0\r\n" +
                        "1.0\r\n" +
                        "2.0\r\n" +
                        "3.0\r\n" +
                        "4.0\r\n" +
                        "Index out of bound: -1. Length = 5\r\n",
                out.toString()
        );


        out.reset();
        translator.translate(
                "new a typeof hashset;" +
                        "new i = get(a, 100);"
        );
        assertEquals("Can't find key 100.0.\r\n", out.toString());


        out.reset();
        translator.translate(
                "new a typeof arraylist;" +
                        "new i = get(a, i);"
        );
        assertEquals("Wrong type of: i\r\n", out.toString());
    }

    @Test
    public void printTest() {
        out.reset();
        translator.translate(
                "new a typeof hashset;" +
                        "new i = 100;" +
                        "put(a, i);" +
                        "print(get(a, i));"
        );
        assertEquals("100.0\r\n", out.toString());


        out.reset();
        translator.translate(
                "new a typeof arraylist;" +
                        "put(a, 100);" +
                        "print(get(a, 0));"
        );
        assertEquals("100.0\r\n", out.toString());


        out.reset();
        translator.translate("print(\"Test String\");");
        assertEquals("Test String\r\n", out.toString());


        out.reset();
        translator.translate(
                "new i = 100;" +
                        "print(\"This value equals \" ++ i);"
        );
        assertEquals("This value equals 100.0\r\n", out.toString());


        out.reset();
        translator.translate(
                "new a typeof hashset;" +
                        "new i = 100;" +
                        "put(a, i);" +
                        "print(\"This value equals \" ++ i ++ \": \" ++ get(a, i));"
        );
        assertEquals("This value equals 100.0: 100.0\r\n", out.toString());


        out.reset();
        translator.translate(
                "new a typeof hashset;" +
                        "print(a);"
        );
        assertEquals("HashSet@hash: [  ]\r\n", out.toString().replaceAll("@\\d+", "@hash"));
    }

    @Test
    public void mathOperationsTest() {
        out.reset();
        translator.translate("print(100 + 100);");
        assertEquals("200.0\r\n", out.toString());


        out.reset();
        translator.translate("print(100 * 10);");
        assertEquals("1000.0\r\n", out.toString());


        out.reset();
        translator.translate("print(100 - 101);");
        assertEquals("-1.0\r\n", out.toString());


        out.reset();
        translator.translate("print(100 / 5 * -1);");
        assertEquals("-20.0\r\n", out.toString());


        out.reset();
        translator.translate("print(100 div 10);");
        assertEquals("10.0\r\n", out.toString());


        out.reset();
        translator.translate("print(105 mod 20);");
        assertEquals("5.0\r\n", out.toString());


        out.reset();
        translator.translate("print(2 * 2 + 2);");
        assertEquals("6.0\r\n", out.toString());


        out.reset();
        translator.translate("print(2 * (2 + 2));");
        assertEquals("8.0\r\n", out.toString());
    }

    @Test
    public void errorsTest() {
        out.reset();
        translator.translate("new a = hashset;");
        assertEquals(
                "Unexpected token HASHSET at '^' mark. \r\n" +
                         "new a = hashset\r\n" +
                         "        ^\r\n",
                out.toString()
        );


        out.reset();
        translator.translate("get(a, 1);");
        assertEquals(
                "Unexpected token GET at '^' mark. \r\n" +
                        "get\r\n" +
                        "^\r\n",
                out.toString()
        );


        out.reset();
        translator.translate("while(a & b);");
        assertEquals(
                "Unexpected token LOP at '^' mark. \r\n" +
                        "while ( a &\r\n" +
                        "          ^\r\n" +
                        "Expected: COP\r\n",
                out.toString()
        );


        out.reset();
        translator.translate("print(i);");
        assertEquals("Can't find variable i\r\n", out.toString());


        out.reset();
        translator.translate(
                "new a typeof hashset;" +
                        "print(get(a, i));"
        );
        assertEquals("Can't find key i.\r\n", out.toString());


        out.reset();
        translator.translate("@");
        assertEquals(
                "Unexpected symbol at '^' mark. \r\n" +
                        "@$\r\n" +
                        "^\r\n",
                out.toString()
        );


        out.reset();
        translator.translate(
                "new a typeof hashset;" +
                        "new i = 0;" +
                        "put(a, i);" +
                        "put(a, i);"
        );
        assertEquals("Key i already exists.\r\n",out.toString());


        out.reset();
        translator.translate(
                "new a typeof hashset;" +
                        "print(get(a, i));"
        );
        assertEquals("Can't find key i.\r\n",out.toString());


        out.reset();
        translator.translate(
                "new a typeof hashset;" +
                        "put(a, i);"
        );
        assertEquals("Can't find variable: i\r\n",out.toString());


        out.reset();
        translator.translate(
                "new a typeof arraylist;" +
                        "new i typeof hashset;" +
                        "put(a, i);"
        );
        assertEquals("Wrong type of: i\r\n",out.toString());
    }

    @Test
    public void typeCastTest() {
        out.reset();
        translator.translate(
                "new a typeof arraylist;" +
                        "put(a, 1);" +
                        "put(a, 2);" +
                        "print(a);" +
                        "a = \"Test\";" +
                        "print(a);" +
                        "a = 100;" +
                        "print(a);" +
                        "a = 101.101;" +
                        "print(a);"
        );
        assertEquals(
                "ArrayList@hash: [ (index = 0, value = 1.0)  (index = 1, value = 2.0) ]\r\n" +
                        "Test\r\n" +
                        "100.0\r\n" +
                        "101.101\r\n"
                ,out.toString().replaceAll("@\\d+", "@hash")
        );


        out.reset();
        translator.translate(
                "new a typeof arraylist;" +
                        "put(a, 1);" +
                        "put(a, 2);" +
                        "print(a);" +
                        "new b typeof hashset;" +
                        "a = b;" +
                        "print(a);" + //При присваивании переменной любого типа отличного от примитивных, сконвертирует все в строку
                        "put(a, 1);"
        );
        assertEquals(
                "ArrayList@hash: [ (index = 0, value = 1.0)  (index = 1, value = 2.0) ]\r\n" +
                        "HashSet@hash: [  ]\r\n" +
                        "Wrong type of: a\r\n"
                ,out.toString().replaceAll("@\\d+", "@hash")
        );
    }
}
