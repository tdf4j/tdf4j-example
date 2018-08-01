package ru.therealmone.translatorAPI;

public final class SavePrinter {
    public synchronized static void savePrintln(String message) {
        System.out.println(message);
    }

    public synchronized static void savePrint(String message) {
        System.out.print(message);
    }

    public synchronized static void savePrintf(String format, Object ... args) {
        System.out.printf(format, args);
    }
}
