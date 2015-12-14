package ru.fizteh.fivt.students.zakharovas.miniorm;

import com.google.common.base.CaseFormat;
import ru.fizteh.fivt.students.zakharovas.miniorm.annotations.Column;
import ru.fizteh.fivt.students.zakharovas.miniorm.exceptions.DatabaseException;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by alexander on 14.12.15.
 */
public class AnnotatedField {
    private String columnName;
    private Field field;

    private static final Map<Class, String> sqlType;
    static {
        sqlType = new HashMap<>();
        sqlType.put(Integer.class, "INT");
        sqlType.put(Long.class, "INT");
        sqlType.put(Byte.class, "INT");
        sqlType.put(Short.class, "INT");
        sqlType.put(Double.class, "DOUBLE");
        sqlType.put(Float.class, "DOUBLE");
        sqlType.put(String.class, "VARCHAR(10)");
        sqlType.put(Character.class, "VARCHAR(10)");
        sqlType.put(Integer.TYPE, "INT");
        sqlType.put(Long.TYPE, "INT");
        sqlType.put(Byte.TYPE, "INT");
        sqlType.put(Short.TYPE, "INT");
        sqlType.put(Double.TYPE, "DOUBLE");
        sqlType.put(Float.TYPE, "DOUBLE");
        sqlType.put(Character.TYPE, "VARCHAR(10)");
    }

    public String getColumnName() {
        return columnName;
    }

    public Field getField() {
        return field;
    }

    public String getSqlType() throws DatabaseException {
        if (sqlType.get(field.getType()) == null) {
            throw new DatabaseException(columnName + " has bad type for sql");
        }
        return sqlType.get(field.getType());

    }

    public AnnotatedField(Field field) {
        this.field = field;
        Column column = field.getAnnotation(Column.class);
        columnName =column.name();
        if (columnName.equals("")) {
            columnName = field.getName();
            columnName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, columnName);
        }
    }
}
