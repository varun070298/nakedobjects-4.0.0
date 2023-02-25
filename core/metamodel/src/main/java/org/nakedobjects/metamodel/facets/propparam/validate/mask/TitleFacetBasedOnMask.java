package org.nakedobjects.metamodel.facets.propparam.validate.mask;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.object.ident.title.TitleFacet;
import org.nakedobjects.metamodel.facets.object.ident.title.TitleFacetAbstract;
import org.nakedobjects.metamodel.facets.object.ident.title.TitleFacetUsingParser;


public class TitleFacetBasedOnMask extends TitleFacetAbstract {
    private final MaskFacet maskFacet;
    private final TitleFacet underlyingTitleFacet;

    public TitleFacetBasedOnMask(final MaskFacet maskFacet, final TitleFacet underlyingTitleFacet) {
        super(maskFacet.getFacetHolder());
        this.maskFacet = maskFacet;
        this.underlyingTitleFacet = underlyingTitleFacet;
    }

    public String title(final NakedObject object) {
        final String mask = maskFacet.value();
        TitleFacetUsingParser titleFacetUsingParser = (TitleFacetUsingParser) underlyingTitleFacet.getUnderlyingFacet();
        if (titleFacetUsingParser != null) {
            final String titleString = titleFacetUsingParser.title(object, mask);
            return titleString;
        } else {
           return underlyingTitleFacet.title(object);
        }
    }

}
