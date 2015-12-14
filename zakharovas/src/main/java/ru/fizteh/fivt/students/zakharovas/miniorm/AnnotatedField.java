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

    private static final Map<Class, String> SQL_TYPE;

    static {
        SQL_TYPE = new HashMap<>();
        SQL_TYPE.put(Integer.class, "INT");
        SQL_TYPE.put(Long.class, "INT");
        SQL_TYPE.put(Byte.class, "INT");
        SQL_TYPE.put(Short.class, "INT");
        SQL_TYPE.put(Double.class, "DOUBLE");
        SQL_TYPE.put(Float.class, "DOUBLE");
        SQL_TYPE.put(String.class, "VARCHAR(10)");
        SQL_TYPE.put(Character.class, "VARCHAR(10)");
        SQL_TYPE.put(Integer.TYPE, "INT");
        SQL_TYPE.put(Long.TYPE, "INT");
        SQL_TYPE.put(Byte.TYPE, "INT");
        SQL_TYPE.put(Short.TYPE, "INT");
        SQL_TYPE.put(Double.TYPE, "DOUBLE");
        SQL_TYPE.put(Float.TYPE, "DOUBLE");
        SQL_TYPE.put(Character.TYPE, "VARCHAR(10)");
    }

    public String getColumnName() {
        return columnName;
    }

    public Field getField() {
        return field;
    }

    public String getSqlType() throws DatabaseException {
        if (SQL_TYPE.get(field.getType()) == null) {
            throw new DatabaseException(columnName + " has bad type for sql");
        }
        return SQL_TYPE.get(field.getType());

    }

    public AnnotatedField(Field field) {
        this.field = field;
        Column column = field.getAnnotation(Column.class);
        columnName = column.name();
        if (columnName.equals("")) {
            columnName = field.getName();
            columnName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, columnName);
        }
    }
}
