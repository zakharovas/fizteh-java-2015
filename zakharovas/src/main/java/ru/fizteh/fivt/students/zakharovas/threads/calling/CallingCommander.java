package ru.fizteh.fivt.students.zakharovas.threads.calling;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by alexander on 05.12.2015.
 */
public class CallingCommander {
    private static final int PROBABILITY_OF_SUCCESS_NUMERATOR = 9;
    private static final int PROBABILITY_OF_SUCCESS_DENOMINATOR = 10;
    private static final String ARE_YOU_READY = "Are you ready?";
    private CyclicBarrier barrier;
    private boolean callingFinished = false;
    private int numberOfThreads;
    private List<AnsweringThread> listOfThreads;

    public CallingCommander(int numberOfThreads) {
        listOfThreads = new ArrayList<>();
        this.numberOfThreads = numberOfThreads;
        barrier = new CyclicBarrier(numberOfThreads + 1, () -> {
            callingFinished = true;
            for (AnsweringThread answeringThread : listOfThreads) {
                callingFinished &= answeringThread.callingResult;
            }
            if (!callingFinished) {
                System.out.println(ARE_YOU_READY);
            }
        });
    }

    public void start() {
        System.out.println(ARE_YOU_READY);
        for (int i = 0; i < numberOfThreads; ++i) {
            listOfThreads.add(new AnsweringThread());
            listOfThreads.get(i).start();
        }
        while (!callingFinished) {
            try {
                barrier.await();
            } catch (Exception e) {
            }
        }
    }

    private class AnsweringThread extends Thread {
        private boolean callingResult;
        private Random random = new Random();

        @Override
        public void run() {
            while (!callingFinished) {
                if (random.nextInt(PROBABILITY_OF_SUCCESS_DENOMINATOR) < PROBABILITY_OF_SUCCESS_NUMERATOR) {
                    callingResult = true;
                    System.out.println("YES");
                } else {
                    callingResult = false;
                    System.out.println("NO");
                }
                try {
                    barrier.await();
                } catch (Exception e) {
                }
            }
        }
    }
}
