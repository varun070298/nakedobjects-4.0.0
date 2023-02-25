package org.nakedobjects.metamodel.specloader.internal;

import java.util.List;

import org.nakedobjects.metamodel.adapter.Instance;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.InteractionInvocationMethod;
import org.nakedobjects.metamodel.consent.InteractionResult;
import org.nakedobjects.metamodel.facets.propcoll.access.PropertyAccessorFacet;
import org.nakedobjects.metamodel.facets.properties.choices.PropertyChoicesFacet;
import org.nakedobjects.metamodel.facets.properties.defaults.PropertyDefaultFacet;
import org.nakedobjects.metamodel.facets.properties.modify.PropertyClearFacet;
import org.nakedobjects.metamodel.facets.properties.modify.PropertyInitializationFacet;
import org.nakedobjects.metamodel.facets.properties.modify.PropertySetterFacet;
import org.nakedobjects.metamodel.facets.propparam.validate.mandatory.MandatoryFacet;
import org.nakedobjects.metamodel.interactions.InteractionUtils;
import org.nakedobjects.metamodel.interactions.PropertyAccessContext;
import org.nakedobjects.metamodel.interactions.PropertyModifyContext;
import org.nakedobjects.metamodel.interactions.PropertyUsabilityContext;
import org.nakedobjects.metamodel.interactions.PropertyVisibilityContext;
import org.nakedobjects.metamodel.interactions.UsabilityContext;
import org.nakedobjects.metamodel.interactions.ValidityContext;
import org.nakedobjects.metamodel.interactions.VisibilityContext;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.runtimecontext.spec.feature.NakedObjectAssociationAbstract;
import org.nakedobjects.metamodel.services.container.query.QueryFindAllInstances;
import org.nakedobjects.metamodel.spec.SpecificationFacets;
import org.nakedobjects.metamodel.spec.feature.OneToOneAssociation;
import org.nakedobjects.metamodel.specloader.internal.peer.NakedObjectAssociationPeer;


public class OneToOneAssociationImpl extends NakedObjectAssociationAbstract implements OneToOneAssociation {

    private final NakedObjectAssociationPeer associationPeer;

    public OneToOneAssociationImpl(
    		final NakedObjectAssociationPeer association, 
    		final RuntimeContext runtimeContext) {
        super(association.getIdentifier().getMemberName(), association.getSpecification(), MemberType.ONE_TO_ONE_ASSOCIATION, association, runtimeContext);
        this.associationPeer = association;
    }

    // /////////////////////////////////////////////////////////////
    // Hidden (or visible)
    // /////////////////////////////////////////////////////////////

    public VisibilityContext<?> createVisibleInteractionContext(
            final AuthenticationSession session,
            final InteractionInvocationMethod invocationMethod,
            final NakedObject ownerAdapter) {
        return new PropertyVisibilityContext(session, invocationMethod, ownerAdapter, getIdentifier());
    }

    // /////////////////////////////////////////////////////////////
    // Disabled (or enabled)
    // /////////////////////////////////////////////////////////////

    public UsabilityContext<?> createUsableInteractionContext(
            final AuthenticationSession session,
            final InteractionInvocationMethod invocationMethod,
            final NakedObject ownerAdapter) {
        return new PropertyUsabilityContext(session, invocationMethod, ownerAdapter, getIdentifier());
    }

    // /////////////////////////////////////////////////////////////
    // Validate
    // /////////////////////////////////////////////////////////////

    public ValidityContext<?> createValidateInteractionContext(
            final AuthenticationSession session,
            final InteractionInvocationMethod interactionMethod,
            final NakedObject ownerAdapter,
            final NakedObject proposedToReferenceAdapter) {
        return new PropertyModifyContext(session, interactionMethod, ownerAdapter, getIdentifier(), proposedToReferenceAdapter);
    }

    /**
     * TODO: currently this method is hard-coded to assume all interactions are initiated
     * {@link InteractionInvocationMethod#BY_USER by user}.
     */
    public Consent isAssociationValid(final NakedObject ownerAdapter, final NakedObject proposedToReferenceAdapter) {
        return isAssociationValidResult(ownerAdapter, proposedToReferenceAdapter).createConsent();
    }

    private InteractionResult isAssociationValidResult(final NakedObject ownerAdapter, final NakedObject proposedToReferenceAdapter) {
        final ValidityContext<?> validityContext = createValidateInteractionContext(getAuthenticationSession(),
                InteractionInvocationMethod.BY_USER, ownerAdapter, proposedToReferenceAdapter);
        return InteractionUtils.isValidResult(this, validityContext);
    }

    // /////////////////////////////////////////////////////////////
    // init
    // /////////////////////////////////////////////////////////////

    public void initAssociation(final NakedObject ownerAdapter, final NakedObject referencedAdapter) {
        final PropertyInitializationFacet initializerFacet = getFacet(PropertyInitializationFacet.class);
        if (initializerFacet != null) {
            initializerFacet.initProperty(ownerAdapter, referencedAdapter);
        }
    }

    // /////////////////////////////////////////////////////////////
    // Access (get, isEmpty)
    // /////////////////////////////////////////////////////////////

    @Override
    public NakedObject get(final NakedObject ownerAdapter) {
        final PropertyAccessorFacet facet = getFacet(PropertyAccessorFacet.class);
        final Object referencedPojo = facet.getProperty(ownerAdapter);
        
        if (referencedPojo == null) {
            return null;
        }
        
        return getRuntimeContext().adapterFor(referencedPojo, ownerAdapter, this);
    }


