package ru.fizteh.fivt.students.zakharovas.collectionquery.impl;

import com.beust.jcommander.internal.Lists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by kormushin on 06.10.15.
 */
public class FromStmt<T> {
    private List<?> previousResults;
    private List<T> source;

    public static <T> FromStmt<T> from(Iterable<T> iterable) {
        return new FromStmt<T>(iterable);
    }

    public static <T> FromStmt<T> from(Stream<T> stream) {
        return new FromStmt<T>(stream);
    }

    public static <T> FromStmt<T> from(Query<T> query) throws ReflectiveOperationException {
        return new FromStmt<T>(query.execute());
    }

    @SafeVarargs
    public final <R> SelectStmt<T, R> select(Class<R> clazz, Function<T, ?>... s) {
        return new SelectStmt<>(previousResults, source, clazz, false, s);
    }

    /**
     * Selects the only defined expression as is without wrapper.
     *
     * @param s
     * @param <R>
     * @return statement resulting in collection of R
     */
    public final <R> SelectStmt<T, R> select(Function<T, R> s) {
        return new SelectStmt<>(previousResults, source, false, s);
    }

    /**
     * Selects the only defined expression as is without wrapper.
     *
     * @param first
     * @param second
     * @param <F>
     * @param <S>
     * @return statement resulting in collection of R
     */
    public final <F, S> SelectStmt<T, Tuple<F, S>> select(Function<T, F> first, Function<T, S> second) {
        Function<T, Tuple<F, S>> converter = (element) -> (new Tuple<>(first.apply(element), second.apply(element)));
        return new SelectStmt<>(previousResults, source, false, converter);
    }

    @SafeVarargs
    public final <R> SelectStmt<T, R> selectDistinct(Class<R> clazz, Function<T, ?>... s) {
        return new SelectStmt<T, R>(previousResults, source, clazz, true, s);
    }

    /**
     * Selects the only defined expression as is without wrapper.
     *
     * @param s
     * @param <R>
     * @return statement resulting in collection of R
     */
    public final <R> SelectStmt<T, R> selectDistinct(Function<T, R> s) {
        return new SelectStmt<T, R>(previousResults, source, true, s);
    }

    public <J> JoinClause<T, J> join(Iterable<J> iterable) {
        return new JoinClause<>(this, StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList()));
    }

    public <J> JoinClause<T, J> join(Stream<J> stream) {
        return new JoinClause<>(this, stream.collect(Collectors.toList()));
    }

    public <J> JoinClause<T, J> join(Query<J> stream) throws ReflectiveOperationException {
        return new JoinClause<>(this,
                StreamSupport.stream(stream.execute().spliterator(), false).collect(Collectors.toList()));
    }

    public <R> void setPreviousResults(List<R> previousResults) {
        this.previousResults = previousResults;
    }

    public class JoinClause<T, J> {
        private FromStmt<T> fromStmt;
        private List<J> extraSource;

        public JoinClause(FromStmt<T> fromStmt, List<J> extraSource) {
            this.fromStmt = fromStmt;
            this.extraSource = extraSource;
        }

        public FromStmt<Tuple<T, J>> on(BiPredicate<T, J> condition) {
            List<Tuple<T, J>> sourceForFrom = new ArrayList<>();
            for (T firstElement : fromStmt.source) {
                for (J secondElement : extraSource) {
                    if (condition.test(firstElement, secondElement)) {
                        sourceForFrom.add(new Tuple<>(firstElement, secondElement));
                    }
                }
            }
            FromStmt<Tuple<T, J>> newFrom = new FromStmt<Tuple<T, J>>(sourceForFrom);
            newFrom.setPreviousResults(fromStmt.previousResults);
            return newFrom;
        }

        public <K extends Comparable<?>> FromStmt<Tuple<T, J>> on(
                Function<T, K> leftKey,
                Function<J, K> rightKey) {
            Map<K, List<T>> mapForFirstSource = new HashMap<>();
            List<Tuple<T, J>> sourceForFrom = new ArrayList<>();
            for (T element : fromStmt.source) {
                if (mapForFirstSource.containsKey(leftKey.apply(element))) {
                    mapForFirstSource.get(leftKey.apply(element)).add(element);
                } else {
                    mapForFirstSource.put(leftKey.apply(element), Lists.newArrayList(element));
                }
            }
            for (J element : extraSource) {
                if (mapForFirstSource.containsKey(rightKey.apply(element))) {
                    for (T firstElement : mapForFirstSource.get(rightKey.apply(element))) {
                        sourceForFrom.add(new Tuple<>(firstElement, element));
                    }
                }
            }
            FromStmt<Tuple<T, J>> newFrom = new FromStmt<>(sourceForFrom);
            newFrom.setPreviousResults(fromStmt.previousResults);
            return newFrom;
        }
    }

    public FromStmt(Iterable<T> iterable) {
        source = StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList());
    }

    public FromStmt(Stream<T> stream) {
        source = stream.collect(Collectors.toList());
    }


}
