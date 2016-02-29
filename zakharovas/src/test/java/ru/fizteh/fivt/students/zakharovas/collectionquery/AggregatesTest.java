package ru.fizteh.fivt.students.zakharovas.collectionquery;

import org.junit.Before;
import org.junit.Test;
import ru.fizteh.fivt.students.zakharovas.collectionquery.impl.aggregators.AvgFunction;
import ru.fizteh.fivt.students.zakharovas.collectionquery.impl.aggregators.CountFunction;
import ru.fizteh.fivt.students.zakharovas.collectionquery.impl.aggregators.MaxFunction;
import ru.fizteh.fivt.students.zakharovas.collectionquery.impl.aggregators.MinFunction;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by alexander on 14.12.15.
 */
public class AggregatesTest {

    private List<Integer> inputData;
    private Function<Integer, Integer> function;

    @Before
    public void setUp() {
        inputData = Arrays.asList(1, 2, -1, 3, - 3, -14, 24, 10);
        function = (number) -> number;
    }

    @Test
    public void testMin() {
        assertThat(new MinFunction<>(function).applyOnList(inputData), is(Collections.min(inputData)));
    }

    @Test
    public void testMax() {
        assertThat(new MaxFunction<>(function).applyOnList(inputData), is(Collections.max(inputData)));

    }

    @Test
    public void testCount() {
        assertThat(new CountFunction<Integer>().applyOnList(inputData), is(inputData.size()));

    }

    @Test
    public void testAvg() {
        assertThat(new AvgFunction<>(function).applyOnList(inputData),
                is(inputData.stream().mapToInt(i -> i).average().getAsDouble()));
    }
}
