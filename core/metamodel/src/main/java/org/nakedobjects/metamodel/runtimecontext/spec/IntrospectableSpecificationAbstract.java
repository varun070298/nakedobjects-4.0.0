package org.nakedobjects.metamodel.runtimecontext.spec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.adapter.Instance;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.filters.Filter;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.InteractionInvocationMethod;
import org.nakedobjects.metamodel.consent.InteractionResult;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolderImpl;
import org.nakedobjects.metamodel.facets.actcoll.typeof.TypeOfFacet;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionFacet;
import org.nakedobjects.metamodel.facets.hide.HiddenFacet;
import org.nakedobjects.metamodel.facets.object.aggregated.AggregatedFacet;
import org.nakedobjects.metamodel.facets.object.encodeable.EncodableFacet;
import org.nakedobjects.metamodel.facets.object.immutable.ImmutableFacet;
import org.nakedobjects.metamodel.facets.object.parseable.ParseableFacet;
import org.nakedobjects.metamodel.facets.object.value.ValueFacet;
import org.nakedobjects.metamodel.interactions.InteractionContext;
import org.nakedobjects.metamodel.interactions.InteractionUtils;
import org.nakedobjects.metamodel.interactions.ObjectTitleContext;
import org.nakedobjects.metamodel.interactions.ObjectValidityContext;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.runtimecontext.spec.feature.NakedObjectActionSet;
import org.nakedobjects.metamodel.spec.IntrospectableSpecification;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.Persistability;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAction;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionParameter;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionType;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociationFilters;
import org.nakedobjects.metamodel.spec.feature.OneToManyAssociation;
import org.nakedobjects.metamodel.spec.feature.OneToOneAssociation;


