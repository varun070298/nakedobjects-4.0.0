package org.nakedobjects.metamodel.facets.propparam.validate.regex;

import org.nakedobjects.applib.events.ValidityEvent;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MultipleValueFacetAbstract;
import org.nakedobjects.metamodel.interactions.ProposedHolder;
import org.nakedobjects.metamodel.interactions.ValidityContext;


public abstract class RegExFacetAbstract extends MultipleValueFacetAbstract implements RegExFacet {

    public static Class<? extends Facet> type() {
        return RegExFacet.class;
    }

    private final String validation;
    private final String format;
    private final boolean caseSensitive;

    public RegExFacetAbstract(final String validation, final String format, final boolean caseSensitive, final FacetHolder holder) {
        super(type(), holder);
        this.validation = validation;
        this.format = format;
        this.caseSensitive = caseSensitive;
    }

    public String validation() {
        return validation;
    }

    public String format() {
        return format;
    }

    public boolean caseSensitive() {
        return caseSensitive;
    }

    // //////////////////////////////////////////////////////////

    public String invalidates(final ValidityContext<? extends ValidityEvent> context) {
        if (!(context instanceof ProposedHolder)) {
            return null;
        }
        final ProposedHolder proposedHolder = (ProposedHolder) context;
        final NakedObject proposedArgument = proposedHolder.getProposed();
        if (proposedArgument == null) {
            return null;
        }
        final String titleString = proposedArgument.titleString();
        if (!doesNotMatch(titleString)) {
            return null;
        }
        return "Doesn't match pattern";
    }

}
