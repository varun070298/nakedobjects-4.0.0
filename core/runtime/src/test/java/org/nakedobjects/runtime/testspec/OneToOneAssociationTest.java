package org.nakedobjects.runtime.testspec;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.adapter.Instance;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.exceptions.UnexpectedCallException;
import org.nakedobjects.metamodel.consent.Allow;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.InteractionInvocationMethod;
import org.nakedobjects.metamodel.facets.FacetHolderNoop;
import org.nakedobjects.metamodel.interactions.PropertyAccessContext;
import org.nakedobjects.metamodel.interactions.UsabilityContext;
import org.nakedobjects.metamodel.interactions.ValidityContext;
import org.nakedobjects.metamodel.interactions.VisibilityContext;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.spec.feature.OneToOneAssociation;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManager;
import org.nakedobjects.runtime.persistence.internal.RuntimeContextFromSession;


public abstract class OneToOneAssociationTest extends FacetHolderNoop implements OneToOneAssociation {

    private final RuntimeContext runtimeContext;

    public OneToOneAssociationTest() {
    	runtimeContext = new RuntimeContextFromSession();
    }

	public boolean isOneToManyAssociation() {
        return false;
    }
    public boolean isOneToOneAssociation() {
        return true;
    }
    
    public void clearAssociation(final NakedObject inObject) {
        throw new UnexpectedCallException();
    }

    public String getBusinessKeyName() {
        return "";
    }

    public String getDescription() {
        return "";
    }

    public String getHelp() {
        return "";
    }

    public NakedObject[] getChoices(final NakedObject target) {
        return null;
    }

    public boolean isEmpty(final NakedObject adapter) {
        return false;
    }

    public boolean isMandatory() {
        return false;
    }

    public boolean hasChoices() {
        return false;
    }

    public boolean isDerived() {
        return false;
    }

    public Consent isUsable(final AuthenticationSession session, final NakedObject target) {
        return Allow.DEFAULT;
    }

    public Consent isVisible(final AuthenticationSession session, final NakedObject target) {
        return Allow.DEFAULT;
    }

    public NakedObject getDefault(final NakedObject nakedObject) {
        return null;
    }

    public void toDefault(final NakedObject target) {}

    @Override
    public Identifier getIdentifier() {
        return null;
    }

    public VisibilityContext<?> createVisibleInteractionContext(
            final AuthenticationSession session,
            final InteractionInvocationMethod invocationMethod,
            final NakedObject targetNakedObject) {
        return null;
    }

    public UsabilityContext<?> createUsableInteractionContext(
            final AuthenticationSession session,
            final InteractionInvocationMethod invocationMethod,
            final NakedObject target) {
        return null;
    }

    public ValidityContext<?> createValidateInteractionContext(
            final AuthenticationSession session,
            final InteractionInvocationMethod invocationMethod,
            final NakedObject owningNakedObject,
            final NakedObject newValue) {
        return null;
    }

    public PropertyAccessContext createAccessInteractionContext(
            final AuthenticationSession session,
            final InteractionInvocationMethod interactionMethod,
            final NakedObject targetNakedObject) {
        return null;
    }

    public Instance getInstance(NakedObject nakedObject) {
        OneToOneAssociation specification = this;
        return nakedObject.getInstance(specification);
    }

    public boolean isAction() {
        return false;
    }

    public boolean isAssociation() {
        return true;
    }


    ////////////////////////////////////////////////////////
    // Dependencies (from context)
    ////////////////////////////////////////////////////////

    protected static PersistenceSession getPersistenceSession() {
        return NakedObjectsContext.getPersistenceSession();
    }

    protected static AdapterManager getAdapterManager() {
        return getPersistenceSession().getAdapterManager();
    }

    
	public RuntimeContext getRuntimeContext() {
		return runtimeContext;
	}



}
// Copyright (c) Naked Objects Group Ltd.
