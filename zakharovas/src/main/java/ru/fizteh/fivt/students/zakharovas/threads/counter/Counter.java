package ru.fizteh.fivt.students.zakharovas.threads.counter;

import java.io.IOException;

/**
 * Created by alexander on 05.12.2015.
 */
public class Counter {
    public static void main(String[] args) {
        int numberOfThreads = 0;
        try {
            if (args.length == 0) {
                throw new IOException();
            }
            numberOfThreads = Integer.valueOf(args[0]);
            if (numberOfThreads <= 0) {
                throw new IOException();
            }
        } catch (NumberFormatException | IOException e) {
            System.err.println("There should be positive number as command line argument");
            System.exit(1);
        }
        startThreads(numberOfThreads);
    }

    private static void startThreads(int numberOfThreads) {
        CountingThread.setNumberOfThreads(numberOfThreads);
        for (int i = 0; i < numberOfThreads; ++i) {
            new CountingThread(i).start();
        }
    }
}
