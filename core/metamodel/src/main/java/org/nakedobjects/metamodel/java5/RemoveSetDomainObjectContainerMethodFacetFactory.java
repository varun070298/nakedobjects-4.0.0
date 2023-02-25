package org.nakedobjects.metamodel.java5;

import org.nakedobjects.applib.DomainObjectContainer;
import org.nakedobjects.metamodel.facets.FacetFactoryAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.facets.MethodScope;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


/**
 * Removes any calls to <tt>setContainer(DomainObjectContainer)</tt>.
 */
public class RemoveSetDomainObjectContainerMethodFacetFactory extends FacetFactoryAbstract {

    public RemoveSetDomainObjectContainerMethodFacetFactory() {
        super(NakedObjectFeatureType.OBJECTS_ONLY);
    }

    @Override
    public boolean process(final Class<?> type, final MethodRemover methodRemover, final FacetHolder holder) {
        methodRemover.removeMethod(MethodScope.OBJECT, "setContainer", void.class, new Class[] { DomainObjectContainer.class });
        methodRemover.removeMethod(MethodScope.OBJECT, "set_Container", void.class, new Class[] { DomainObjectContainer.class });
        return false;
    }

}

// Copyright (c) Naked Objects Group Ltd.
