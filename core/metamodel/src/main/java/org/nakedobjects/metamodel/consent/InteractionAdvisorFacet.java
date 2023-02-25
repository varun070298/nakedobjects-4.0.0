package org.nakedobjects.metamodel.consent;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;


/**
 * Used by {@link Consent} (specifically the main implementations {@link Allow} and {@link Veto}), with the
 * idea being that the only things that can create {@link Consent} objects are {@link Facet}s.
 * 
 * <p>
 * TODO: note, this is a work-in-progress, because the DnD viewer in particular creates its own {@link Allow}s
 * and {@link Veto}s. The constructors that it uses have been deprecated to flag that the DnD logic should
 * move into {@link Facet}s that implement this interface.
 * 
 * @author Dan Haywood
 * 
 */
public interface InteractionAdvisorFacet extends InteractionAdvisor, Facet {

    /**
     * For testing purposes only.
     */
    public static InteractionAdvisorFacet NOOP = new InteractionAdvisorFacet() {
        public boolean alwaysReplace() {
            return false;
        }

        public Class<? extends Facet> facetType() {
            return null;
        }

        public FacetHolder getFacetHolder() {
            return null;
        }

        public boolean isNoop() {
            return true;
        }

        public void setFacetHolder(final FacetHolder facetHolder) {}
        
    	public Facet getUnderlyingFacet() {
    		return null;
    	}
    	public void setUnderlyingFacet(Facet underlyingFacet) {
    		throw new UnsupportedOperationException();
    	}

        public boolean isDerived() {
        	return false;
        }

    };

}
