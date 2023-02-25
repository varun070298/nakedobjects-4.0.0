package org.nakedobjects.metamodel.spec.identifier;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.facets.FacetHolder;

public interface Identified extends FacetHolder {

    /**
     * Identifier of this feature.
     */
    Identifier getIdentifier();


}
