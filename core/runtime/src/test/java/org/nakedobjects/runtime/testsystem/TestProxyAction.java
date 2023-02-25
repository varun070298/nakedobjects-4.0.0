package org.nakedobjects.runtime.testsystem;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.adapter.Instance;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.filters.Filter;
import org.nakedobjects.metamodel.consent.Allow;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.InteractionInvocationMethod;
import org.nakedobjects.metamodel.facets.FacetHolderNoop;
import org.nakedobjects.metamodel.interactions.ActionInvocationContext;
import org.nakedobjects.metamodel.interactions.UsabilityContext;
import org.nakedobjects.metamodel.interactions.VisibilityContext;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.runtimecontext.noruntime.RuntimeContextNoRuntime;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.Target;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAction;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionParameter;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionType;


public class TestProxyAction extends FacetHolderNoop implements NakedObjectAction {

    private final String id;
	private final RuntimeContext runtimeContext;

    public TestProxyAction(final String id) {
        this.id = id;
		runtimeContext = new RuntimeContextNoRuntime();
    }

    public NakedObject execute(final NakedObject target, final NakedObject[] parameters) {
        return null;
    }

    public NakedObjectAction[] getActions() {
        return null;
    }

    public NakedObject[] getDefaults(final NakedObject target) {
        return null;
    }

    public NakedObjectSpecification getOnType() {
        return null;
    }

    public NakedObject[][] getChoices(final NakedObject target) {
        return null;
    }

    public int getParameterCount() {
        return 0;
    }

    public NakedObjectActionParameter[] getParameters() {
        return null;
    }

    public NakedObjectSpecification[] getParameterTypes() {
        return null;
    }

    public NakedObjectActionParameter[] getParameters(final Filter<NakedObjectActionParameter> filter) {
        return null;
    }

    public NakedObjectSpecification getReturnType() {
        return null;
    }

    public Target getTarget() {
        return null;
    }

    public NakedObjectActionType getType() {
        return null;
    }

    public boolean hasReturn() {
        return false;
    }

    public boolean isContributed() {
        return false;
    }

    public boolean promptForParameters(final NakedObject target) {
        return false;
    }

    public boolean isOnInstance() {
        return false;
    }

    public Consent isProposedArgumentSetValid(final NakedObject object, final NakedObject[] parameters) {
        return null;
    }

    public NakedObject realTarget(final NakedObject target) {
        return null;
    }

    public String debugData() {
        return null;
    }

    public String getHelp() {
        return null;
    }

    public String getId() {
        return id;
    }

    public Consent isUsable(final AuthenticationSession session, final NakedObject target) {
        return Allow.DEFAULT;
    }

    public Consent isVisible(final AuthenticationSession session, final NakedObject target) {
        return Allow.DEFAULT;
    }

    public NakedObjectSpecification getSpecification() {
        return null;
    }

    public String getDescription() {
        return null;
    }

    public String getName() {
        return null;
    }

    @Override
    public Identifier getIdentifier() {
        return Identifier.classIdentifier("");
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

    public ActionInvocationContext createActionInvocationInteractionContext(
            final AuthenticationSession session,
            final InteractionInvocationMethod invocationMethod,
            final NakedObject object,
            final NakedObject[] candidateArguments) {
        return null;
    }


    // /////////////////////////////////////////////////////////////
    // isAction, isAssociation
    // /////////////////////////////////////////////////////////////

    public boolean isAction() {
        return true;
    }

    public boolean isAssociation() {
        return false;
    }
    public boolean isOneToManyAssociation() {
        return false;
    }
    public boolean isOneToOneAssociation() {
        return false;
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
