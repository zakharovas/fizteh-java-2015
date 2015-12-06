package ru.fizteh.fivt.students.zakharovas.threads.blockingqueue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by alexander on 05.12.2015.
 */
public class MyBlockingQueue<T> {
    private Queue<T> realQueue = new LinkedList<>();
    //writingLock и readingLock делают так, чтобы запросы на добавление(считывание) из очереди происходили в
    //правильном порядке.
    //LockForOneElement делает так что одновременно к массиву очереди имел доступ только один поток
    private Lock writingLock = new ReentrantLock(true);
    private Lock readingLock = new ReentrantLock(true);
    private Lock lockForOneElement = new ReentrantLock(true);
    private Condition notFullQueueCondition = lockForOneElement.newCondition();
    private Condition notEmptyQueueCondition = lockForOneElement.newCondition();
    private int maxSize;

    public List<T> take(int n) throws IllegalArgumentException {
        if (n < 0) {
            throw new IllegalArgumentException("Argument was negative number");
        }
        try {
            readingLock.lock();
            List<T> answer = new ArrayList<>();
            for (int i = 0; i < n; ++i) {
                try {
                    lockForOneElement.lock();
                    while (realQueue.size() == 0) {
                        try {
                            notEmptyQueueCondition.await();
                        } catch (InterruptedException e) {
                            return null;
                        }
                    }
                    answer.add(realQueue.poll());
                    notFullQueueCondition.signalAll();
                } finally {
                    lockForOneElement.unlock();
                }
            }
            return answer;
        } finally {
            readingLock.unlock();
        }
    }

    public <E extends T> void offer(List<E> list) {
        try {
            writingLock.lock();
            for (E element : list) {
                try {
                    lockForOneElement.lock();
                    while (realQueue.size() == maxSize) {
                        try {
                            notFullQueueCondition.await();
                        } catch (InterruptedException e) {
                            return;

                        }
                    }
                    realQueue.add(element);
                    notEmptyQueueCondition.signal();
                } finally {
                    lockForOneElement.unlock();
                }
            }
        } finally {
            writingLock.unlock();
        }
    }

    //not ready
    public List<T> take(int n, long timeout) throws IllegalArgumentException {
        if (n < 0 || timeout < 0) {
            throw new IllegalArgumentException("One of the arguments was negative number");
        }

        return null;
    }

    public <E extends T> boolean offer(List<E> list, long timeout) {
        if (timeout < 0) {
            throw new IllegalArgumentException("Timeout was negative number");
        }
        return false;
    }

    public MyBlockingQueue(int maxSize) {
        this.maxSize = maxSize;
    }


}
