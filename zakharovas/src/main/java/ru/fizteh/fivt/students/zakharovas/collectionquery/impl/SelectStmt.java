package ru.fizteh.fivt.students.zakharovas.collectionquery.impl;

import java.util.Comparator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by kormushin on 06.10.15.
 */
public class SelectStmt<T, R> implements Query<R> {
    private Stream<T> source;
    private boolean isDistinct;
    private Function<T, R> onlyFunction;
    private Function<T, ?> functions[];
    private Class<R> rClass;

    @SafeVarargs
    public SelectStmt(Stream<T> source, Class<R> rClass, boolean isDistinct, Function<T, ?>... functions) {
        this.functions = functions;
        this.onlyFunction = null;
        this.isDistinct = isDistinct;
        this.source = source;
        this.rClass = rClass;
    }

    public SelectStmt(Stream<T> source, boolean isDistinct, Function<T, R> s) {
        this.source = source;
        this.isDistinct = isDistinct;
        this.onlyFunction = s;
        this.functions = null;

    }

    public WhereStmt<T, R> where(Predicate<T> predicate) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterable<R> execute() {
        return this.stream().collect(Collectors.toList());

    }

    @Override
    public Stream<R> stream() {
        return null;
    }

    public class WhereStmt<T, R> implements Query<R> {
        @SafeVarargs
        public final WhereStmt<T, R> groupBy(Function<T, ?>... expressions) {
            throw new UnsupportedOperationException();
        }

        @SafeVarargs
        public final WhereStmt<T, R> orderBy(Comparator<R>... comparators) {
            throw new UnsupportedOperationException();
        }

        public WhereStmt<T, R> having(Predicate<R> condition) {
            throw new UnsupportedOperationException();
        }

        public WhereStmt<T, R> limit(int amount) {
            throw new UnsupportedOperationException();
        }

        public UnionStmt union() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Iterable<R> execute() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Stream<R> stream() {
            throw new UnsupportedOperationException();
        }
    }

}
