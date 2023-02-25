package org.nakedobjects.metamodel.java5;

import org.nakedobjects.metamodel.facets.FacetFactoryAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.facets.MethodScope;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


/**
 * Removes any static getter or setter methods.
 */
public class RemoveStaticGettersAndSettersFacetFactory extends FacetFactoryAbstract {

    public RemoveStaticGettersAndSettersFacetFactory() {
        super(NakedObjectFeatureType.OBJECTS_ONLY);
    }

    @Override
    public boolean process(final Class<?> type, final MethodRemover methodRemover, final FacetHolder holder) {
        methodRemover.removeMethods(MethodScope.CLASS, "get", null, false, 0);
        methodRemover.removeMethods(MethodScope.CLASS, "set", null, false, 0);
        return false;
    }

}

// Copyright (c) Naked Objects Group Ltd.
