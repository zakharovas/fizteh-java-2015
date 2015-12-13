package ru.fizteh.fivt.students.zakharovas.collectionquery.impl.aggregators;

import java.util.List;
import java.util.function.Function;

/**
 * Created by alexander on 13.12.15.
 */
public class MaxFunction<T, E extends Comparable<E>> implements AggregationFunction<T, E>{

    private Function<T, E> converter;

    public MaxFunction(Function<T, E> converter) {
        this.converter = converter;
    }


    @Override
    public E apply(T t) {
        return null;
    }

    @Override
    public E applyOnList(List<T> list) {
        E result = null;
        for (T element: list) {
            if (result == null) {
                result = converter.apply(element);
            } else {
                E currentResult = converter.apply(element);
                if (currentResult.compareTo(result) > 0) {
                    result = currentResult;
                }
            }
        }
        return result;
    }

}
