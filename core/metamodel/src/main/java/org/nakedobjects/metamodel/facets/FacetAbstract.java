package org.nakedobjects.metamodel.facets;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.nakedobjects.metamodel.commons.ensure.Ensure.ensureThatArg;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.ensure.Ensure;
import org.nakedobjects.metamodel.commons.matchers.NofMatchers;
import org.nakedobjects.metamodel.interactions.DisablingInteractionAdvisor;
import org.nakedobjects.metamodel.interactions.HidingInteractionAdvisor;
import org.nakedobjects.metamodel.interactions.ValidatingInteractionAdvisor;
import org.nakedobjects.metamodel.spec.identifier.Identified;


public abstract class FacetAbstract implements Facet {

	private Facet underlyingFacet;
	
    private final Class<? extends Facet> facetType;
    private final boolean derived;
    private FacetHolder holder;
    
    /**
     * Populated in {@link #setFacetHolder(FacetHolder)} if the provided holder
     * implements {@link Identified}.
     * 
     * <p>
     * Otherwise is <tt>null</tt>.
     */
    private Identified identified;
    
    @SuppressWarnings("unchecked")
    public FacetAbstract(
    		final Class<? extends Facet> facetType, 
    		final FacetHolder holder, 
    		boolean derived) {
        this.facetType = ensureThatArg(facetType, is(not(nullValue(Class.class))));
        setFacetHolder(ensureThatArg(holder, is(not(nullValue(FacetHolder.class)))));
        this.derived = derived;
    }

    public final Class<? extends Facet> facetType() {
        return facetType;
    }

    public FacetHolder getFacetHolder() {
        return holder;
    }
    
    public boolean isDerived() {
    	return derived;
    }

    /**
     * Convenience method that returns {@link #getFacetHolder()} downcast to
     * {@link Identified} if the implementation does indeed inherit from
     * {@link Identified}, otherwise <tt>null</tt>. 
     */
    public Identified getIdentified() {
        return identified;
    }

	public Facet getUnderlyingFacet() {
		return underlyingFacet;
	}
	public void setUnderlyingFacet(Facet underlyingFacet) {
		Ensure.ensureThatArg(underlyingFacet.facetType(), NofMatchers.classEqualTo(facetType));
		this.underlyingFacet = underlyingFacet;
	}

    /**
     * Assume implementation is <i>not</i> a no-op.
     * 
     * <p>
     * No-op implementations should override and return <tt>true</tt>.
     */
    public boolean isNoop() {
        return false;
    }

    /**
     * Default implementation of this method that returns <tt>true</tt>, ie should replace (none
     * {@link #isNoop() no-op} implementations.
     * 
     * <p>
     * Implementations that don't wish to replace none no-op implementations should override and return
     * <tt>false</tt>.
     */
    public boolean alwaysReplace() {
        return true;
    }

    public void setFacetHolder(final FacetHolder facetHolder) {
        this.holder = facetHolder;
        this.identified = holder instanceof Identified? (Identified)holder: null;
    }

    @Override
    public String toString() {
        String details = "";
        if (ValidatingInteractionAdvisor.class.isAssignableFrom(getClass())) {
            details += "Validating";
        }
        if (DisablingInteractionAdvisor.class.isAssignableFrom(getClass())) {
            details += (details.length() > 0 ? ";" : "") + "Disabling";
        }
        if (HidingInteractionAdvisor.class.isAssignableFrom(getClass())) {
            details += (details.length() > 0 ? ";" : "") + "Hiding";
        }
        if (!"".equals(details)) {
            details = "interaction=" + details + ",";
        }

        final String className = getClass().getName();
        final String stringValues = toStringValues();
        if (getClass() != facetType()) {
            final String facetType = facetType().getName();
            details += "type=" + facetType.substring(facetType.lastIndexOf('.') + 1);
        }
        if (!"".equals(stringValues)) {
        	details += ",";
        }
        return className.substring(className.lastIndexOf('.') + 1) + "[" + details + stringValues + "]";
    }

    /**
     * For convenience of subclass facets that implement {@link ValidatingInteractionAdvisor},
     * {@link HidingInteractionAdvisor} or {@link DisablingInteractionAdvisor}.
     */
    public Object unwrapObject(final NakedObject nakedObject) {
        if (nakedObject == null) {
            return null;
        }
        return nakedObject.getObject();
    }

    /**
     * For convenience of subclass facets that implement {@link ValidatingInteractionAdvisor},
     * {@link HidingInteractionAdvisor} or {@link DisablingInteractionAdvisor}.
     */
    public String unwrapString(final NakedObject nakedObject) {
        final Object obj = unwrapObject(nakedObject);
        if (obj == null) {
            return null;
        }
        if (!(obj instanceof String)) {
            return null;
        }
        return (String) obj;
    }

    protected String toStringValues() {
        return "";
    }
}
