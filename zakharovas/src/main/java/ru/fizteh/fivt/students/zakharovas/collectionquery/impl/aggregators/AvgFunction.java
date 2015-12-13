package ru.fizteh.fivt.students.zakharovas.collectionquery.impl.aggregators;

import java.util.List;
import java.util.function.Function;

/**
 * Created by alexander on 13.12.15.
 */
public class AvgFunction<T> implements AggregationFunction<T, Double> {

    private Function<T, ? extends Number> converter;

    public AvgFunction(Function<T, ? extends Number> converter) {
        this.converter = converter;
    }

    @Override
    public Double applyOnList(List<T> list) {
        Double result = 0d;
        for (T element: list) {
            result += converter.apply(element).doubleValue();
        }
        result /= list.size();
        return result;
    }

    @Override
    public Double apply(T t) {
        return null;
    }
}
