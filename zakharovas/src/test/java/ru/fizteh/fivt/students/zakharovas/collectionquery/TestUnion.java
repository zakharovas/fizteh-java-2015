package ru.fizteh.fivt.students.zakharovas.collectionquery;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static ru.fizteh.fivt.students.zakharovas.collectionquery.impl.FromStmt.from;

/**
 * Created by alexander on 14.12.15.
 */
public class TestUnion {
    @Test
    public void testUnion() throws Exception {
        List<Integer> input = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> result = from(input).select(integer -> 2 * integer).
                where(i -> i > 3).union()
                .from(input)
                .select(integer -> 2 * integer).where(i -> i <=3)
                .stream().collect(Collectors.toList());
        assertThat(result, contains(8, 10, 2, 4, 6));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalUnion() throws Exception{
        List<Integer> input = Arrays.asList(1, 2, 3, 4, 5);
        Stream<TwoInt> s = from(input).select(integer -> 2 * integer).
                where(i -> i > 3).union()
                .from(input)
                .select(TwoInt.class, integer -> 2 * integer, integer -> 2 * integer).where(i -> i <=3)
                .stream();
    }
}
