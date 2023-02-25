package org.nakedobjects.metamodel.adapter;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.filters.Filter;
import org.nakedobjects.metamodel.consent.Allow;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.InteractionInvocationMethod;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.MultiTypedFacet;
import org.nakedobjects.metamodel.interactions.ActionInvocationContext;
import org.nakedobjects.metamodel.interactions.UsabilityContext;
import org.nakedobjects.metamodel.interactions.VisibilityContext;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.Target;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAction;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionParameter;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionType;


/**
 * Has no functionality but makes it easier to write tests that require an instance of an {@link Identifier}.
 * 
 * <p>
 * Was previously DummyAction, in the web viewer project. Only used by tests there.
 */
public class NakedObjectActionNoop implements NakedObjectAction {

    public boolean[] canParametersWrap() {
        return null;
    }

    public String debugData() {
        return null;
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

    public String getDescription() {
        return null;
    }

    public boolean containsFacet(final Class<? extends Facet> facetType) {
        return false;
    }

    public <T extends Facet> T getFacet(final Class<T> cls) {
        return null;
    }

    public Class<? extends Facet>[] getFacetTypes() {
        return new Class[0];
    }

    public Facet[] getFacets(final Filter<Facet> filter) {
        return null;
    }

    public void addFacet(final Facet facet) {}

    public void addFacet(final MultiTypedFacet facet) {}

    public void removeFacet(final Facet facet) {}

    public void removeFacet(final Class<? extends Facet> facetType) {}

    public Identifier getIdentifier() {
        return null;
    }

    public String getHelp() {
        return null;
    }

    public String getId() {
        return null;
    }

    public String getName() {
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

    public VisibilityContext<?> createVisibleInteractionContext(
            final AuthenticationSession session,
            final InteractionInvocationMethod invocationMethod,
            final NakedObject targetNakedObject) {
        return null;
    }

    public Consent isVisible(final AuthenticationSession session, final NakedObject target) {
        return Allow.DEFAULT;
    }

    public Consent isUsable(final AuthenticationSession session, final NakedObject target) {
        return Allow.DEFAULT;
    }

    public Consent isProposedArgumentSetValid(final NakedObject object, final NakedObject[] parameters) {
        return Allow.DEFAULT;
    }

    public NakedObject realTarget(final NakedObject target) {
        return target;
    }

    public NakedObjectSpecification getSpecification() {
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
        NakedObjectAction specification = this;
        return nakedObject.getInstance(specification);
    }

    public NakedObjectSpecification[] getParameterTypes() {
        return null;
    }

	public RuntimeContext getRuntimeContext() {
		// TODO Auto-generated method stub
		return null;
	}



}

// Copyright (c) Naked Objects Group Ltd.
