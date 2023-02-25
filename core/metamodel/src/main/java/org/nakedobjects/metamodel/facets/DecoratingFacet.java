package org.nakedobjects.metamodel.facets;

/**
 * Provides access to underlying facet that has been decorated.
 * 
 * <p>
 * Originally introduced as a means to allow filtering of facets to get at the underlying facet (eg to locate
 * those that are imperative, that is, abstract a method call to an <tt>addToXxx</tt>).
 * 
 * @param <T>
 */
public interface DecoratingFacet<T extends Facet> {

    T getDecoratedFacet();
}
