package ru.fizteh.fivt.students.zakharovas.collectionquery.impl;

import java.util.function.Function;

/**
 * Created by alexander on 13.12.15.
 */
public class GroupByResult<T> {
    private Object[] results;

    public GroupByResult(T element, Function<T, ?>[] functions) {
        results = new Object[functions.length];
        for (int i = 0; i < functions.length; ++i) {
            results[i] = functions[i].apply(element);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GroupByResult)) {
            return false;
        }
        GroupByResult anotherResult = (GroupByResult) obj;
        if (results.length != anotherResult.results.length) {
            return false;
        }
        for (int i = 0; i < results.length; ++i) {
            if (!results[i].equals(anotherResult.results[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;
        System.out.println(results.length);
        for (Object element : results) {
            result += element.hashCode();
        }
        return result;
    }
}
