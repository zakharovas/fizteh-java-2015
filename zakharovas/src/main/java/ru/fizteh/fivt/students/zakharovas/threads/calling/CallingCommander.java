package ru.fizteh.fivt.students.zakharovas.threads.calling;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by alexander on 05.12.2015.
 */
public class CallingCommander {

    private static final String ARE_YOU_READY = "Are you ready?";
    private CyclicBarrier barrier;
    private boolean callingFinished = false;
    private int numberOfThreads;
    private List<AnsweringThread> listOfThreads;

    public CallingCommander(int numberOfThreads) {
        listOfThreads = new ArrayList<>();
        this.numberOfThreads = numberOfThreads;
        barrier = new CyclicBarrier(numberOfThreads + 1, new Runnable() {
            @Override
            public void run() {
                callingFinished = true;
                for (int i = 0; i < listOfThreads.size(); ++i) {
                    callingFinished &= listOfThreads.get(i).getCallingResult();
                    listOfThreads.get(i);
                }
                if (!callingFinished) {
                    System.out.println(ARE_YOU_READY);
                }
            }
        });
    }


    public boolean isCallingFinished() {
        return callingFinished;
    }

    public CyclicBarrier getBarrier() {
        return barrier;
    }

    public void start() {
        System.out.println(ARE_YOU_READY);
        for (int i = 0; i < numberOfThreads; ++i) {
            listOfThreads.add(new AnsweringThread(this));
            listOfThreads.get(i).start();
        }
        while (!callingFinished) {
            try {
                barrier.await();
            } catch (Exception e) {
            }
        }
    }
}
