package org.nakedobjects.runtime.testspec;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.adapter.Instance;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
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
import org.nakedobjects.runtime.persistence.internal.RuntimeContextFromSession;


public abstract class ValueFieldTest extends FacetHolderNoop implements OneToOneAssociation {

    private final RuntimeContext runtimeContext;

    public ValueFieldTest() {
    	runtimeContext = new RuntimeContextFromSession();
    }
    
    public RuntimeContext getRuntimeContext() {
    	return runtimeContext;
    }

    public boolean isOneToManyAssociation() {
        return false;
    }
    public boolean isOneToOneAssociation() {
        return true;
    }

    public boolean canClear() {
        return false;
    }

    public boolean canWrap() {
        return false;
    }

    public String getDescription() {
        return "";
    }

    public String getBusinessKeyName() {
        return "";
    }

    public NakedObject[] getChoices(final NakedObject target) {
        return null;
    }

    public int getMaximumLength() {
        return 0;
    }

    public int getNoLines() {
        return 0;
    }

    public int getTypicalLineLength() {
        return 0;
    }

    public String getHelp() {
        return "";
    }

    public Consent isUsable(final AuthenticationSession session, final NakedObject target) {
        return Allow.DEFAULT;
    }

    public Consent isVisible(final AuthenticationSession session, final NakedObject target) {
        return Allow.DEFAULT;
    }

    public boolean isCollection() {
        return false;
    }

    public boolean isDerived() {
        return false;
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

    public boolean isObject() {
        return false;
    }

    public Consent isUsable(final NakedObject target) {
        return Allow.DEFAULT;
    }

    public boolean isVisible(final NakedObject target) {
        return true;
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

}
// Copyright (c) Naked Objects Group Ltd.
