package ru.fizteh.fivt.students.zakharovas.collectionquery;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static ru.fizteh.fivt.students.zakharovas.collectionquery.impl.FromStmt.from;

/**
 * Created by alexander on 14.12.15.
 */
public class TestJoin {

    private List<TwoInt> first;
    private List<TwoInt> second;
    private List<TwoInt> rightAnswer;

    @Before
    public void setUp() {
        first = new ArrayList<>();
        second = new ArrayList<>();
        rightAnswer = Arrays.asList(new TwoInt(1, -1), new TwoInt(1, 1),
                new TwoInt(2, 2), new TwoInt(2, -2), new TwoInt(3, -3), new TwoInt(3, 3));
        List<Integer> list = Arrays.asList(-1, 1, 2, -2, -3, 3);
        list.forEach(element -> {
            first.add(new TwoInt(element, element + 10));
            second.add(new TwoInt(Math.abs(element), element));
        });
    }

    @Test
    public void testJoinIterable() throws Exception {
        Stream<TwoInt> result = from(first).join(from(second).select(element -> element).execute())
                .on(TwoInt::getF, TwoInt::getF)
                .select(TwoInt.class, (e) -> (e.getFirst().getF()), (e) -> (e.getSecond().getS())).stream();
        assertThat(checkResult(result), is(true));
    }



    @Test
    public void testJoinStream() throws Exception {
        Stream<TwoInt> result = from(first).join(from(second).select(element -> element).stream())
                .on(TwoInt::getF, TwoInt::getF)
                .select(TwoInt.class, (e) -> (e.getFirst().getF()), (e) -> (e.getSecond().getS())).stream();
        assertThat(checkResult(result), is(true));
    }

    @Test
    public void testJoinSubQuery() throws Exception {
        Stream<TwoInt> result = from(first).join(from(second).select(element -> element))
                .on(TwoInt::getF, TwoInt::getF)
                .select(TwoInt.class, (e) -> (e.getFirst().getF()), (e) -> (e.getSecond().getS())).stream();
        assertThat(checkResult(result), is(true));
    }

    @Test
    public void testJoinOnBiPredicate() throws Exception {
        Stream<TwoInt> result = from(first).join(from(second).select(element -> element).execute())
                .on((f, s) -> (f.getF().equals(s.getF())))
                .select(TwoInt.class, (e) -> (e.getFirst().getF()), (e) -> (e.getSecond().getS())).stream();
        assertThat(checkResult(result), is(true));
    }

    private boolean checkResult(Stream<TwoInt> result) {
        List<TwoInt> cqlResult = result.collect(Collectors.toList());
        return cqlResult.equals(rightAnswer);
    }

    public static class TwoInt {
        private Integer f;
        private Integer s;

        public Integer getF() {
            return f;
        }

        public Integer getS() {
            return s;
        }

        public TwoInt(Integer f, Integer s) {
            this.f = f;
            this.s = s;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof TwoInt)) {
                return false;
            }
            TwoInt another = (TwoInt) obj;
            return f.equals(another.f) && s.equals(another.s);
        }

        @Override
        public int hashCode() {
            return (f.hashCode() + s.hashCode());
        }

    }
}
