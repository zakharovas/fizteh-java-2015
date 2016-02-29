package ru.fizteh.fivt.students.zakharovas.collectionquery;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static ru.fizteh.fivt.students.zakharovas.collectionquery.Aggregates.count;
import static ru.fizteh.fivt.students.zakharovas.collectionquery.impl.FromStmt.from;

/**
 * Created by alexander on 14.12.15.
 */
public class    TestGroupBy {
    private List<TwoInt> input;
    private List<TwoInt> rightAnswer;

    @Before
    public void setUp() {
        input = new ArrayList<>();
        rightAnswer = Arrays.asList(new TwoInt(-1, 2), new TwoInt(1, 2),
                new TwoInt(2, 4), new TwoInt(-2, 4), new TwoInt(-2, 4), new TwoInt(2, 4),
                new TwoInt(-3, 2), new TwoInt(3, 2));
        List<Integer> list = Arrays.asList(-1, 1, 2, -2, -2, -3, 3, 2);
        list.forEach(element -> {
            input.add(new TwoInt(Math.abs(element), element));
        });


    }

    @Test
    public void testGroupBy() throws Exception{
        List<TwoInt> result = from(input).select(TwoInt.class, TwoInt::getS, count(TwoInt::getF))
                .where((s)->(true))
                .groupBy(TwoInt::getF)
                .stream().collect(Collectors.toList());
        assertThat(result.equals(rightAnswer), is(true));
    }

    @Test
    public void testHaving() throws Exception{
        input.add(new TwoInt(4, 4));
        List<TwoInt> result = from(input).select(TwoInt.class, TwoInt::getS, count(TwoInt::getF))
                .where((s)->(true))
                .groupBy(TwoInt::getF).having((element) -> (element.getF() < 4))
                .stream().collect(Collectors.toList());
        assertThat(result.equals(rightAnswer), is(true));
    }


}
