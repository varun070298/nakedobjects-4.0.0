package org.nakedobjects.metamodel.facets.object.validprops;

import org.nakedobjects.applib.events.ValidityEvent;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.interactions.ObjectValidityContext;
import org.nakedobjects.metamodel.interactions.ValidityContext;


public abstract class ObjectValidPropertiesFacetAbstract extends FacetAbstract implements ObjectValidPropertiesFacet {

    public static Class<? extends Facet> type() {
        return ObjectValidPropertiesFacet.class;
    }

    public ObjectValidPropertiesFacetAbstract(final FacetHolder holder) {
        super(type(), holder, false);
    }

    public String invalidates(final ValidityContext<? extends ValidityEvent> ic) {
        if (!(ic instanceof ObjectValidityContext)) {
            return null;
        }
        final ObjectValidityContext validityContext = (ObjectValidityContext) ic;
        return invalidReason(validityContext);
    }

}
