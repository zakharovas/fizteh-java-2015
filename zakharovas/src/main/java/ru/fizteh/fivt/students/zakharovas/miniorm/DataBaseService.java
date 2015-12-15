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
    private int primaryKey = -1;
    private boolean hasTable = false;
    private String tableName;
    private static final String DATABASE_NAME = "jdbc:h2:~/database";


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
        for (Field field : typeClass.getFields()) {
            if (field.getAnnotation(Column.class) != null) {
                columns.add(new AnnotatedField(field));
            }
            if (field.getAnnotation(PrimaryKey.class) != null) {
                if (field.getAnnotation(Column.class) == null) {
                    throw new DatabaseException("Primary key should be column");
                }
                if (primaryKey != -1) {
                    throw new DatabaseException("Primary key should be one");
                }
                primaryKey = columns.size() - 1;
            }
        }
        try (Connection connection = DriverManager.getConnection(DATABASE_NAME)) {
            try (ResultSet resultSet = connection.getMetaData()
                    .getTables(null, null,
                            CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_UNDERSCORE, tableName), null)) {
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
        for (AnnotatedField field : columns) {
            createRequest.append(field.getColumnName()).append(" ");
            createRequest.append(field.getSqlType()).append(" ");
            if (field.getField().isAnnotationPresent(PrimaryKey.class)) {
                createRequest.append("NOT NULL PRIMARY KEY ");
            }
            createRequest.append(" , ");
        }
        createRequest.deleteCharAt(createRequest.lastIndexOf(","));
        createRequest.append(")");
        try (Connection connection = DriverManager.getConnection(DATABASE_NAME)) {
            try (Statement statement = connection.createStatement()) {
                statement.execute(createRequest.toString());
                hasTable = true;
            }
        }


    }

    public void dropTable() throws DatabaseException, SQLException {
        if (!hasTable) {
            throw new DatabaseException("table should be created before");
        }
        try (Connection connection = DriverManager.getConnection(DATABASE_NAME)) {
            try (Statement statement = connection.createStatement()) {
                statement.execute("DROP TABLE " + tableName);
                hasTable = false;
            }
        }

    }

    public <K> T queryById(K key) throws DatabaseException, SQLException {
        if (!hasTable) {
            throw new DatabaseException("table should be created before");
        }
        if (primaryKey == -1) {
            throw new DatabaseException("primary key should exist for delete");
        }
        if (!columns.get(primaryKey).getField().getType().isInstance(key)) {
            throw new IllegalArgumentException("key should have same type as primary key");
        }
        StringBuilder query = new StringBuilder();
        List<T> result = queryWithRequest(query.toString());
        if (result.size() == 0) {
            return null;
        } else {
            return result.get(0);
        }

    }


    public List<T> queryForAll() throws DatabaseException, SQLException {
        if (!hasTable) {
            throw new DatabaseException("table should be created before");
        }
        return queryWithRequest("SELECT * FROM " + tableName);

    }

    public void insert(T element) throws DatabaseException, SQLException {
        if (!hasTable) {
            throw new DatabaseException("table should be created before");
        }
        StringBuilder insertRequest = new StringBuilder();
        insertRequest.append("INSERT INTO ").append(tableName).append(" ( ");
        for (AnnotatedField field: columns) {
            insertRequest.append(field.getColumnName()).append(", ");
        };
        insertRequest.deleteCharAt(insertRequest.lastIndexOf(","));
        insertRequest.append(") VALUES (");
        for (AnnotatedField field: columns) {
            try {
                insertRequest.append(field.getField().get(element)).append(", ");
            } catch (IllegalAccessException e) {
                throw new DatabaseException("bad element for insert");
            }
        };
        insertRequest.deleteCharAt(insertRequest.lastIndexOf(","));
        insertRequest.append(")");
        try (Connection connection = DriverManager.getConnection(DATABASE_NAME)){
            try (Statement statement = connection.createStatement()){
                statement.execute(insertRequest.toString());
            }
        }



    }

    public void update(T element) throws DatabaseException {
        if (!hasTable) {
            throw new DatabaseException("table should be created before");
        }
        if (primaryKey == -1) {
            throw new DatabaseException("primary key should exist for update");
        }

    }

    public <K> void deleteByKey(K key) throws DatabaseException {
        if (!hasTable) {
            throw new DatabaseException("table should be created before");
        }
        if (primaryKey == -1) {
            throw new DatabaseException("primary key should exist for delete");
        }
        if (!columns.get(primaryKey).getField().getType().isInstance(key)) {
            throw new IllegalArgumentException("key should have same type as primary key");
        }

    }

    public void delete(T line) throws IllegalAccessException, DatabaseException {
        if (!hasTable) {
            throw new DatabaseException("table should be created before");
        }
        Object key = columns.get(primaryKey).getField().get(line);
        deleteByKey(key);
    }

    private List<T> queryWithRequest(String query) throws SQLException, DatabaseException {
        try (Connection connection = DriverManager.getConnection(DATABASE_NAME)) {
            try (Statement statement = connection.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery(query)) {
                    return convertResult(resultSet);
                }
            }
        }
    }

    private List<T> convertResult(ResultSet resultSet) throws SQLException, DatabaseException {
        List<T> result = new ArrayList<>();
        while (resultSet.next()) {
            T newElement;
            try {
                newElement = typeClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new DatabaseException("Can not create new element");
            }
            for (int i = 0; i < columns.size(); ++i) {
                try {
                    switch (columns.get(i).getSqlType()) {
                        case "INT":
                            long number = resultSet.getLong(i + 1);
                            columns.get(i).getField().set(newElement, number);
                            break;
                        case "DOUBLE":
                            double doubleNumber = resultSet.getDouble(i + 1);
                            columns.get(i).getField().set(newElement, doubleNumber);
                            break;
                        case "VARCHAR(10)":
                            if (columns.get(i).getField().getType().equals(String.class)) {
                                String string = resultSet.getString(i + 1);
                                columns.get(i).getField().set(newElement, string);
                            } else {
                                char c = resultSet.getString(i + 1).charAt(0);
                                columns.get(i).getField().set(newElement, c);
                            }
                            break;
                        default:
                            throw new DatabaseException("Type of field in class is bad.");
                    }
                } catch (IllegalAccessException e) {
                    throw new DatabaseException("Can not initialize new element");
                }
            }
            result.add(newElement);
        }
        return result;
    }


}
