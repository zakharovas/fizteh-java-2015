package ru.fizteh.fivt.students.zakharovas.collectionquery;

import ru.fizteh.fivt.students.zakharovas.collectionquery.impl.Tuple;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static ru.fizteh.fivt.students.zakharovas.collectionquery.Aggregates.avg;
import static ru.fizteh.fivt.students.zakharovas.collectionquery.Aggregates.count;
import static ru.fizteh.fivt.students.zakharovas.collectionquery.CollectionQuery.Student.student;
import static ru.fizteh.fivt.students.zakharovas.collectionquery.Sources.list;
import static ru.fizteh.fivt.students.zakharovas.collectionquery.impl.FromStmt.from;

/**
 * @author akormushin
 */
public class CollectionQuery {

    /**
     * Make this code work!
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {
        Iterable<Statistics> statistics =
                from(list(
                        student("ivanov", LocalDate.parse("1986-08-06"), "494"),
                        student("sidorov", LocalDate.parse("1986-08-06"), "495"),
                        student("smith", LocalDate.parse("1986-08-06"), "495"),
                        student("petrov", LocalDate.parse("2006-08-06"), "494")))
                        .select(Statistics.class, Student::getGroup, count(Student::getGroup), avg(Student::age))
                        .execute();
        System.out.println(statistics);

        Iterable<Tuple<String, String>> mentorsByStudent =
                from(list(student("ivanov", LocalDate.parse("1985-08-06"), "494")))
                .join(list(new Group("494", "mr.sidorov")))
                .on((s, g) -> Objects.equals(s.getGroup(), g.getGroup()))
                .select(sg -> sg.getFirst().getName(), sg -> sg.getSecond().getMentor())
                .execute();
        System.out.println(mentorsByStudent);
    }


    public static class Student {
        private final String name;

        private final LocalDate dateOfBith;

        private final String group;

        public String getName() {
            return name;
        }

        public Student(String name, LocalDate dateOfBith, String group) {
            this.name = name;
            this.dateOfBith = dateOfBith;
            this.group = group;
        }

        public LocalDate getDateOfBith() {
            return dateOfBith;
        }

        public String getGroup() {
            return group;
        }

        public long age() {
            return ChronoUnit.YEARS.between(getDateOfBith(), LocalDateTime.now());
        }

        public static Student student(String name, LocalDate dateOfBith, String group) {
            return new Student(name, dateOfBith, group);
        }
    }

    public static class Group {
        private final String group;
        private final String mentor;

        public Group(String group, String mentor) {
            this.group = group;
            this.mentor = mentor;
        }

        public String getGroup() {
            return group;
        }

        public String getMentor() {
            return mentor;
        }
    }


    public static class Statistics {

        private final String group;
        private final Integer count;
        private final Double age;

        public String getGroup() {
            return group;
        }

        public Integer getCount() {
            return count;
        }

        public Double getAge() {
            return age;
        }

        public Statistics(String group, Integer count, Double age) {
            this.group = group;
            this.count = count;
            this.age = age;
        }

        @Override
        public String toString() {
            return "Statistics{"
                    + "group='" + group + '\''
                    + ", count=" + count
                    + ", age=" + age
                    + '}';
        }
    }

}
