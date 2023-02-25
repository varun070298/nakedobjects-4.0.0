package org.nakedobjects.metamodel.facets.actions;

import java.lang.reflect.Method;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetFactoryAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MethodFilteringFacetFactory;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.facets.MethodScope;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


/**
 * Designed to simply filter out {@link Iterable#iterator()} method if it exists.
 * 
 * <p>
 * Does not add any {@link Facet}s.
 */
public class IteratorFilteringFacetFactory extends FacetFactoryAbstract implements MethodFilteringFacetFactory {

    public IteratorFilteringFacetFactory() {
        super(NakedObjectFeatureType.OBJECTS_ONLY);
    }

    @Override
    public boolean process(final Class<?> type, final MethodRemover methodRemover, final FacetHolder holder) {
        methodRemover.removeMethod(MethodScope.OBJECT, "iterator", java.util.Iterator.class, new Class[] {});
        return false;
    }

    public boolean recognizes(final Method method) {
        if (method.toString().equals("public abstract java.util.Iterator java.lang.Iterable.iterator()")) {
            return true;
        }
        return false;
    }

}

// Copyright (c) Naked Objects Group Ltd.