    /**
     * TODO: currently this method is hard-coded to assume all interactions are initiated
     * {@link InteractionInvocationMethod#BY_USER by user}.
     */
    public PropertyAccessContext createAccessInteractionContext(
            final AuthenticationSession session,
            final InteractionInvocationMethod interactionMethod,
            final NakedObject ownerAdapter) {
        return new PropertyAccessContext(session, InteractionInvocationMethod.BY_USER, ownerAdapter, getIdentifier(),
                get(ownerAdapter));
    }

    @Override
    public boolean isEmpty(final NakedObject ownerAdapter) {
        return get(ownerAdapter) == null;
    }

    // /////////////////////////////////////////////////////////////
    // Set
    // /////////////////////////////////////////////////////////////

    public void set(NakedObject ownerAdapter, NakedObject newReferencedAdapter) {
    	if (newReferencedAdapter != null) {
    		setAssociation(ownerAdapter, newReferencedAdapter);
    	} else {
    		clearAssociation(ownerAdapter);
    	}
    }

    public void setAssociation(final NakedObject ownerAdapter, final NakedObject newReferencedAdapter) {
        final PropertySetterFacet setterFacet = getFacet(PropertySetterFacet.class);
        if (setterFacet != null) {
            if (ownerAdapter.isPersistent() && 
			    newReferencedAdapter != null && 
			    newReferencedAdapter.isTransient()) {
            	// TODO: move to facet ?
			    throw new NakedObjectException("can't set a reference to a transient object from a persistent one: "
			            + newReferencedAdapter.titleString() + " (transient)");
			}
            setterFacet.setProperty(ownerAdapter, newReferencedAdapter);
        }
    }

    public void clearAssociation(final NakedObject ownerAdapter) {
        final PropertyClearFacet facet = getFacet(PropertyClearFacet.class);
        facet.clearProperty(ownerAdapter);
    }

    // /////////////////////////////////////////////////////////////
    // defaults
    // /////////////////////////////////////////////////////////////

    public NakedObject getDefault(final NakedObject ownerAdapter) {
        PropertyDefaultFacet propertyDefaultFacet = getFacet(PropertyDefaultFacet.class);
        // if no default on the association, attempt to find a default on the specification (eg an int should
        // default to 0).
        if (propertyDefaultFacet == null || propertyDefaultFacet.isNoop()) {
            propertyDefaultFacet = this.getSpecification().getFacet(PropertyDefaultFacet.class);
        }
        if (propertyDefaultFacet == null) {
            return null;
        }
        return propertyDefaultFacet.getDefault(ownerAdapter);
    }

    public void toDefault(final NakedObject ownerAdapter) {
    	// don't default optional fields
    	MandatoryFacet mandatoryFacet = getFacet(MandatoryFacet.class);
    	if (mandatoryFacet != null && mandatoryFacet.isInvertedSemantics()) {
    		return;
    	}
    	
    	final NakedObject defaultValue = (NakedObject) getDefault(ownerAdapter);
        if (defaultValue != null) {
            initAssociation(ownerAdapter, defaultValue);
        }
    }

    // /////////////////////////////////////////////////////////////
    // options (choices)
    // /////////////////////////////////////////////////////////////

    @Override
    public boolean hasChoices() {
        final PropertyChoicesFacet propertyChoicesFacet = getFacet(PropertyChoicesFacet.class);
        final boolean optionEnabled = propertyChoicesFacet != null;
        return SpecificationFacets.isBoundedSet(getSpecification()) || optionEnabled;
    }

    public NakedObject[] getChoices(final NakedObject ownerAdapter) {
        final PropertyChoicesFacet propertyChoicesFacet = getFacet(PropertyChoicesFacet.class);
        final Object[] pojoOptions = propertyChoicesFacet == null ? null : propertyChoicesFacet.getChoices(ownerAdapter);
        if (pojoOptions != null) {
            final NakedObject[] options = new NakedObject[pojoOptions.length];
            for (int i = 0; i < options.length; i++) {
                options[i] = getRuntimeContext().adapterFor(pojoOptions[i]);
            }
            return options;
        } else if (SpecificationFacets.isBoundedSet(getSpecification())) {
        	
            QueryFindAllInstances query = new QueryFindAllInstances(getSpecification());
			final List<NakedObject> allInstancesAdapter = getRuntimeContext().allMatchingQuery(query);
        	final NakedObject[] options = new NakedObject[allInstancesAdapter.size()];
        	int j = 0;
            for (NakedObject adapter: allInstancesAdapter) {
            	options[j++] = adapter;
            }
            return options;
        }
        return null;
    }



    // /////////////////////////////////////////////////////////////
    // getInstance
    // /////////////////////////////////////////////////////////////
    
    public Instance getInstance(NakedObject ownerAdapter) {
        OneToOneAssociation specification = this;
        return ownerAdapter.getInstance(specification);
    }

    
    
    // /////////////////////////////////////////////////////////////
    // debug, toString
    // /////////////////////////////////////////////////////////////

    public String debugData() {
        final DebugString debugString = new DebugString();
        debugString.indent();
        debugString.indent();
        associationPeer.debugData(debugString);
        return debugString.toString();
    }

    @Override
    public String toString() {
        final ToString str = new ToString(this);
        str.append(super.toString());
        str.setAddComma();
        str.append("persisted", !isDerived());
        str.append("type", getSpecification().getShortName());
        return str.toString();
    }



}
// Copyright (c) Naked Objects Group Ltd.
