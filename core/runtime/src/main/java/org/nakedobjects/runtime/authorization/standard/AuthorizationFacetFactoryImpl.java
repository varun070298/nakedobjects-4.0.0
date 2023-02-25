package org.nakedobjects.runtime.authorization.standard;

import java.lang.reflect.Method;

import org.nakedobjects.metamodel.facets.FacetFactoryAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;
import org.nakedobjects.runtime.authorization.AuthorizationManager;
import org.nakedobjects.runtime.context.NakedObjectsContext;


public class AuthorizationFacetFactoryImpl extends FacetFactoryAbstract {

	public AuthorizationFacetFactoryImpl() {
        super(NakedObjectFeatureType.EVERYTHING_BUT_PARAMETERS);
    }
    
    @Override
    public boolean process(final Class<?> cls, final MethodRemover methodRemover, final FacetHolder holder) {
        return FacetUtil.addFacet(createFacet(holder));
    }

    @Override
    public boolean process(Class<?> cls, final Method method, final MethodRemover methodRemover, final FacetHolder holder) {
        return FacetUtil.addFacet(createFacet(holder));
    }

    private AuthorizationFacetImpl createFacet(final FacetHolder holder) {
    	AuthorizationManager authorizationManager = getAuthorizationManager();
		return new AuthorizationFacetImpl(holder, authorizationManager);
    }

    
    ////////////////////////////////////////////////////////////////////
    // Dependencies (from context)
    ////////////////////////////////////////////////////////////////////
    
	private AuthorizationManager getAuthorizationManager() {
		return NakedObjectsContext.getAuthorizationManager();
	}
    
}
