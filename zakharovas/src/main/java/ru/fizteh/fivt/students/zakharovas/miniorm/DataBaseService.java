package ru.fizteh.fivt.students.zakharovas.miniorm;

import com.google.common.base.CaseFormat;
import ru.fizteh.fivt.students.zakharovas.miniorm.annotations.Column;
import ru.fizteh.fivt.students.zakharovas.miniorm.annotations.PrimaryKey;
import ru.fizteh.fivt.students.zakharovas.miniorm.annotations.Table;
import ru.fizteh.fivt.students.zakharovas.miniorm.exceptions.DatabaseException;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexander on 14.12.15.
 */
public class DataBaseService<T> {
    private Class<T> typeClass;
    private List<AnnotatedField> columns;
    private Field primaryKey;
    private boolean hasTable = false;
    private String tableName;
    private final static String DATABASE_NAME = "jdbc:h2:~/database";


    public DataBaseService(Class<T> typeClass) throws DatabaseException, ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");
        columns = new ArrayList<>();
        this.typeClass = typeClass;
        Table table = typeClass.getAnnotation(Table.class);
        if (table == null) {
            throw new DatabaseException("Class should be annotated with Table");
        }
        tableName = table.name();
        if (tableName.equals("")) {
            tableName = typeClass.getSimpleName();
            tableName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, tableName);
        }
        for (Field field : typeClass.getDeclaredFields()) {
            if (field.getAnnotation(Column.class) != null) {
                columns.add(new AnnotatedField(field));
            }
            if (field.getAnnotation(PrimaryKey.class) != null) {
                if (field.getAnnotation(Column.class) == null) {
                    throw new DatabaseException("Primary key should be column");
                }
                if (primaryKey != null) {
                    throw new DatabaseException("Primary key should be one");
                }
                primaryKey = field;
            }
        }
        try(Connection connection = DriverManager.getConnection(DATABASE_NAME)) {
            try (ResultSet resultSet = connection.getMetaData()
                    .getTables(null, null,
                            CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_UNDERSCORE, tableName), null)){
                if (resultSet.next()) {
                    hasTable = true;
                }
            }
        }
    }


    public void createTable() throws DatabaseException, SQLException {
        if (hasTable) {
            throw new DatabaseException("table can be created only once for each class");
        }
        StringBuilder createRequest = new StringBuilder();
        createRequest.append("CREATE TABLE ").append(tableName).append(" (");
        for (AnnotatedField field: columns) {
            createRequest.append(field.getColumnName()).append(" ");
            createRequest.append(field.getSqlType()).append(" ");
            if (field.getField().isAnnotationPresent(PrimaryKey.class)) {
                createRequest.append("NOT NULL PRIMARY KEY ");
            }
            createRequest.append(" , ");
        }
        createRequest.deleteCharAt(createRequest.lastIndexOf(","));
        createRequest.append(")");
        try(Connection connection = DriverManager.getConnection(DATABASE_NAME)) {
            try(Statement statement = connection.createStatement()) {
                statement.execute(createRequest.toString());
                hasTable =true;
            }
        }




    }

    public void dropTable() throws DatabaseException, SQLException {
        if (!hasTable) {
            throw new DatabaseException("table should be created before");
        }
        try(Connection connection = DriverManager.getConnection(DATABASE_NAME)) {
            try (Statement statement = connection.createStatement()){
                statement.execute("DROP TABLE " + tableName);
                hasTable = false;
            }
        }

    }

    public <K> T queryById(K key) throws DatabaseException {
        if (!hasTable) {
            throw new DatabaseException("table should be created before");
        }
        if (primaryKey == null) {
            throw new DatabaseException("primary key should exist for delete");
        }
        if (!key.getClass().isInstance(primaryKey.getType())) {
            throw new IllegalArgumentException("key should have same type as primary key");
        }
        return null;

    }

    public List<T> queryForAll() throws DatabaseException {
        if (!hasTable) {
            throw new DatabaseException("table should be created before");
        }
        return null;
    }

    public void insert(T element) throws DatabaseException {
        if (!hasTable) {
            throw new DatabaseException("table should be created before");
        }
        if (primaryKey == null) {
            throw new DatabaseException("primary key should exist for delete");
        }

    }

    public void update(T element) throws DatabaseException {
        if (!hasTable) {
            throw new DatabaseException("table should be created before");
        }
        if (primaryKey == null) {
            throw new DatabaseException("primary key should exist for delete");
        }

    }

    public <K> void deleteByKey(K key) throws DatabaseException {
        if (!hasTable) {
            throw new DatabaseException("table should be created before");
        }
        if (primaryKey == null) {
            throw new DatabaseException("primary key should exist for delete");
        }
        if (!primaryKey.getType().isInstance(key)) {
            throw new IllegalArgumentException("key should have same type as primary key");
        }

    }

    public void delete(T line) throws IllegalAccessException, DatabaseException {
        if (!hasTable) {
            throw new DatabaseException("table should be created before");
        }
        Object key = primaryKey.get(line);
        deleteByKey(key);
    }


}
