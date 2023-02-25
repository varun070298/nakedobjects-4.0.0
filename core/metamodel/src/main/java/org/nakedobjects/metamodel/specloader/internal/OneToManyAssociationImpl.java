package org.nakedobjects.metamodel.specloader.internal;

import org.nakedobjects.metamodel.adapter.Instance;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.InteractionInvocationMethod;
import org.nakedobjects.metamodel.consent.InteractionResult;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionAddToFacet;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionClearFacet;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionFacet;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionRemoveFromFacet;
import org.nakedobjects.metamodel.facets.propcoll.access.PropertyAccessorFacet;
import org.nakedobjects.metamodel.interactions.CollectionAddToContext;
import org.nakedobjects.metamodel.interactions.CollectionRemoveFromContext;
import org.nakedobjects.metamodel.interactions.CollectionUsabilityContext;
import org.nakedobjects.metamodel.interactions.CollectionVisibilityContext;
import org.nakedobjects.metamodel.interactions.InteractionUtils;
import org.nakedobjects.metamodel.interactions.UsabilityContext;
import org.nakedobjects.metamodel.interactions.ValidityContext;
import org.nakedobjects.metamodel.interactions.VisibilityContext;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.runtimecontext.spec.feature.NakedObjectAssociationAbstract;
import org.nakedobjects.metamodel.spec.feature.OneToManyAssociation;
import org.nakedobjects.metamodel.specloader.internal.peer.NakedObjectAssociationPeer;
import org.nakedobjects.metamodel.util.CollectionFacetUtils;


public class OneToManyAssociationImpl extends NakedObjectAssociationAbstract implements OneToManyAssociation {

    private final NakedObjectAssociationPeer reflectiveAdapter;

    public OneToManyAssociationImpl(
    		final NakedObjectAssociationPeer association, 
    		final RuntimeContext runtimeContext) {
        super(association.getIdentifier().getMemberName(), association.getSpecification(), MemberType.ONE_TO_MANY_ASSOCIATION, association, runtimeContext);
        this.reflectiveAdapter = association;
    }


    // /////////////////////////////////////////////////////////////
    // Hidden (or visible)
    // /////////////////////////////////////////////////////////////

    public VisibilityContext<?> createVisibleInteractionContext(
            final AuthenticationSession session,
            final InteractionInvocationMethod invocationMethod,
            final NakedObject ownerAdapter) {
        return new CollectionVisibilityContext(session, invocationMethod, ownerAdapter, getIdentifier());
    }

    // /////////////////////////////////////////////////////////////
    // Disabled (or enabled)
    // /////////////////////////////////////////////////////////////

    public UsabilityContext<?> createUsableInteractionContext(
            final AuthenticationSession session,
            final InteractionInvocationMethod invocationMethod,
            final NakedObject ownerAdapter) {
        return new CollectionUsabilityContext(session, invocationMethod, ownerAdapter, getIdentifier());
    }


    // /////////////////////////////////////////////////////////////
    // Validate Add
    // /////////////////////////////////////////////////////////////

    public ValidityContext<?> createValidateAddInteractionContext(
            final AuthenticationSession session,
            final InteractionInvocationMethod invocationMethod,
            final NakedObject ownerAdapter,
            final NakedObject proposedToAddAdapter) {
        return new CollectionAddToContext(session, invocationMethod, ownerAdapter, getIdentifier(), proposedToAddAdapter);
    }

    /**
     * TODO: currently this method is hard-coded to assume all interactions are initiated
     * {@link InteractionInvocationMethod#BY_USER by user}.
     */
    public Consent isValidToAdd(final NakedObject ownerAdapter, final NakedObject proposedToAddAdapter) {
        return isValidToAddResult(ownerAdapter, proposedToAddAdapter).createConsent();
    }

    private InteractionResult isValidToAddResult(final NakedObject ownerAdapter, final NakedObject proposedToAddAdapter) {
        final ValidityContext<?> validityContext = createValidateAddInteractionContext(getAuthenticationSession(),
                InteractionInvocationMethod.BY_USER, ownerAdapter, proposedToAddAdapter);
        return InteractionUtils.isValidResult(this, validityContext);
    }

    // /////////////////////////////////////////////////////////////
    // Validate Remove
    // /////////////////////////////////////////////////////////////

    public ValidityContext<?> createValidateRemoveInteractionContext(
            final AuthenticationSession session,
            final InteractionInvocationMethod invocationMethod,
            final NakedObject ownerAdapter,
            final NakedObject proposedToRemoveAdapter) {
        return new CollectionRemoveFromContext(session, invocationMethod, ownerAdapter, getIdentifier(),
                proposedToRemoveAdapter);
    }

