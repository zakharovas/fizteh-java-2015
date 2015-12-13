package ru.fizteh.fivt.students.zakharovas.collectionquery.impl;

import java.util.List;

/**
 * Created by kormushin on 09.10.15.
 */
public class UnionStmt<R> {
    List<R> previousResults;

    public <T> FromStmt<T> from(Iterable<T> list) {
        throw new UnsupportedOperationException();

    }

    public UnionStmt(List<R> previousResults) {
        this.previousResults = previousResults;
    }


}
