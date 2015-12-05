package ru.fizteh.fivt.students.zakharovas.threads.counter;

/**
 * Created by alexander on 05.12.2015.
 */
public class CountingThread extends Thread {

    private static int numberOfThreads;
    private static volatile int currentNumber = 0;
    private int thisThreadNumber;

    public static void setNumberOfThreads(int numberOfThreads) {
        CountingThread.numberOfThreads = numberOfThreads;
    }

    @Override
    public void run() {
        synchronized (this.getClass()) {
            while (true) {
                if (currentNumber == thisThreadNumber) {
                    System.out.println("Thread " + thisThreadNumber);
                    currentNumber = (currentNumber + 1) % numberOfThreads;
                    this.getClass().notifyAll();
                }
                try {
                    this.getClass().wait();
                } catch (InterruptedException e) { }
            }
        }
    }

    public CountingThread(int thisThreadNumber) {
        this.thisThreadNumber = thisThreadNumber;
    }
}
