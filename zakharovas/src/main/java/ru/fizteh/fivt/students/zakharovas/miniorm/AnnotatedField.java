package ru.fizteh.fivt.students.zakharovas.miniorm;

import com.google.common.base.CaseFormat;
import ru.fizteh.fivt.students.zakharovas.miniorm.annotations.Column;

import java.lang.reflect.Field;

/**
 * Created by alexander on 14.12.15.
 */
public class AnnotatedField {
    private String columnName;
    private Field field;

    public String getColumnName() {
        return columnName;
    }

    public Field getField() {
        return field;
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
