package ru.fizteh.fivt.students.zakharovas.threads.blockingqueue;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

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
        Thread testingThread = new Thread(() -> {
            try {
                queue.take(5);
            } catch (InterruptedException e) {
            }
        });
        testingThread.start();
        testingThread.join(50);
        assertThat(testingThread.isAlive(), is(true));
        testingThread.interrupt();
    }

    @Test
    public void testBlockingOnOverFlowing() throws Exception {
        MyBlockingQueue<Integer> queue = new MyBlockingQueue<>(5);
        List<Integer> tooLargeList = Arrays.asList(1, 2, 3, 4, 5, 6);
        Thread testingThread = new Thread(() -> {
            try {
                queue.offer(tooLargeList);
            } catch (InterruptedException e) {
            }
        });
        testingThread.start();
        testingThread.join(50);
        assertThat(testingThread.isAlive(), is(true));
        testingThread.interrupt();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadSize() throws InterruptedException {
        queue.take(-10);
    }

    @Test(timeout = 1000)
    public void testSingleThreadQueue() throws InterruptedException {
        List<Integer> firstList = Arrays.asList(1, 2, 3);
        List<Integer> secondList = Arrays.asList(6, 5);
        queue.offer(firstList);
        queue.offer(secondList);
        List<Integer> answerList1 = queue.take(2);
        List<Integer> answerList2 = queue.take(3);
        assertThat(answerList1.size(), is(2));
        assertThat(answerList1, contains(1, 2));
        assertThat(answerList2.size(), is(3));
        assertThat(answerList2, contains(3, 6, 5));
    }

    @Test(timeout = 1000)
    public void testMultiThreadQueueTaking() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        List<Integer> answerList1;
        List<Integer> answerList2;
        Future<List<Integer>> answer1 = executor.submit(() -> queue.take(2));
        Future<List<Integer>> answer2 = executor.submit(() -> queue.take(3));
        List<Integer> firstList = Arrays.asList(1, 2, 3);
        List<Integer> secondList = Arrays.asList(6, 5);
        queue.offer(firstList);
        queue.offer(secondList);
        answerList1 = answer1.get();
        answerList2 = answer2.get();
        assertThat(answerList1.size(), is(2));
        assertThat(answerList2.size(), is(3));
        answerList1.addAll(answerList2);
        assertThat(answerList1, containsInAnyOrder(1, 2, 3, 6, 5));
    }

    @Test(timeout = 1000)
    public void testMultiThreadOffering() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        List<Integer> answerList;
        executor.submit(() -> {
            try {
                queue.offer(Arrays.asList(1, 2, 3));
            } catch (InterruptedException e) {
            }
        });
        executor.submit(() -> {
            try {
                queue.offer(Arrays.asList(6, 5));
            } catch (InterruptedException e) {
            }
        });
        answerList = queue.take(5);
        assertThat(answerList.size(), is(5));
        assertThat(answerList, containsInAnyOrder(1, 2, 3, 6, 5));
    }

    @Test(timeout = 1000)
    public void testTimeoutMethods() throws InterruptedException {
        List<Integer> firstList = Arrays.asList(1, 2, 3);
        List<Integer> secondList = Arrays.asList(6, 5);
        assertThat(queue.offer(firstList, 500), is(true));
        assertThat(queue.offer(secondList, 500), is(true));
        List<Integer> answerList1 = queue.take(2, 500);
        List<Integer> answerList2 = queue.take(3, 500);
        assertThat(answerList1.size(), is(2));
        assertThat(answerList1, contains(1, 2));
        assertThat(answerList2.size(), is(3));
        assertThat(answerList2, contains(3, 6, 5));
    }

    @Test(timeout = 200)
    public void testTimeoutTake() throws InterruptedException {
        assertThat(queue.take(10, 100), nullValue());
    }

    @Test(timeout = 200)
    public void testTimeoutOffer() throws InterruptedException {
        assertThat(queue.offer(Arrays.asList(1, 2, 3, 4, 5, 6), 100), is(false));
    }

    @Test(timeout = 1000)
    public void testTimeoutOn2Threads() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<Boolean> firstResult = executorService.submit(() ->
                queue.offer(Arrays.asList(1, 2, 3, 4, 5, 6), 100));
        Future<Boolean> secondResult = executorService.submit(() ->
                queue.offer(Arrays.asList(11, 12, 13, 14, 15)));
        List<Integer> result = queue.take(5);
        assertThat(firstResult.get(), is(false));
        assertThat(secondResult.get(), is(true));
        assertThat(result, contains(11, 12, 13, 14, 15));

    }

}
