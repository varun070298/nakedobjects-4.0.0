package org.nakedobjects.metamodel.facets.object.validate;

import org.nakedobjects.applib.events.ValidityEvent;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.interactions.ObjectValidityContext;
import org.nakedobjects.metamodel.interactions.ValidityContext;


public abstract class ValidateObjectFacetAbstract extends FacetAbstract implements ValidateObjectFacet {

    public static Class<? extends Facet> type() {
        return ValidateObjectFacet.class;
    }

    public ValidateObjectFacetAbstract(final FacetHolder holder) {
        super(type(), holder, false);
    }

    public String invalidates(final ValidityContext<? extends ValidityEvent> vc) {
    	if (!(vc instanceof ObjectValidityContext)) {
    		return null;
    	}
        NakedObject toValidate = vc.getTarget();
		return toValidate!=null? invalidReason(toValidate): null;
    }

}
