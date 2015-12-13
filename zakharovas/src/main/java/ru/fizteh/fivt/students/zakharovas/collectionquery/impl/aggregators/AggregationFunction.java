package ru.fizteh.fivt.students.zakharovas.collectionquery.impl.aggregators;

import java.util.List;
import java.util.function.Function;

/**
 * Created by alexander on 13.12.15.
 */
public interface AggregationFunction<T, E> extends Function<T, E> {
    public E applyOnList(List<T> list);
}
