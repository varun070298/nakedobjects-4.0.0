package org.nakedobjects.metamodel.facets.ordering.memberorder;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MultipleValueFacetAbstract;


public abstract class MemberOrderFacetAbstract extends MultipleValueFacetAbstract implements MemberOrderFacet {

    public static Class<? extends Facet> type() {
        return MemberOrderFacet.class;
    }

    private final String name;
    private final String sequence;

    public MemberOrderFacetAbstract(final String name, final String sequence, final FacetHolder holder) {
        super(type(), holder);
        this.name = name;
        this.sequence = sequence;
    }

    public String name() {
        return name;
    }

    public String sequence() {
        return sequence;
    }

}
