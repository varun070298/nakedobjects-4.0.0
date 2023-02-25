package org.nakedobjects.runtime.system.specpeer;

import org.nakedobjects.metamodel.facets.FacetHolderImpl;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.specloader.internal.peer.NakedObjectActionParamPeer;


public class DummyActionParamPeer extends FacetHolderImpl implements NakedObjectActionParamPeer {

    public NakedObjectSpecification getSpecification() {
        return null;
    }

}