// TODO work through all subclasses and remove duplicated overridden methods - this 
// hierarchy is badly structured move common, and default, functionality to this class from subclasses.
public abstract class IntrospectableSpecificationAbstract 
		extends FacetHolderImpl 
		implements NakedObjectSpecification, IntrospectableSpecification {

	private boolean introspected = false;


    protected String fullName;
    protected NakedObjectAssociation[] fields;
    protected NakedObjectAction[] objectActions;
    protected NakedObjectSpecification superClassSpecification;
    protected Identifier identifier;
    
	private final RuntimeContext runtimeContext;

    // //////////////////////////////////////////////////////////////////////
    // Constructor
    // //////////////////////////////////////////////////////////////////////

    public IntrospectableSpecificationAbstract(
    		final RuntimeContext runtimeContext) {
    	this.runtimeContext = runtimeContext;
    }

    // //////////////////////////////////////////////////////////////////////
    // Class and stuff immediately derivable from class
    // //////////////////////////////////////////////////////////////////////

    public String getFullName() {
        return fullName;
    }

    public String getIconName(final NakedObject object) {
        return null;
    }

    public boolean hasSubclasses() {
        return false;
    }

    public NakedObjectSpecification[] interfaces() {
        return new NakedObjectSpecification[0];
    }

    public NakedObjectSpecification[] subclasses() {
        return new NakedObjectSpecification[0];
    }

    public NakedObjectSpecification superclass() {
        return superClassSpecification;
    }

    public boolean isOfType(final NakedObjectSpecification specification) {
        return specification == this;
    }

    public void addSubclass(final NakedObjectSpecification specification) {}

    public Instance getInstance(NakedObject nakedObject) {
        return nakedObject;
    }

    
    // //////////////////////////////////////////////////////////////////////
    // Introspection
    // //////////////////////////////////////////////////////////////////////

    protected void setIntrospected(boolean introspected) {
		this.introspected = introspected;
	}
    public boolean isIntrospected() {
    	return introspected;
    }

    // //////////////////////////////////////////////////////////////////////
    // Facet Handling
    // //////////////////////////////////////////////////////////////////////

    @Override
    public <Q extends Facet> Q getFacet(final Class<Q> facetType) {
        final Q facet = super.getFacet(facetType);
        Q noopFacet = null;
        if (isNotANoopFacet(facet)) {
            return facet;
        } else {
            noopFacet = facet;
        }
        if (superclass() != null) {
            final Q superClassFacet = superclass().getFacet(facetType);
            if (isNotANoopFacet(superClassFacet)) {
                return superClassFacet;
            } else {
                if (noopFacet == null) {
                    noopFacet = superClassFacet;
                }
            }
        }
        if (interfaces() != null) {
            final NakedObjectSpecification[] interfaces = interfaces();
            for (int i = 0; i < interfaces.length; i++) {
                final NakedObjectSpecification interfaceSpec = interfaces[i];
                if (interfaceSpec == null) {
                    // HACK: shouldn't happen, but occurring on occasion when running
                    // XATs under JUnit4. Some sort of race condition?
                    continue;
                }
                final Q interfaceFacet = interfaceSpec.getFacet(facetType);
                if (isNotANoopFacet(interfaceFacet)) {
                    return interfaceFacet;
                } else {
                    if (noopFacet == null) {
                        noopFacet = interfaceFacet;
                    }
                }
            }
        }
        return noopFacet;
    }

    private boolean isNotANoopFacet(final Facet facet) {
        return facet != null && !facet.isNoop();
    }

    // //////////////////////////////////////////////////////////////////////
    // DefaultValue
    // //////////////////////////////////////////////////////////////////////

    public Object getDefaultValue() {
        return null;
    }

    // //////////////////////////////////////////////////////////////////////
    // Identifier
    // //////////////////////////////////////////////////////////////////////

    public Identifier getIdentifier() {
        return identifier;
    }

    // //////////////////////////////////////////////////////////////////
    // create InteractionContext
    // //////////////////////////////////////////////////////////////////

    public ObjectTitleContext createTitleInteractionContext(
            final AuthenticationSession session,
            final InteractionInvocationMethod interactionMethod,
            final NakedObject targetNakedObject) {
        return new ObjectTitleContext(session, interactionMethod, targetNakedObject, getIdentifier(), targetNakedObject
                .titleString());
    }

    // //////////////////////////////////////////////////////////////////////
    // getStaticallyAvailableFields, getDynamically..Fields, getField
    // //////////////////////////////////////////////////////////////////////

    public NakedObjectAssociation[] getAssociations() {
        return fields;
    }

    public List<? extends NakedObjectAssociation> getAssociationList() {
        return Arrays.asList(fields);
    }

    public NakedObjectAssociation[] getAssociations(final Filter<NakedObjectAssociation> filter) {
        final NakedObjectAssociation[] allFields = getAssociations();

        final NakedObjectAssociation[] selectedFields = new NakedObjectAssociation[allFields.length];
        int v = 0;
        for (int i = 0; i < allFields.length; i++) {
            if (filter.accept(allFields[i])) {
                selectedFields[v++] = allFields[i];
            }
        }

        final NakedObjectAssociation[] fields = new NakedObjectAssociation[v];
        System.arraycopy(selectedFields, 0, fields, 0, v);
        return fields;
    }

    public List<? extends NakedObjectAssociation> getAssociationList(final Filter<NakedObjectAssociation> filter) {
        return Arrays.asList(getAssociations(filter));
    }

    @SuppressWarnings("unchecked")
    public List<OneToOneAssociation> getPropertyList() {
        List<OneToOneAssociation> list = new ArrayList<OneToOneAssociation>();
        List associationList = getAssociationList(NakedObjectAssociationFilters.PROPERTIES);
        list.addAll(associationList);
        return list;
    }

    @SuppressWarnings("unchecked")
    public List<OneToManyAssociation> getCollectionList() {
        List<OneToManyAssociation> list = new ArrayList<OneToManyAssociation>();
        List associationList = getAssociationList(NakedObjectAssociationFilters.COLLECTIONS);
        list.addAll(associationList);
        return list;
    }


    // //////////////////////////////////////////////////////////////////////
    // getObjectAction, getAction, getActions
    // //////////////////////////////////////////////////////////////////////

    protected NakedObjectAction[] getActions(final NakedObjectAction[] availableActions, final NakedObjectActionType type) {
        return new NakedObjectAction[0];
    }

    public NakedObjectAction[] getObjectActions(final NakedObjectActionType... types) {
    	List<? extends NakedObjectAction> actions = getObjectActionList(types);
		return actions.toArray(new NakedObjectAction[]{});
    }

	public List<? extends NakedObjectAction> getObjectActionList(final NakedObjectActionType... types) {
		List<NakedObjectAction> actions = new ArrayList<NakedObjectAction>();
    	for(NakedObjectActionType type: types) {
            addActions(type, actions);
    	}
		return actions;
	}

	private void addActions(NakedObjectActionType type,
			List<NakedObjectAction> actions) {
		if (!isService()) {
			actions.addAll(Arrays.asList(getServiceActions(type)));
		}
		actions.addAll(Arrays.asList(getActions(objectActions, type)));
	}

    public NakedObjectAction[] getServiceActionsFor(final NakedObjectActionType... types) {
        final List<NakedObject> services = getRuntimeContext().getServices();
        final List<NakedObjectAction> relatedActions = new ArrayList<NakedObjectAction>();
            for (NakedObject serviceAdapter : services) {
                final List<NakedObjectAction> matchingActions = new ArrayList<NakedObjectAction>();
            for (NakedObjectActionType type : types) {
                final NakedObjectAction[] serviceActions = serviceAdapter.getSpecification().getObjectActions(type);
                for (int j = 0; j < serviceActions.length; j++) {
                    final NakedObjectSpecification returnType = serviceActions[j].getReturnType();
                    if (returnType != null && returnType.isCollection()) {
                        final TypeOfFacet facet = serviceActions[j].getFacet(TypeOfFacet.class);
                        final NakedObjectSpecification elementType = facet.valueSpec();
                        if (elementType.isOfType(this)) {
                            matchingActions.add(serviceActions[j]);
                        }
                    } else if (returnType != null && returnType.isOfType(this)) {
                        matchingActions.add(serviceActions[j]);
                    }
                }
            }
            if (matchingActions.size() > 0) {
                final NakedObjectActionSet set = new NakedObjectActionSet("id", serviceAdapter.titleString(), matchingActions,
                        runtimeContext);
                relatedActions.add(set);
            }
        }
        return (NakedObjectAction[]) relatedActions.toArray(new NakedObjectAction[relatedActions.size()]);
    }

    public boolean isAbstract() {
        return false;
    }

    /**
     * Whether or not this specification's class is marked as final, that is it may not have subclasses, and
     * hence methods that could be overridden.
     * 
     * <p>
     * Note - not used at present.
     */
    public boolean isFinal() {
        return false;
    }

    public boolean isService() {
        return false;
    }

    // //////////////////////////////////////////////////////////////////////
    // Dirty
    // //////////////////////////////////////////////////////////////////////

    public boolean isDirty(final NakedObject object) {
        return false;
    }

    public void clearDirty(final NakedObject object) {}

    public void markDirty(final NakedObject object) {}

    // //////////////////////////////////////////////////////////////////////
    // markAsService, findServiceMethodsWithParameter
    // //////////////////////////////////////////////////////////////////////

    /**
     * Finds all service actions of the specified type, if any.
     * 
     * <p>
     * However, if this specification {@link #isService() is actually for} a service, then returns an empty
     * array.
     * 
     * @return an array of {@link NakedObjectActionSet}s (!!), each of which contains
     *         {@link NakedObjectAction}s of the requested type.
     * 
     */
    protected NakedObjectAction[] getServiceActions(final NakedObjectActionType type) {
        if (isService()) {
            return new NakedObjectAction[0];
        }
        final List<NakedObject> services = getRuntimeContext().getServices();

        // will populate an ActionSet with all actions contributed by each service
        final List<NakedObjectActionSet> serviceActionSets = new ArrayList<NakedObjectActionSet>();

        for (NakedObject serviceAdapter : services) {
            final NakedObjectSpecification specification = serviceAdapter.getSpecification();
            if (specification == this) {
                continue;
            }

            final NakedObjectAction[] serviceActions = specification.getObjectActions(type);
            final List<NakedObjectAction> matchingServiceActions = new ArrayList<NakedObjectAction>();
            for (int j = 0; j < serviceActions.length; j++) {
                final NakedObjectAction serviceAction = serviceActions[j];
                if (serviceAction.containsFacet(HiddenFacet.class)) {
                    // ignore if permanently hidden
                    continue;
                }
                // see if qualifies by inspecting all parameters
                if (matchesParameterOf(serviceAction)) {
                    matchingServiceActions.add(serviceAction);
                }
            }
            // only add if there are matching subactions.
            if (matchingServiceActions.size() > 0) {
                final NakedObjectAction[] asArray = matchingServiceActions.toArray(new NakedObjectAction[matchingServiceActions
                        .size()]);
                final NakedObjectActionSet nakedObjectActionSet = new NakedObjectActionSet("id", serviceAdapter.titleString(),
                    asArray, runtimeContext);
                serviceActionSets.add(nakedObjectActionSet);
            }

        }
        return serviceActionSets.toArray(new NakedObjectAction[] {});
    }

    private boolean matchesParameterOf(final NakedObjectAction serviceAction) {
        final NakedObjectActionParameter[] params = serviceAction.getParameters();
        for (int k = 0; k < params.length; k++) {
            if ( isOfType(params[k].getSpecification())) {
                return true;
            }
        }
        return false;
    }

    public Consent isValid(final NakedObject inObject) {
        return isValidResult(inObject).createConsent();
    }

    /**
     * TODO: currently this method is hard-coded to assume all interactions are initiated
     * {@link InteractionInvocationMethod#BY_USER by user}.
     */
    public InteractionResult isValidResult(final NakedObject targetNakedObject) {
        final ObjectValidityContext validityContext = createValidityInteractionContext(getAuthenticationSession(),
                InteractionInvocationMethod.BY_USER, targetNakedObject);
        return InteractionUtils.isValidResult(this, validityContext);
    }

    /**
     * Create an {@link InteractionContext} representing an attempt to save the object.
     */
    public ObjectValidityContext createValidityInteractionContext(
            final AuthenticationSession session,
            final InteractionInvocationMethod interactionMethod,
            final NakedObject targetNakedObject) {
        return new ObjectValidityContext(session, interactionMethod, targetNakedObject, getIdentifier());
    }

    public Persistability persistability() {
        return Persistability.USER_PERSISTABLE;
    }

    // //////////////////////////////////////////////////////////////////////
    // convenience isXxx (looked up from facets)
    // //////////////////////////////////////////////////////////////////////

    public boolean isParseable() {
        return containsFacet(ParseableFacet.class);
    }

    public boolean isEncodeable() {
        return containsFacet(EncodableFacet.class);
    }

    public boolean isValueOrIsAggregated() {
        return containsFacet(AggregatedFacet.class) || containsFacet(ValueFacet.class);
    }

    public boolean isCollection() {
        return containsFacet(CollectionFacet.class);
    }

    public boolean isObject() {
        return !isCollection();
    }

    public boolean isImmutable() {
        return containsFacet(ImmutableFacet.class);
    }
    
    // //////////////////////////////////////////////////////////////////////
    // misc
    // //////////////////////////////////////////////////////////////////////

    public boolean isCollectionOrIsAggregated() {
        return false;
    }

    public Object createObject(CreationMode creationMode) {
        throw new UnsupportedOperationException(getFullName());
    }



    // //////////////////////////////////////////////////////////////////////
    // toString
    // //////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        final ToString str = new ToString(this);
        str.append("class", fullName);
        return str.toString();
    }

    // //////////////////////////////////////////////////////////////////////
    // Dependencies (injected in constructor)
    // //////////////////////////////////////////////////////////////////////

    public RuntimeContext getRuntimeContext() {
        return runtimeContext;
    }

    /**
     * Derived from {@link #getRuntimeContext() runtime context}.
     */
    protected final AuthenticationSession getAuthenticationSession() {
        return getRuntimeContext().getAuthenticationSession();
    }


}

// Copyright (c) Naked Objects Group Ltd.
