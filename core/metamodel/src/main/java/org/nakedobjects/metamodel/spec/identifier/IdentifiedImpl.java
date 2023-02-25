package org.nakedobjects.metamodel.spec.identifier;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.facets.FacetHolderImpl;

public class IdentifiedImpl extends FacetHolderImpl implements Identified {

    private final Identifier identifier;
    
    public IdentifiedImpl() {
        this(null);
    }

    public IdentifiedImpl(final Identifier identifier) {
        this.identifier = identifier;
    }
    public Identifier getIdentifier() {
        return identifier;
    }


}
