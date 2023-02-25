package org.nakedobjects.metamodel.commons.filters;


public final class Filters {

    private Filters() {}

    public static <T> Filter<T> and(final Filter<T> f1, final Filter<T> f2) {
        return new AbstractFilter<T>() {
            @Override
            public boolean accept(final T f) {
                return f1.accept(f) && f2.accept(f);
            }
        };
    }

    public static <T> Filter<T> or(final Filter<T> f1, final Filter<T> f2) {
        return new AbstractFilter<T>() {
            @Override
            public boolean accept(final T f) {
                return f1.accept(f) || f2.accept(f);
            }
        };
    }

    public static <T> Filter<T> not(final Filter<T> f1) {
        return new AbstractFilter<T>() {
            @Override
            public boolean accept(final T f) {
                return !f1.accept(f);
            }
        };
    }

    public static <T> Filter<T> any() {
        return new AbstractFilter<T>() {

            @Override
            public boolean accept(final T t) {
                return true;
            }
        };
    }

    public static <T> Filter<T> none() {
        return new AbstractFilter<T>() {
            @Override
            public boolean accept(final T f) {
                return false;
            }
        };
    }

}
