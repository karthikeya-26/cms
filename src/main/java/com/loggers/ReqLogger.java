package com.loggers;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class ReqLogger {
    private static PrintStream outputStream;
    private static PrintStream errorStream;

    static {
        try {
            outputStream = new PrintStream(new BufferedOutputStream(new FileOutputStream("sout.txt", true)), true);
            errorStream = new PrintStream(new BufferedOutputStream(new FileOutputStream("serr.txt", true)), true);
            
            System.setOut(outputStream);
            System.setErr(errorStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void logRequest(String message) {
        System.out.println(message); 
    }

    public static void logError(String message) {
        System.err.println(message); 
    }

    public static void close() {
        if (outputStream != null) {
            outputStream.close();
        }
        if (errorStream != null) {
            errorStream.close();
        }
    }
}
