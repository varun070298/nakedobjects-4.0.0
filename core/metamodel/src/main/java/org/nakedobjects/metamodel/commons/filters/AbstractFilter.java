package org.nakedobjects.metamodel.commons.filters;



public abstract class AbstractFilter<T> implements Filter<T> {

    /*
     * (non-Javadoc)
     * 
     * @see org.nakedobjects.noa.reflect.IFilter#accept(T)
     */
    public abstract boolean accept(T f);

    /*
     * (non-Javadoc)
     * 
     * @see org.nakedobjects.noa.reflect.IFilter#and(org.nakedobjects.noa.reflect.IFilter)
     */
    public Filter<T> and(final Filter<T> f) {
        return Filters.and(this, f);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nakedobjects.noa.reflect.IFilter#or(org.nakedobjects.noa.reflect.IFilter)
     */
    public Filter<T> or(final Filter<T> f) {
        return Filters.or(this, f);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nakedobjects.noa.reflect.IFilter#not()
     */
    public Filter<T> not() {
        return Filters.not(this);
    }

    public final static <T> Filter<T> noop(final Class<T> clazz) {
        return new AbstractFilter<T>() {
            @Override
            public boolean accept(final T f) {
                return true;
            }
        };
    }


}
