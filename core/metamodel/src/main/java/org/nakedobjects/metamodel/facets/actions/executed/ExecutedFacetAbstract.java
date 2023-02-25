package org.nakedobjects.metamodel.facets.actions.executed;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.SingleValueFacetAbstract;
import org.nakedobjects.metamodel.spec.Target;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionConstants;


public abstract class ExecutedFacetAbstract extends SingleValueFacetAbstract implements ExecutedFacet {

    public static Class<? extends Facet> type() {
        return ExecutedFacet.class;
    }

    private final Where value;

    public ExecutedFacetAbstract(final Where value, final FacetHolder holder) {
        super(type(), holder);
        this.value = value;
    }

    public Where value() {
        return value;
    }

    public Target getTarget() {
        if (value == Where.LOCALLY) {
            return NakedObjectActionConstants.LOCAL;
        }
        if (value == Where.REMOTELY) {
            return NakedObjectActionConstants.REMOTE;
        }
        return NakedObjectActionConstants.DEFAULT;
    }

    @Override
    protected String toStringValues() {
        return "where=" + value.getFriendlyName();
    }
}
