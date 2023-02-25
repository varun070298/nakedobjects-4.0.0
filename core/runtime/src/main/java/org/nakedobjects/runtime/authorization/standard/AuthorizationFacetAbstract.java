package org.nakedobjects.runtime.authorization.standard;

import org.nakedobjects.applib.events.UsabilityEvent;
import org.nakedobjects.applib.events.VisibilityEvent;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.interactions.UsabilityContext;
import org.nakedobjects.metamodel.interactions.VisibilityContext;
import org.nakedobjects.runtime.authorization.AuthorizationManager;


public abstract class AuthorizationFacetAbstract extends FacetAbstract implements AuthorizationFacet {

    public static Class<? extends Facet> type() {
        return AuthorizationFacet.class;
    }

	private AuthorizationManager authorizationManager;

    public AuthorizationFacetAbstract(final FacetHolder holder, final AuthorizationManager authorizationManager) {
        super(type(), holder, false);
        this.authorizationManager = authorizationManager;
    }

    public String hides(final VisibilityContext<? extends VisibilityEvent> ic) {
    	return authorizationManager.isVisible(ic.getSession(), ic.getTarget(), ic.getIdentifier()) ? 
    			null : "Not authorized to view";
    }

    public String disables(final UsabilityContext<? extends UsabilityEvent> ic) {
        return authorizationManager.isUsable(ic.getSession(), ic.getTarget(), ic.getIdentifier()) ? 
        		null : "Not authorized to edit";
    }

}
