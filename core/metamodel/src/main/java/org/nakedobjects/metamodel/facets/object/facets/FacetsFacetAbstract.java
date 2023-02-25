package org.nakedobjects.metamodel.facets.object.facets;

import java.util.ArrayList;
import java.util.List;

import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.commons.factory.InstanceFactory;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetAbstract;
import org.nakedobjects.metamodel.facets.FacetFactory;
import org.nakedobjects.metamodel.facets.FacetHolder;


public abstract class FacetsFacetAbstract extends FacetAbstract implements FacetsFacet {

    public static Class<? extends Facet> type() {
        return FacetsFacet.class;
    }

    private final Class<? extends FacetFactory>[] facetFactories;

	public FacetsFacetAbstract(final String[] names, final Class<?>[] classes, final FacetHolder holder) {
        super(type(), holder, false);
        final List<Class<? extends FacetFactory>> facetFactories = new ArrayList<Class<? extends FacetFactory>>();
        for (int i = 0; i < names.length; i++) {
            final Class<? extends FacetFactory> facetFactory = facetFactoryOrNull(names[i]);
            if (facetFactory != null) {
                facetFactories.add(facetFactory);
            }
        }
        for (int i = 0; i < classes.length; i++) {
            final Class<? extends FacetFactory> facetFactory = facetFactoryOrNull(classes[i]);
            if (facetFactory != null) {
                facetFactories.add(facetFactory);
            }
        }
        this.facetFactories = asArray(facetFactories);
    }

	@SuppressWarnings("unchecked")
	private Class<? extends FacetFactory>[] asArray(
			final List<Class<? extends FacetFactory>> facetFactories) {
		return (Class<? extends FacetFactory>[]) facetFactories.toArray(new Class[] {});
	}

    public Class<? extends FacetFactory>[] facetFactories() {
        return facetFactories;
    }

    private Class<? extends FacetFactory> facetFactoryOrNull(final String classCandidateName) {
        if (classCandidateName == null) {
            return null;
        }
        Class<?> classCandidate = null;
        try {
        	classCandidate = InstanceFactory.loadClass(classCandidateName);
            return facetFactoryOrNull(classCandidate);
        } catch (final NakedObjectException ex) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
	private Class<? extends FacetFactory> facetFactoryOrNull(final Class classCandidate) {
        if (classCandidate == null) {
            return null;
        }
        return FacetFactory.class.isAssignableFrom(classCandidate) ? classCandidate : null;
    }

}
