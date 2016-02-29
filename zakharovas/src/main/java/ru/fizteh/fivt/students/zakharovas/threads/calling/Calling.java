package ru.fizteh.fivt.students.zakharovas.threads.calling;


/**
 * Created by alexander on 05.12.2015.
 */
public class Calling {

    public static void main(String[] args) {
        int numberOfThreads = 0;
        try {
            if (args.length == 0) {
                throw new IllegalArgumentException();
            }
            numberOfThreads = Integer.valueOf(args[0]);
            if (numberOfThreads <= 0) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            System.err.println("There should be positive number as command line argument");
            System.exit(1);
        }
        CallingCommander commander = new CallingCommander(numberOfThreads);
        commander.start();
    }
}
