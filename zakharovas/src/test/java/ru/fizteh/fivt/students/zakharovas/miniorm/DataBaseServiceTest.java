package ru.fizteh.fivt.students.zakharovas.miniorm;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.fizteh.fivt.students.zakharovas.miniorm.annotations.Column;
import ru.fizteh.fivt.students.zakharovas.miniorm.annotations.PrimaryKey;
import ru.fizteh.fivt.students.zakharovas.miniorm.annotations.Table;
import ru.fizteh.fivt.students.zakharovas.miniorm.exceptions.DatabaseException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;


/**
 * Created by alexander on 16.12.15.
 */
public class DataBaseServiceTest {

    List<AllInOne> input;
    DataBaseService<AllInOne> dataBaseService;

    @Before
    public void setUp() throws SQLException, DatabaseException, ClassNotFoundException {
        dataBaseService = new DataBaseService<>(AllInOne.class);
        if (!dataBaseService.isTableCreated()) {
            dataBaseService.createTable();
        }
        input = new ArrayList<>();
        List<Integer> baseForInput = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            baseForInput.add(i);
        }
        baseForInput.forEach(element -> {
            input.add(new AllInOne(element, element.shortValue(), element.byteValue(), element.toString(),
                    element.longValue(), element.toString().charAt(0), element.doubleValue(), element.floatValue()));
        });
        for (AllInOne element : input) {
            dataBaseService.insert(element);
        }
    }

    @After
    public void tryToDrop() throws DatabaseException, SQLException {
        if (dataBaseService.isTableCreated()) {
            dataBaseService.dropTable();
        }

    }

    @Test(expected = SQLException.class)
    public void testDoubleInsert() throws Exception {
        dataBaseService.insert(input.get(0));
        dataBaseService.insert(input.get(0));
    }

    @Test
    public void testQueryById() throws Exception {
        AllInOne allInOne = dataBaseService.queryById(1);
        assertThat(allInOne, equalTo(input.get(1)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testQueryByBadKey() throws Exception {
        AllInOne allInOne = dataBaseService.queryById(1d);
    }

    @Test
    public void testQueryForAll() throws Exception {
        List<AllInOne> newList = dataBaseService.queryForAll();
        assertThat(newList, equalTo(input));
    }

    @Test
    public void testDrop() throws Exception {
        dataBaseService.dropTable();
        assertThat(dataBaseService.isTableCreated(), is(false));
    }

    @Test
    public void testDelete() throws Exception {
        dataBaseService.delete(input.get(0));
        List<AllInOne> newList = dataBaseService.queryForAll();
        assertThat(newList, equalTo(input.subList(1, 10)));
    }

    @Test
    public void testInsert() throws Exception {
        dataBaseService.delete(input.get(0));
        dataBaseService.insert(input.get(0));
        List<AllInOne> newList = dataBaseService.queryForAll();
        assertThat(newList, equalTo(input));
    }

    @Test
    public void testUpdate() throws Exception {
        input.get(0).name = "Test";
        dataBaseService.update(input.get(0));
        List<AllInOne> newList = dataBaseService.queryForAll();
        assertThat(newList, equalTo(input));
    }


    @Table
    public static class AllInOne {
        @Column
        @PrimaryKey
        public Integer id;

        @Column
        public Short shortNumber;

        @Column
        public Byte byteNumber;

        @Column
        public String name;

        @Column
        public Long longNumber;

        @Column
        public Double doubleNumber;

        @Column Float floatNumber;

        @Column
        public Character c;

        public AllInOne(Integer id, Short shortNumber, Byte byteNumber, String name, Long longNumber, Character c,
                        Double doubleNumber, Float floatNumber) {
            this.id = id;
            this.shortNumber = shortNumber;
            this.byteNumber = byteNumber;
            this.name = name;
            this.c = c;
            this.longNumber = longNumber;
            this.doubleNumber = doubleNumber;
            this.floatNumber = floatNumber;
        }

        public AllInOne() {
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof AllInOne)) {
                return false;
            }
            AllInOne another = (AllInOne) obj;
            return id.equals(another.id) && shortNumber.equals(another.shortNumber)
                    && byteNumber.equals(another.byteNumber) && name.equals(another.name)
                    && longNumber.equals(another.longNumber);
        }
    }



}