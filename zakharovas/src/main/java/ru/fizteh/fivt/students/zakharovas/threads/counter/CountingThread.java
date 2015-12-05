package ru.fizteh.fivt.students.zakharovas.threads.counter;

/**
 * Created by alexander on 05.12.2015.
 */
public class CountingThread extends Thread {

    public static int numberOfThreads;
    private final static Object mutex = new Object();
    private static volatile int currentNumber = 0;
    private int thisThreadNumber;

    @Override
    public void run() {
        synchronized (mutex) {
            while (true) {
                if (currentNumber == thisThreadNumber) {
                    System.out.println("Thread " + thisThreadNumber);
                    currentNumber = (currentNumber + 1) % numberOfThreads;
                    mutex.notifyAll();
                 }
                try {
                    mutex.wait();
                } catch (InterruptedException e) {}
            }
        }
    }

    public CountingThread(int thisThreadNumber) {
        this.thisThreadNumber = thisThreadNumber;
    }
}
