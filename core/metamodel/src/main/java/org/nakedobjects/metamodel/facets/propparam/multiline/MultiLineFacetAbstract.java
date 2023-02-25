package org.nakedobjects.metamodel.facets.propparam.multiline;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MultipleValueFacetAbstract;


public abstract class MultiLineFacetAbstract extends MultipleValueFacetAbstract implements MultiLineFacet {

    public static Class<? extends Facet> type() {
        return MultiLineFacet.class;
    }

    private final int numberOfLines;
    private final boolean preventWrapping;

    public MultiLineFacetAbstract(final int numberOfLines, final boolean preventWrapping, final FacetHolder holder) {
        super(type(), holder);
        this.numberOfLines = numberOfLines;
        this.preventWrapping = preventWrapping;
    }

    public int numberOfLines() {
        return numberOfLines;
    }

    public boolean preventWrapping() {
        return preventWrapping;
    }

    @Override
    protected String toStringValues() {
        return "lines=" + numberOfLines + "," + (preventWrapping ? "prevent-wrapping" : "allow-wrapping");
    }

}
