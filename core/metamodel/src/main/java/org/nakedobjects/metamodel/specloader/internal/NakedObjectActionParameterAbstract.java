package org.nakedobjects.metamodel.specloader.internal;

import java.util.ArrayList;
import java.util.List;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.filters.Filter;
import org.nakedobjects.metamodel.consent.Allow;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.InteractionInvocationMethod;
import org.nakedobjects.metamodel.exceptions.ModelException;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.MultiTypedFacet;
import org.nakedobjects.metamodel.facets.actions.choices.ActionParameterChoicesFacet;
import org.nakedobjects.metamodel.facets.actions.defaults.ActionParameterDefaultsFacet;
import org.nakedobjects.metamodel.facets.naming.describedas.DescribedAsFacet;
import org.nakedobjects.metamodel.facets.naming.named.NamedFacet;
import org.nakedobjects.metamodel.facets.propparam.validate.mandatory.MandatoryFacet;
import org.nakedobjects.metamodel.interactions.ActionArgumentContext;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.services.container.query.QueryFindAllInstances;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.SpecificationFacets;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAction;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionParameter;
import org.nakedobjects.metamodel.specloader.internal.peer.NakedObjectActionParamPeer;


public abstract class NakedObjectActionParameterAbstract implements NakedObjectActionParameter {

    private final int number;
    private final NakedObjectActionImpl parentAction;
    private final NakedObjectActionParamPeer peer;

    protected NakedObjectActionParameterAbstract(
            final int number,
            final NakedObjectActionImpl nakedObjectAction,
            final NakedObjectActionParamPeer peer) {
        this.number = number;
        this.parentAction = nakedObjectAction;
        this.peer = peer;
    }

    /**
     * Subclasses should override either {@link #isObject()} or {@link #isCollection()}.
     */
    public boolean isObject() {
        return false;
    }

    /**
     * Subclasses should override either {@link #isObject()} or {@link #isCollection()}.
     */
    public boolean isCollection() {
        return false;
    }

    /**
     * Parameter number, 0-based.
     */
    public int getNumber() {
        return number;
    }

    public NakedObjectAction getAction() {
        return parentAction;
    }

    public NakedObjectSpecification getSpecification() {
        return peer.getSpecification();
    }

    public Identifier getIdentifier() {
        return parentAction.getIdentifier();
    }

    public String getName() {
        final NamedFacet facet = getFacet(NamedFacet.class);
        String name = facet == null ? null : facet.value();
        name = name == null ? peer.getSpecification().getSingularName() : name;
        return name;
    }

    public String getDescription() {
        final DescribedAsFacet facet = getFacet(DescribedAsFacet.class);
        final String description = facet.value();
        return description == null ? "" : description;
    }

    public boolean isOptional() {
        final MandatoryFacet facet = getFacet(MandatoryFacet.class);
        return facet.isInvertedSemantics();
    }

    public Consent isUsable() {
        return Allow.DEFAULT;
    }

    // //////////////////////////////////////////////////////////
    // FacetHolder
    // //////////////////////////////////////////////////////////

    public boolean containsFacet(final Class<? extends Facet> facetType) {
        return peer != null ? peer.containsFacet(facetType) : false;
    }

    public <T extends Facet> T getFacet(final Class<T> cls) {
        return peer != null ? peer.getFacet(cls) : null;
    }

    public Class<? extends Facet>[] getFacetTypes() {
        return peer != null ? peer.getFacetTypes() : new Class[] {};
    }

    public Facet[] getFacets(final Filter<Facet> filter) {
        return peer != null ? peer.getFacets(filter) : new Facet[] {};
    }

    public void addFacet(final Facet facet) {
        if (peer != null) {
            peer.addFacet(facet);
        }
    }

    public void addFacet(final MultiTypedFacet facet) {
        if (peer != null) {
            peer.addFacet(facet);
        }
    }

    public void removeFacet(final Facet facet) {
        if (peer != null) {
            peer.removeFacet(facet);
        }
    }

    public void removeFacet(final Class<? extends Facet> facetType) {
        if (peer != null) {
            peer.removeFacet(facetType);
        }
    }

    // //////////////////////////////////////////////////////////
    // Interaction
    // //////////////////////////////////////////////////////////

    public ActionArgumentContext createProposedArgumentInteractionContext(
            final AuthenticationSession session,
            final InteractionInvocationMethod invocationMethod,
            final NakedObject targetObject,
            final NakedObject[] proposedArguments,
            final int position) {
        return new ActionArgumentContext(getAuthenticationSession(), invocationMethod, targetObject, getIdentifier(),
                proposedArguments, position);
    }

    public NakedObject[] getChoices(NakedObject nakedObject) {
        final List<NakedObject> parameterChoices = new ArrayList<NakedObject>();
        final ActionParameterChoicesFacet choicesFacet = getFacet(ActionParameterChoicesFacet.class);

        if (choicesFacet != null) {
            Object[] choices = choicesFacet.getChoices(parentAction.realTarget(nakedObject));
            checkChoicesType(getRuntimeContext(), choices, getSpecification());
            for (Object choice : choices) {
                parameterChoices.add(getRuntimeContext().adapterFor(choice));
            }
        }
        if (parameterChoices.size() == 0 && SpecificationFacets.isBoundedSet(getSpecification())) {
            QueryFindAllInstances query = new QueryFindAllInstances(getSpecification());
			final List<NakedObject> allInstancesAdapter = getRuntimeContext().allMatchingQuery(query);
            for (NakedObject adapter: allInstancesAdapter) {
                parameterChoices.add(adapter);
            }
        }
        return parameterChoices.toArray(new NakedObject[0]);
    }

    protected static void checkChoicesType(RuntimeContext runtimeContext, Object[] objects, NakedObjectSpecification paramSpec) {
        for (Object object : objects) {
            NakedObjectSpecification componentSpec = runtimeContext.getSpecificationLoader().loadSpecification(object.getClass());
            if (!componentSpec.isOfType(paramSpec)) {
                throw new ModelException("Choice type incompatible with parameter type; expected " + paramSpec.getFullName() + ", but was " + componentSpec.getFullName());
            }
        }
    }
    
    public NakedObject getDefault(NakedObject nakedObject) {
        if (parentAction.isContributed() && nakedObject != null) {
            if (nakedObject.getSpecification().isOfType(getSpecification())) {
                return nakedObject;
            }
        }
        final ActionParameterDefaultsFacet defaultsFacet = getFacet(ActionParameterDefaultsFacet.class);
        if (defaultsFacet != null) {
            Object dflt = defaultsFacet.getDefault(parentAction.realTarget(nakedObject));
            if (dflt == null) {
            	// it's possible that even though there is a default facet, when invoked it
            	// is unable to return a default.
            	return null;
            }
			return getRuntimeContext().adapterFor(dflt);
        }
        return null;
    }
    
    
    // /////////////////////////////////////////////////////////////
    // Dependencies (from context)
    // /////////////////////////////////////////////////////////////

    protected RuntimeContext getRuntimeContext() {
        return parentAction.getRuntimeContext();
    }

    protected AuthenticationSession getAuthenticationSession() {
        return getRuntimeContext().getAuthenticationSession();
    }

}
