package ru.fizteh.fivt.students.zakharovas.collectionquery;

import ru.fizteh.fivt.students.zakharovas.collectionquery.impl.aggregators.AvgFunction;
import ru.fizteh.fivt.students.zakharovas.collectionquery.impl.aggregators.CountFunction;
import ru.fizteh.fivt.students.zakharovas.collectionquery.impl.aggregators.MaxFunction;
import ru.fizteh.fivt.students.zakharovas.collectionquery.impl.aggregators.MinFunction;

import java.util.function.Function;

/**
 * Aggregate functions.
 *
 * @author akormushin
 */
public class Aggregates {

    /**
     * Maximum value for expression for elements of given collecdtion.
     *
     * @param expression
     * @param <C>
     * @param <T>
     * @return
     */
    public static <C, T extends Comparable<T>> Function<C, T> max(Function<C, T> expression) {
        return new MaxFunction<>(expression);
    }

    /**
     * Minimum value for expression for elements of given collecdtion.
     *
     * @param expression
     * @param <C>
     * @param <T>
     * @return
     */
    public static <C, T extends Comparable<T>> Function<C, T> min(Function<C, T> expression) {
        return new MinFunction<>(expression);
    }

    /**
     * Number of items in source collection that turns this expression into not null.
     *
     * @param expression
     * @param <C>
     * @return
     */
    public static <C> Function<C, Integer> count(Function<C, ?> expression) {
        return new CountFunction<C>();
    }

    /**
     * Average value for expression for elements of given collection.
     *
     * @param expression
     * @param <C>
     * @return
     */
    public static <C> Function<C, Double> avg(Function<C, ? extends Number> expression) {
        return new AvgFunction<>(expression);
    }

}
