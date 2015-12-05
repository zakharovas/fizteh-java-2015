package ru.fizteh.fivt.students.zakharovas.threads.calling;

import java.util.Random;

/**
 * Created by alexander on 05.12.2015.
 */
public class AnsweringThread extends Thread {

    private boolean callingResult;
    private Random random = new Random();
    private CallingCommander commander;

    public AnsweringThread(CallingCommander callingCommander) {
        this.commander = callingCommander;
    }

    public boolean getCallingResult() {
        return callingResult;
    }

    @Override
    public void run() {
        while (!commander.isCallingFinished()) {
            if (random.nextInt(10) != 0) {
                callingResult = true;
                System.out.println("YES");
            } else {
                callingResult = false;
                System.out.println("NO");
            }
            try {
                commander.getBarrier().await();
            } catch (Exception e) {
            }
        }
    }
}
