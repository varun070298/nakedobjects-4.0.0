package org.nakedobjects.metamodel.examples.facets.namefile;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.naming.named.NamedFacet;


public class NameFileFacet extends FacetAbstract implements NamedFacet {

    public static Class<? extends Facet> type() {
        return NamedFacet.class;
    }

    private final String name;

    public NameFileFacet(final FacetHolder holder, String name) {
        super(type(), holder, false);
        this.name = name;
    }

    public String value() {
        return name;
    }


}
