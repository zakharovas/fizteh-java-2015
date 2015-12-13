package ru.fizteh.fivt.students.zakharovas.threads.blockingqueue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
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
    //LockForOperation делает так что одновременно к массиву очереди имел доступ только один поток
    private Lock writingLock = new ReentrantLock(true);
    private Lock readingLock = new ReentrantLock(true);
    private Lock lockForOperation = new ReentrantLock(true);
    private Condition notFullQueueCondition = lockForOperation.newCondition();
    private Condition notEmptyQueueCondition = lockForOperation.newCondition();
    private int maxSize;

    public List<T> take(int n) throws InterruptedException, IllegalArgumentException {
        if (n < 0) {
            throw new IllegalArgumentException("Argument was negative number");
        }
        if (n == 0) {
            return new ArrayList<T>();
        }
        readingLock.lockInterruptibly();
        try {
            lockForOperation.lockInterruptibly();
            try {
                List<T> answer = new ArrayList<>();
                while (realQueue.size() < n) {
                    notEmptyQueueCondition.await();
                }
                for (int i = 0; i < n; ++i) {
                    answer.add(realQueue.poll());
                }
                notFullQueueCondition.signalAll();
                return answer;
            } finally {
                lockForOperation.unlock();
            }
        } finally {
            readingLock.unlock();
        }
    }

    public <E extends T> boolean offer(List<E> list) throws InterruptedException {
        writingLock.lockInterruptibly();
        try {
            lockForOperation.lockInterruptibly();
            try {
                while (realQueue.size() + list.size() > maxSize) {
                    notFullQueueCondition.await();
                }
                realQueue.addAll(list);
                notEmptyQueueCondition.signal();
            } finally {
                lockForOperation.unlock();
            }
        } finally {
            writingLock.unlock();
        }
        return true;
    }

    public List<T> take(int n, long timeout) throws InterruptedException, IllegalArgumentException {
        if (n < 0 || timeout < 0) {
            throw new IllegalArgumentException("One of the arguments was negative number");
        }
        if (n == 0) {
            return new ArrayList<T>();
        }
        try {
            long currentTime = System.currentTimeMillis();
            if (readingLock.tryLock(timeout, TimeUnit.MILLISECONDS)) {
                try {
                    timeout -= (System.currentTimeMillis() - currentTime);
                    currentTime = System.currentTimeMillis();
                    if (lockForOperation.tryLock(timeout, TimeUnit.MILLISECONDS)) {
                        try {
                            List<T> answer = new ArrayList<>();
                            while (realQueue.size() < n) {
                                timeout -= (System.currentTimeMillis() - currentTime);
                                currentTime = System.currentTimeMillis();
                                if (!notEmptyQueueCondition.await(timeout, TimeUnit.MILLISECONDS)) {
                                    throw new TimeoutException();
                                }
                            }
                            for (int i = 0; i < n; ++i) {
                                answer.add(realQueue.poll());
                            }
                            notFullQueueCondition.signalAll();
                            return answer;
                        } finally {
                            lockForOperation.unlock();
                        }
                    } else {
                        throw new TimeoutException();
                    }
                } finally {
                    readingLock.unlock();
                }
            } else {
                throw new TimeoutException();
            }
        } catch (TimeoutException e) {
            return null;
        }
    }

    public <E extends T> boolean offer(List<E> list, long timeout) throws InterruptedException {
        if (timeout < 0) {
            throw new IllegalArgumentException("Timeout was negative number");
        }
        try {
            long currentTime = System.currentTimeMillis();
            if (writingLock.tryLock(currentTime, TimeUnit.MILLISECONDS)) {
                try {
                    timeout -= System.currentTimeMillis() - currentTime;
                    currentTime = System.currentTimeMillis();
                    if (lockForOperation.tryLock(timeout, TimeUnit.MILLISECONDS)) {
                        try {
                            while (realQueue.size() + list.size() > maxSize) {
                                timeout -= System.currentTimeMillis() - currentTime;
                                currentTime = System.currentTimeMillis();
                                if (!notFullQueueCondition.await(timeout, TimeUnit.MILLISECONDS)) {
                                    throw new TimeoutException();
                                }
                            }
                            realQueue.addAll(list);
                            notEmptyQueueCondition.signal();
                        } finally {
                            lockForOperation.unlock();
                        }
                    } else {
                        throw new TimeoutException();
                    }
                } finally {
                    writingLock.unlock();
                }
            } else {
                throw new TimeoutException();
            }
        } catch (TimeoutException e) {
            return false;
        }
        return true;
    }

    public MyBlockingQueue(int maxSize) {
        this.maxSize = maxSize;
    }

    public MyBlockingQueue() {
        maxSize = 100;
    }


}
