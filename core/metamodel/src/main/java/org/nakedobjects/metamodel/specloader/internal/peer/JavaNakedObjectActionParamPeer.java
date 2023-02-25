package org.nakedobjects.metamodel.specloader.internal.peer;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.commons.exceptions.NotYetImplementedException;
import org.nakedobjects.metamodel.facets.FacetHolderImpl;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;


public class JavaNakedObjectActionParamPeer extends FacetHolderImpl implements NakedObjectActionParamPeer {
    private final NakedObjectSpecification specification;

    public JavaNakedObjectActionParamPeer(final NakedObjectSpecification specification) {
        this.specification = specification;
    }

    public NakedObjectSpecification getSpecification() {
        return specification;
    }

    public Identifier getIdentifier() {
        throw new NotYetImplementedException();
    }
}
