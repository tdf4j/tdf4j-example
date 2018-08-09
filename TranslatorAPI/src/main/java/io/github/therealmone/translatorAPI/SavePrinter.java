package io.github.therealmone.translatorAPI;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public final class SavePrinter {
    private static Map<Integer, PrintStream> streams;

    static {
        streams = new HashMap<>();
    }

    public synchronized static void savePrintln(final String message) {
        int hash = Thread.currentThread().hashCode();
        if(!streams.containsKey(hash)) {
            setDefaultOut(hash);
        }

        streams.get(hash).println(message);
    }

    public synchronized static void savePrint(final String message) {
        int hash = Thread.currentThread().hashCode();
        if(!streams.containsKey(hash)) {
            setDefaultOut(hash);
        }

        streams.get(hash).print(message);
    }

    public synchronized static void savePrintf(final String format, final Object... args) {
        int hash = Thread.currentThread().hashCode();
        if(!streams.containsKey(hash)) {
            setDefaultOut(hash);
        }

        streams.get(hash).printf(format, args);
    }

    public synchronized static void setOut(OutputStream outputStream) {
        int hash = Thread.currentThread().hashCode();
        if(streams.containsKey(hash)) {
            streams.replace(hash, new PrintStream(outputStream));
        } else {
            streams.put(hash, new PrintStream(outputStream));
        }
    }

    private static void setDefaultOut(int hash) {
        streams.put(hash, System.out);
    }
}
