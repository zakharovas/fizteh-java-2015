package ru.fizteh.fivt.students.zakharovas.collectionquery;

import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static ru.fizteh.fivt.students.zakharovas.collectionquery.Aggregates.avg;
import static ru.fizteh.fivt.students.zakharovas.collectionquery.Aggregates.count;
import static ru.fizteh.fivt.students.zakharovas.collectionquery.CollectionQuery.Statistics;
import static ru.fizteh.fivt.students.zakharovas.collectionquery.CollectionQuery.Student;
import static ru.fizteh.fivt.students.zakharovas.collectionquery.CollectionQuery.Student.student;
import static ru.fizteh.fivt.students.zakharovas.collectionquery.Sources.list;
import static ru.fizteh.fivt.students.zakharovas.collectionquery.impl.FromStmt.from;

/**
 * Created by alexander on 14.12.15.
 */

public class CQLtests {


    @Test
    public void testSelectSimple() throws Exception {
        Iterable<CollectionQuery.Statistics> statistics = from(list(student("ivanov", LocalDate.parse("1986-08-06"), "494"),
                student("sidorov", LocalDate.parse("1986-08-06"), "495"),
                student("smith", LocalDate.parse("1986-08-06"), "495"),
                student("petrov", LocalDate.parse("2006-08-06"), "494")))
                .select(Statistics.class, Student::getName,
                        count(Student::getGroup), avg(Student::age)).execute();
        System.out.println(statistics);
        List<Statistics> rightAnswer = Arrays.asList(new Statistics("ivanov", 4, 24d), new Statistics("sidorov", 4, 24.0), new Statistics("smith", 4, 24d), new Statistics("petrov", 4, 24d));
        List<Statistics> answer = StreamSupport.stream(statistics.spliterator(), false).collect(Collectors.toList());

    }

    @Test
    public void testWhere() {

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

    @Test
    public void testDistinctSelect() {

    }


}
