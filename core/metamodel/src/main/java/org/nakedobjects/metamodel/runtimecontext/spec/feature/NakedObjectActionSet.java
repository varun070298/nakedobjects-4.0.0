package org.nakedobjects.metamodel.runtimecontext.spec.feature;

import java.util.List;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.adapter.Instance;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.exceptions.UnexpectedCallException;
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
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionConstants;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionParameter;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionType;


public class NakedObjectActionSet implements NakedObjectAction {

    private final String name;
    private final String id;
    private final NakedObjectAction[] actions;
	private final RuntimeContext runtimeContext;
	
    public NakedObjectActionSet(
    		final String id, 
    		final String name, 
    		final NakedObjectAction[] actions, 
    		final RuntimeContext runtimeContext) {
        this.id = id;
        this.name = name;
        this.actions = actions;
        this.runtimeContext = runtimeContext;
    }

    public NakedObjectActionSet(
    		final String id, 
    		final String name, 
    		final List<NakedObjectAction> actions, 
    		final RuntimeContext runtimeContext) {
        this(id, name, actions.toArray(new NakedObjectAction[]{}), runtimeContext);
    }

    // /////////////////////////////////////////////////////////////
    // description, actions
    // /////////////////////////////////////////////////////////////

    public NakedObjectAction[] getActions() {
        return actions;
    }

    public String getDescription() {
        return "";
    }

    public Identifier getIdentifier() {
        return null;
    }

    public String getHelp() {
        return "";
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public NakedObjectSpecification getOnType() {
        return null;
    }

    public NakedObjectSpecification getReturnType() {
        return null;
    }

    public Target getTarget() {
        return NakedObjectActionConstants.DEFAULT;
    }

    public NakedObjectActionType getType() {
        return NakedObjectActionConstants.SET;
    }

    public boolean hasReturn() {
        return false;
    }

    public boolean isContributed() {
        return false;
    }

    /**
     * Always returns <tt>null</tt>.
     */
    public NakedObjectSpecification getSpecification() {
        return null;
    }

    // /////////////////////////////////////////////////////////////
    // target
    // /////////////////////////////////////////////////////////////

    public NakedObject realTarget(final NakedObject target) {
        return null;
    }

    // /////////////////////////////////////////////////////////////
    // execute
    // /////////////////////////////////////////////////////////////

    public NakedObject execute(final NakedObject target, final NakedObject[] parameters) {
        throw new UnexpectedCallException();
    }

    // /////////////////////////////////////////////////////////////
    // facets
    // /////////////////////////////////////////////////////////////

    /**
     * Does nothing
     */
    public <T extends Facet> T getFacet(final Class<T> cls) {
        return null;
    }

    /**
     * Does nothing
     */
    public Class<? extends Facet>[] getFacetTypes() {
        return new Class[0];
    }

    /**
     * Does nothing
     */
    public Facet[] getFacets(final Filter<Facet> filter) {
        return new Facet[0];
    }

    /**
     * Does nothing
     */
    public void addFacet(final Facet facet) {}

    /**
     * Does nothing
     */
    public void addFacet(final MultiTypedFacet facet) {}

    /**
     * Does nothing
     */
    public void removeFacet(final Facet facet) {}

    /**
     * Does nothing
     */
    public boolean containsFacet(final Class<? extends Facet> facetType) {
        return false;
    }

    /**
     * Does nothing
     */
    public void removeFacet(final Class<? extends Facet> facetType) {}

    // /////////////////////////////////////////////////////////////
    // parameters
    // /////////////////////////////////////////////////////////////

    public int getParameterCount() {
        return 0;
    }

    public NakedObjectActionParameter[] getParameters() {
        return new NakedObjectActionParameter[0];
    }

    public NakedObjectSpecification[] getParameterTypes() {
        return new NakedObjectSpecification[0];
    }


    public NakedObjectActionParameter[] getParameters(final Filter<NakedObjectActionParameter> filter) {
        return new NakedObjectActionParameter[0];
    }

    public boolean promptForParameters(final NakedObject target) {
        return false;
    }

    // /////////////////////////////////////////////////////////////
    // visibility
    // /////////////////////////////////////////////////////////////

    /**
     * Does nothing, but shouldn't be called.
     */
    public VisibilityContext<?> createVisibleInteractionContext(
            final AuthenticationSession session,
            final InteractionInvocationMethod invocationMethod,
            final NakedObject targetNakedObject) {
        return null;
    }

    public Consent isVisible(final AuthenticationSession session, final NakedObject target) {
        return Allow.DEFAULT;
    }

    // /////////////////////////////////////////////////////////////
    // usability
    // /////////////////////////////////////////////////////////////

    public UsabilityContext<?> createUsableInteractionContext(
            final AuthenticationSession session,
            final InteractionInvocationMethod invocationMethod,
            final NakedObject target) {
        return null;
    }

    public Consent isUsable(final AuthenticationSession session, final NakedObject target) {
        return Allow.DEFAULT;
    }

    // /////////////////////////////////////////////////////////////
    // validity
    // /////////////////////////////////////////////////////////////

    public ActionInvocationContext createActionInvocationInteractionContext(
            final AuthenticationSession session,
            final InteractionInvocationMethod invocationMethod,
            final NakedObject object,
            final NakedObject[] candidateArguments) {
        return null;
    }

    public Consent isProposedArgumentSetValid(final NakedObject object, final NakedObject[] parameters) {
        throw new UnexpectedCallException();
    }

    // /////////////////////////////////////////////////////////////
    // defaults
    // /////////////////////////////////////////////////////////////

    public NakedObject[] getDefaults(final NakedObject target) {
        throw new UnexpectedCallException();
    }

    // /////////////////////////////////////////////////////////////
    // options
    // /////////////////////////////////////////////////////////////

    public NakedObject[][] getChoices(final NakedObject target) {
        throw new UnexpectedCallException();
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
    // debug
    // /////////////////////////////////////////////////////////////

    public String debugData() {
        return "";
    }

    
    // /////////////////////////////////////////////////////////////
    // getInstance
    // /////////////////////////////////////////////////////////////
    
    public Instance getInstance(NakedObject nakedObject) {
        NakedObjectAction specification = this;
        return nakedObject.getInstance(specification);
    }

    
    // /////////////////////////////////////////////////////////////
    // RuntimeContext
    // /////////////////////////////////////////////////////////////

	public RuntimeContext getRuntimeContext() {
		return runtimeContext;
	}



}
// Copyright (c) Naked Objects Group Ltd.
