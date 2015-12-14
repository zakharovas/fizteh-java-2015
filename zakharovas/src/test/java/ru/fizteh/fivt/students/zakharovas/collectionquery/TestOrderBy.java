package ru.fizteh.fivt.students.zakharovas.collectionquery;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static ru.fizteh.fivt.students.zakharovas.collectionquery.impl.FromStmt.from;

/**
 * Created by alexander on 14.12.15.
 */
public class TestOrderBy {
    List<Integer> data;

    @Before
    public void setUp() {
        data = Arrays.asList(1, 2, 3, 4, 5);
        Collections.shuffle(data);
    }

    @Test
    public void testOrderByDesc() throws Exception {
        List<Integer> result = from(data).select(integer -> 2 * integer).
                where(i -> i > 0).orderBy(OrderByConditions.desc(Integer::intValue)).stream()
                .collect(Collectors.toList());
        assertThat(result, contains(10, 8, 6, 4, 2));
    }

    @Test
    public void testOrderByAsc() throws Exception {
        List<Integer> result = from(data).select(integer -> 2 * integer).
                where(i -> i > 0).orderBy(OrderByConditions.asc(Integer::intValue)).stream()
                .collect(Collectors.toList());
        assertThat(result, contains(2, 4, 6, 8, 10));

    }

}
