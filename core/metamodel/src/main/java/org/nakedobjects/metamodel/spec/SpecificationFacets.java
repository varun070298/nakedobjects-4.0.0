package org.nakedobjects.metamodel.spec;

import org.nakedobjects.metamodel.facets.When;
import org.nakedobjects.metamodel.facets.object.bounded.BoundedFacet;
import org.nakedobjects.metamodel.facets.object.cached.CachedFacet;
import org.nakedobjects.metamodel.facets.object.immutable.ImmutableFacet;


public class SpecificationFacets {

    public static boolean isAlwaysImmutable(final NakedObjectSpecification specification) {
    	// this is a workaround for a dubious test
    	if (specification == null) {
    		return false;
    	}
    	
        final ImmutableFacet immutableFacet = specification.getFacet(ImmutableFacet.class);
        if (immutableFacet == null) {
            return false;
        }
        return immutableFacet.value() == When.ALWAYS;
    }

    public static boolean isImmutableOncePersisted(final NakedObjectSpecification specification) {
    	// this is a workaround for a dubious test
    	if (specification == null) {
    		return false;
    	}

    	final ImmutableFacet immutableFacet = specification.getFacet(ImmutableFacet.class);
        if (immutableFacet == null) {
            return false;
        }
        return immutableFacet.value() == When.ONCE_PERSISTED;
    }

    public static boolean isBoundedSet(final NakedObjectSpecification specification) {
        return specification.getFacet(BoundedFacet.class) != null;
    }

    public static boolean isCached(final NakedObjectSpecification specification) {
        return specification.getFacet(CachedFacet.class) != null;
    }

    private SpecificationFacets() {}
}

// Copyright (c) Naked Objects Group Ltd.
