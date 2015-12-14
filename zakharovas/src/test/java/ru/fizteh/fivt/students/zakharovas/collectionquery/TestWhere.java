package ru.fizteh.fivt.students.zakharovas.collectionquery;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.core.Is.is;
import static ru.fizteh.fivt.students.zakharovas.collectionquery.impl.FromStmt.from;

/**
 * Created by alexander on 14.12.15.
 */
public class TestWhere {

    @Test
    public void testWhere() throws ReflectiveOperationException {
        List<Integer> result = from(Arrays.asList(1, 2, 3, 4, 5)).select(integer -> 2 * integer).
                where(i -> i > 3).stream().collect(Collectors.toList());
        assertThat(result.size(), is(2));
        assertThat(result, contains(8, 10));
    }

    @Test
    public void testLimit() throws Exception {
        List<Integer> result = from(Arrays.asList(1, 2, 3, 4, 5)).select(integer -> 2 * integer).
                where(i -> i > 0).limit(2).stream().collect(Collectors.toList());
        assertThat(result.size(), is(2));
        assertThat(result, contains(2, 4));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeLimit() throws Exception {
        List<Integer> result = from(Arrays.asList(1, 2, 3, 4, 5)).select(integer -> 2 * integer).
                where(i -> i > 0).limit(-1).stream().collect(Collectors.toList());
        assertThat(result.size(), is(2));
        assertThat(result, contains(2, 4));
    }

    @Test
    public void testOverLimit() throws Exception {
        List<Integer> result = from(Arrays.asList(1, 2, 3, 4, 5)).select(integer -> 2 * integer).
                where(i -> i > 0).limit(1000).stream().collect(Collectors.toList());
        assertThat(result.size(), is(5));
        assertThat(result, contains(2, 4, 6, 8, 10));
    }



}
