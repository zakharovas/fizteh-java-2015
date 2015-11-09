package ru.fizteh.fivt.students.zakharovas.collectionquery.impl;

import java.util.stream.Stream;

/**
 * @author akormushin
 */
public interface Query<R> {

    Iterable<R> execute();

    Stream<R> stream();
}
