package org.nakedobjects.metamodel.facets.propparam.validate.regex;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.object.ident.title.TitleFacetAbstract;


public class TitleFacetFormattedByRegex extends TitleFacetAbstract {

    private final RegExFacet regexFacet;

    public TitleFacetFormattedByRegex(final RegExFacet regexFacet) {
        super(regexFacet.getFacetHolder());
        this.regexFacet = regexFacet;
    }

    public String title(final NakedObject object) {
        return regexFacet.format(object.titleString());
    }

}
