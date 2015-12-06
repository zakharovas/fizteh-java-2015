package ru.fizteh.fivt.students.zakharovas.threads.blockingqueue;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by alexander on 06.12.2015.
 */
public class MyBlockingQueueTest {

    MyBlockingQueue<Integer> queue;

    @Before
    public void setUp() {
        queue = new MyBlockingQueue<>(5);
    }

    @Test
    public void testBlockingOnEmpty() throws Exception {
        MyBlockingQueue<Integer> queue = new MyBlockingQueue<>(5);
        Thread testingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                queue.take(5);
            }
        });
        testingThread.start();
        Thread.sleep(50);
        assertThat(testingThread.isAlive(), is(true));
        testingThread.interrupt();
    }

    @Test
    public void testBlockingOnOverFlowing() throws Exception {
        MyBlockingQueue<Integer> queue = new MyBlockingQueue<>(5);
        List<Integer> tooLargeList = Arrays.asList(1, 2, 3, 4, 5, 6);
        Thread testingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                queue.offer(tooLargeList);
            }
        });
        testingThread.start();
        Thread.sleep(50);
        assertThat(testingThread.isAlive(), is(true));
        testingThread.interrupt();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadSize() {
        queue.take(-10);
    }
}