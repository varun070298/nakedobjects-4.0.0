package org.nakedobjects.metamodel.facets.actions;

import java.lang.reflect.Method;

import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetFactoryAbstract;
import org.nakedobjects.metamodel.facets.MethodFilteringFacetFactory;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


/**
 * Designed to simply filter out any synthetic methods.
 * 
 * <p>
 * Does not add any {@link Facet}s.
 */
public class SyntheticMethodFilteringFacetFactory extends FacetFactoryAbstract implements MethodFilteringFacetFactory {

    public SyntheticMethodFilteringFacetFactory() {
        super(new NakedObjectFeatureType[0]);
    }

    public boolean recognizes(final Method method) {
        return isSynthetic(method);
    }

    private boolean isSynthetic(final Method method) {
        try {
            final Class<?> type = method.getClass();
            try {
                return ((Boolean) type.getMethod("isSynthetic", (Class[]) null).invoke(method, (Object[]) null)).booleanValue();
            } catch (final NoSuchMethodException nsm) {
                // pre java 5
                return false;
            }
        } catch (final Exception e) {
            throw new NakedObjectException(e);
        }
    }

}

// Copyright (c) Naked Objects Group Ltd.
