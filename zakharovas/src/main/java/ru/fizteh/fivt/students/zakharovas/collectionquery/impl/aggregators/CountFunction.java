package ru.fizteh.fivt.students.zakharovas.collectionquery.impl.aggregators;

import java.util.List;

/**
 * Created by alexander on 13.12.15.
 */
public class CountFunction<T> implements AggregationFunction<T, Integer> {
    @Override
    public Integer applyOnList(List<T> list) {
        return list.size();
    }

    @Override
    public Integer apply(T t) {
        return null;
    }
}
