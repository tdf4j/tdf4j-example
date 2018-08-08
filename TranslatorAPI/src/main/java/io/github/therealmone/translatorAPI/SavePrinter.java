package io.github.therealmone.translatorAPI;

public final class SavePrinter {
    public synchronized static void savePrintln(final String message) {
        System.out.println(message);
    }

    public synchronized static void savePrint(final String message) {
        System.out.print(message);
    }

    public synchronized static void savePrintf(final String format, final Object... args) {
        System.out.printf(format, args);
    }
}
