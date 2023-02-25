package org.nakedobjects.metamodel.specloader.internal.peer;

import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;


public interface NakedObjectActionParamPeer extends FacetHolder {

    NakedObjectSpecification getSpecification();

}
