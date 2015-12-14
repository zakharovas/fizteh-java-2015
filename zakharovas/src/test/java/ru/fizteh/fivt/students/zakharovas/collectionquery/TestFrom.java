package ru.fizteh.fivt.students.zakharovas.collectionquery;

import org.junit.Test;
import ru.fizteh.fivt.students.zakharovas.collectionquery.impl.Tuple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.core.Is.is;
import static ru.fizteh.fivt.students.zakharovas.collectionquery.impl.FromStmt.from;

/**
 * Created by alexander on 14.12.15.
 */

public class TestFrom {


    @Test
    public void testSelectSimple() throws Exception {
        Iterable<Integer> result = from(Arrays.asList(1, 2, 3, 4, 5)).select(integer -> 2 * integer).execute();
        assertThat(result, contains(2, 4, 6, 8, 10));
    }

    @Test
    public void testSelectwithFunctions() throws Exception {
        Iterable<MyInteger> result = from(Arrays.asList(1, 2, 3, 4, 5)).select(MyInteger.class, integer -> 2 * integer).
                execute();
        List<Integer> realResults = StreamSupport.stream(result.spliterator(), false)
                .mapToInt(MyInteger::toInt).boxed().collect(Collectors.toList());
        assertThat(realResults, contains(2, 4, 6, 8, 10));
    }

    @Test
    public void testFromSubQuery() throws Exception {
        Iterable<Integer> result = from(from(Arrays.asList(1, 2, 3, 4, 5)).select(integer -> 2 * integer))
                .select(integer -> integer).execute();
        assertThat(result, contains(2, 4, 6, 8, 10));
    }

    @Test
    public void testTupleReturn() throws Exception {
        Iterable<Tuple<Integer, Integer>> result = from(Arrays.asList(1, -2, 3, -4, 5))
                .select(integer -> 2 * integer, Math::abs).execute();
        List<Integer> first = new ArrayList<>();
        List<Integer> second = new ArrayList<>();
        for (Tuple<Integer, Integer> element : result) {
            first.add(element.getFirst());
            second.add(element.getSecond());
        }
        assertThat(first, contains(2, -4, 6, -8, 10));
        assertThat(second, contains(1, 2, 3, 4, 5));
    }

    @Test
    public void testDistinctSelect() throws Exception {
        Iterable<Integer> result = from(Arrays.asList(1, 1, 1, 2, 3, 2, 4, 5)).selectDistinct(integer -> 2 * integer)
                .execute();
        assertThat(result, contains(2, 4, 6, 8, 10));
    }

    @Test
    public void testDistinctSelectToClass() throws Exception {
        Iterable<MyInteger> result = from(Arrays.asList(1, 1, 1, 2, 3, 2, 4, 5))
                .selectDistinct(MyInteger.class, integer -> 2 * integer).
                execute();
        List<Integer> realResults = StreamSupport.stream(result.spliterator(), false)
                .mapToInt(MyInteger::toInt).boxed().collect(Collectors.toList());
        assertThat(realResults, contains(2, 4, 6, 8, 10));
    }

    @Test
    public void testFromStream() throws Exception {
        Stream<Integer> result = from(Stream.of(1, 2, 3, 4, 5)).select(integer -> 2 * integer).stream();
        assertThat(result.collect(Collectors.toList()), contains(2, 4, 6, 8, 10));
    }

    @Test
    public void testWhere() throws ReflectiveOperationException {
        List<Integer> result = from(Arrays.asList(1, 2, 3, 4, 5)).select(integer -> 2 * integer).
                where(i -> i > 3).stream().collect(Collectors.toList());
        assertThat(result.size(), is(2));
        assertThat(result, contains(8, 10));

    }



    @Test
    public void testGroupBy() {

    }

    @Test
    public void testOrderBy() {

    }

    @Test
    public void testLimit() {

    }

    @Test
    public void testUnion() {

    }

    public static class MyInteger {
        private Integer number;

        public MyInteger(Integer number) {
            this.number = number;
        }

        public Integer toInt() {
            return number;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof MyInteger)) {
                return false;
            }
            MyInteger another = (MyInteger) obj;
            return number.equals(another.number);
        }

        @Override
        public int hashCode(){
            return number.hashCode();
        }
    }

}
