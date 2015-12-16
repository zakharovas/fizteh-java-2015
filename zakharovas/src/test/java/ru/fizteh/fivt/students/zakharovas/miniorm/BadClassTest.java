package ru.fizteh.fivt.students.zakharovas.miniorm;

import org.junit.Test;
import ru.fizteh.fivt.students.zakharovas.miniorm.annotations.Column;
import ru.fizteh.fivt.students.zakharovas.miniorm.annotations.PrimaryKey;
import ru.fizteh.fivt.students.zakharovas.miniorm.annotations.Table;
import ru.fizteh.fivt.students.zakharovas.miniorm.exceptions.DatabaseException;

/**
 * Created by alexander on 17.12.15.
 */
public class BadClassTest {

    @Test(expected = DatabaseException.class)
    public void testTwoPrimaryKeys() throws Exception {
        DataBaseService<BadClass> dataBaseService = new DataBaseService<>(BadClass.class);

    }

    @Test(expected = DatabaseException.class)
    public void testNotColumnPrimaryKey() throws Exception{
        DataBaseService<BadClass1> dataBaseService = new DataBaseService<>(BadClass1.class);
    }

    @Test(expected = DatabaseException.class)
    public void testNotTableClass() throws Exception{
        DataBaseService<BadClass2> dataBaseService = new DataBaseService<>(BadClass2.class);
    }

    @Table
    public static class BadClass {
        @Column
        @PrimaryKey
        public Integer number;

        @Column
        @PrimaryKey
        public Integer number1;
    }

    @Table
    public static class BadClass1 {
        @PrimaryKey
        public Integer number;
    }


    public static class BadClass2 {
        @PrimaryKey
        public Integer number;
    }

}