    /**
     * TODO: currently this method is hard-coded to assume all interactions are initiated
     * {@link InteractionInvocationMethod#BY_USER by user}.
     */
    public Consent isValidToRemove(final NakedObject ownerAdapter, final NakedObject proposedToRemoveAdapter) {
        return isValidToRemoveResult(ownerAdapter, proposedToRemoveAdapter).createConsent();
    }

    private InteractionResult isValidToRemoveResult(final NakedObject ownerAdapter, final NakedObject proposedToRemoveAdapter) {
        final ValidityContext<?> validityContext = createValidateRemoveInteractionContext(getAuthenticationSession(),
                InteractionInvocationMethod.BY_USER, ownerAdapter, proposedToRemoveAdapter);
        return InteractionUtils.isValidResult(this, validityContext);
    }

    private boolean readWrite() {
        return !isDerived();
    }

    // /////////////////////////////////////////////////////////////
    // get, isEmpty, add, clear
    // /////////////////////////////////////////////////////////////

    @Override
    public NakedObject get(final NakedObject ownerAdapter) {

        final PropertyAccessorFacet accessor = getFacet(PropertyAccessorFacet.class);
        final Object collection = accessor.getProperty(ownerAdapter);
        if (collection == null) {
            return null;
        }
        return getRuntimeContext().adapterFor(collection, ownerAdapter, this);
    }



    @Override
    public boolean isEmpty(final NakedObject parentAdapter) {
        // REVIEW should we be able to determine if a collection is empty without loading it?
        final NakedObject collection = get(parentAdapter);
        final CollectionFacet facet = CollectionFacetUtils.getCollectionFacetFromSpec(collection);
        return facet.size(collection) == 0;
    }


    // /////////////////////////////////////////////////////////////
    // add, clear
    // /////////////////////////////////////////////////////////////

    public void addElement(final NakedObject ownerAdapter, final NakedObject referencedAdapter) {
        if (referencedAdapter == null) {
            throw new IllegalArgumentException("Can't use null to add an item to a collection");
        }
        if (readWrite()) {
            if (ownerAdapter.isPersistent() && referencedAdapter.isTransient()) {
                throw new NakedObjectException("can't set a reference to a transient object from a persistent one: "
                        + ownerAdapter.titleString() + " (persistent) -> " + referencedAdapter.titleString() + " (transient)");
            }
            final CollectionAddToFacet facet = getFacet(CollectionAddToFacet.class);
            facet.add(ownerAdapter, referencedAdapter);
        }
    }

    public void removeElement(final NakedObject ownerAdapter, final NakedObject referencedAdapter) {
        if (referencedAdapter == null) {
            throw new IllegalArgumentException("element should not be null");
        }
        if (readWrite()) {
            final CollectionRemoveFromFacet facet = getFacet(CollectionRemoveFromFacet.class);
            facet.remove(ownerAdapter, referencedAdapter);
        }
    }

    public void removeAllAssociations(final NakedObject ownerAdapter) {
        final CollectionClearFacet facet = getFacet(CollectionClearFacet.class);
        facet.clear(ownerAdapter);
    }

    public void clearCollection(final NakedObject ownerAdapter) {
        if (readWrite()) {
            final CollectionClearFacet facet = getFacet(CollectionClearFacet.class);
            facet.clear(ownerAdapter);
        }
    }

    // /////////////////////////////////////////////////////////////
    // defaults
    // /////////////////////////////////////////////////////////////

    public NakedObject getDefault(final NakedObject ownerAdapter) {
        return null;
    }

    public void toDefault(final NakedObject ownerAdapter) {}

    
    // /////////////////////////////////////////////////////////////
    // options (choices)
    // /////////////////////////////////////////////////////////////

    public NakedObject[] getChoices(final NakedObject ownerAdapter) {
        return new NakedObject[0];
    }

    @Override
    public boolean hasChoices() {
        return false;
    }



    // /////////////////////////////////////////////////////////////
    // getInstance
    // /////////////////////////////////////////////////////////////
    
    public Instance getInstance(NakedObject nakedObject) {
        OneToManyAssociation specification = this;
        return nakedObject.getInstance(specification);
    }
    

    // /////////////////////////////////////////////////////////////
    // debug, toString
    // /////////////////////////////////////////////////////////////

    public String debugData() {
        final DebugString debugString = new DebugString();
        debugString.indent();
        debugString.indent();
        reflectiveAdapter.debugData(debugString);
        return debugString.toString();
    }

    @Override
    public String toString() {
        final ToString str = new ToString(this);
        str.append(super.toString());
        str.append(",");
        str.append("persisted", !isDerived());
        str.append("type", getSpecification() == null ? "unknown" : getSpecification().getShortName());
        return str.toString();
    }





}
// Copyright (c) Naked Objects Group Ltd.
