package org.nakedobjects.metamodel.java5;

import org.nakedobjects.metamodel.facets.FacetFactoryAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.facets.MethodScope;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


/**
 * Removes any static getter or setter methods.
 * 
 * <p>
 * TODO: this is probably redundant given we also have {@link RemoveJavaLangObjectMethodsFacetFactory}.
 */
public class RemoveGetClassMethodFacetFactory extends FacetFactoryAbstract {

    public RemoveGetClassMethodFacetFactory() {
        super(NakedObjectFeatureType.OBJECTS_ONLY);
    }

    @Override
    public boolean process(final Class<?> type, final MethodRemover methodRemover, final FacetHolder holder) {
        methodRemover.removeMethod(MethodScope.OBJECT, "getClass", Class.class, null);
        return false;
    }

}

// Copyright (c) Naked Objects Group Ltd.
