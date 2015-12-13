package ru.fizteh.fivt.students.zakharovas.collectionquery.impl;

import java.util.List;

/**
 * Created by kormushin on 09.10.15.
 */
public class UnionStmt<R> {
    private List<R> previousResults;

    public <T> FromStmt<T> from(Iterable<T> list) {
        FromStmt<T> newFrom =  new FromStmt<T>(list);
        newFrom.setPreviousResults(previousResults);
        return newFrom;

    }

    public UnionStmt(List<R> previousResults) {
        this.previousResults = previousResults;
    }


}
