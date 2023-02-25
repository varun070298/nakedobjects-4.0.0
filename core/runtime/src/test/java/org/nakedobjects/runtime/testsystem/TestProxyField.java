package org.nakedobjects.runtime.testsystem;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.adapter.Instance;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.consent.Allow;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.InteractionInvocationMethod;
import org.nakedobjects.metamodel.consent.InteractionResult;
import org.nakedobjects.metamodel.consent.InteractionResultSet;
import org.nakedobjects.metamodel.facets.FacetHolderImpl;
import org.nakedobjects.metamodel.interactions.UsabilityContext;
import org.nakedobjects.metamodel.interactions.ValidityContext;
import org.nakedobjects.metamodel.interactions.VisibilityContext;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.runtimecontext.noruntime.RuntimeContextNoRuntime;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;


public class TestProxyField extends FacetHolderImpl implements NakedObjectAssociation {

    private final String name;
    private final NakedObjectSpecification spec;
    private Identifier identifier;
	private final RuntimeContext runtimeContext;

    public TestProxyField(final String name, final NakedObjectSpecification spec) {
        this.name = name;
        this.spec = spec;
        identifier = Identifier.propertyOrCollectionIdentifier(spec.getFullName(), name);
		runtimeContext = new RuntimeContextNoRuntime();
    }

    public String debugData() {
        return "";
    }

    public NakedObject get(final NakedObject fromObject) {
        return ((TestProxyNakedObject) fromObject).getField(this); // contentObject;
    }

    public String getBusinessKeyName() {
        return null;
    }

    public boolean isOneToManyAssociation() {
        return getSpecification().isCollection();
    }

    public boolean isOneToOneAssociation() {
        return !isOneToManyAssociation();
    }

    public boolean isDerived() {
        return false;
    }

    public boolean isEmpty(final NakedObject adapter) {
        return false;
    }

    public boolean isObject() {
        return getSpecification().isObject();
    }

    public boolean hasChoices() {
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

    public Identifier getIdentifier() {
        return identifier;
    }

    public NakedObject[] getChoices(final NakedObject object) {
        return new NakedObject[] {};
    }

    public NakedObjectSpecification getSpecification() {
        return spec;
    }

    public boolean isMandatory() {
        return false;
    }

    public String getHelp() {
        return null;
    }

    public String getId() {
        return name;
    }

    public String getDescription() {
        return null;
    }

    public String getName() {
        return name;
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
            final NakedObject parent,
            final NakedObject newValue) {
        return null;
    }

    public InteractionResultSet isAssociationValidResultSet(final NakedObject targetObject, final NakedObject newValue) {
        return new InteractionResultSet();
    }

    public InteractionResult isUsableResult(final AuthenticationSession session, final NakedObject target) {
        return null;
    }

    public InteractionResult isVisibleResult(final AuthenticationSession session, final NakedObject target) {
        return null;
    }


    // /////////////////////////////////////////////////////////////
    // isAction, isAssociation
    // /////////////////////////////////////////////////////////////

    public boolean isAction() {
        return false;
    }

    public boolean isAssociation() {
        return true;
    }

    // /////////////////////////////////////////////////////////////
    // getInstance
    // /////////////////////////////////////////////////////////////
    
    public Instance getInstance(NakedObject nakedObject) {
        return null;
    }

    
    // /////////////////////////////////////////////////////////////
    // RuntimeContext
    // /////////////////////////////////////////////////////////////

	public RuntimeContext getRuntimeContext() {
		return runtimeContext;
	}

}

// Copyright (c) Naked Objects Group Ltd.
