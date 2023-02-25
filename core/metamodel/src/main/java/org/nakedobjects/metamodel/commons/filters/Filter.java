package org.nakedobjects.metamodel.commons.filters;

public interface Filter<T> {

    public abstract boolean accept(T t);

    public abstract Filter<T> and(Filter<T> t);

    public abstract Filter<T> or(Filter<T> t);

    public abstract Filter<T> not();

}
