package org.nakedobjects.metamodel.specloader.internal;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.Instance;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.commons.exceptions.UnknownTypeException;
import org.nakedobjects.metamodel.commons.filters.Filter;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.InteractionInvocationMethod;
import org.nakedobjects.metamodel.consent.InteractionResultSet;
import org.nakedobjects.metamodel.exceptions.ModelException;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.actions.choices.ActionChoicesFacet;
import org.nakedobjects.metamodel.facets.actions.choices.ActionParameterChoicesFacet;
import org.nakedobjects.metamodel.facets.actions.debug.DebugFacet;
import org.nakedobjects.metamodel.facets.actions.defaults.ActionDefaultsFacet;
import org.nakedobjects.metamodel.facets.actions.defaults.ActionParameterDefaultsFacet;
import org.nakedobjects.metamodel.facets.actions.executed.ExecutedFacet;
import org.nakedobjects.metamodel.facets.actions.executed.ExecutedFacet.Where;
import org.nakedobjects.metamodel.facets.actions.exploration.ExplorationFacet;
import org.nakedobjects.metamodel.facets.actions.invoke.ActionInvocationFacet;
import org.nakedobjects.metamodel.interactions.ActionInvocationContext;
import org.nakedobjects.metamodel.interactions.ActionUsabilityContext;
import org.nakedobjects.metamodel.interactions.ActionVisibilityContext;
import org.nakedobjects.metamodel.interactions.InteractionUtils;
import org.nakedobjects.metamodel.interactions.UsabilityContext;
import org.nakedobjects.metamodel.interactions.ValidityContext;
import org.nakedobjects.metamodel.interactions.VisibilityContext;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.runtimecontext.spec.feature.NakedObjectMemberAbstract;
import org.nakedobjects.metamodel.services.container.query.QueryFindAllInstances;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.SpecificationFacets;
import org.nakedobjects.metamodel.spec.Target;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAction;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionConstants;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionParameter;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionType;
import org.nakedobjects.metamodel.specloader.internal.peer.NakedObjectActionParamPeer;
import org.nakedobjects.metamodel.specloader.internal.peer.NakedObjectActionPeer;


public class NakedObjectActionImpl extends NakedObjectMemberAbstract implements NakedObjectAction {
    private final static Logger LOG = Logger.getLogger(NakedObjectActionImpl.class);

    public static NakedObjectActionType getType(final String typeStr) {
    	NakedObjectActionType type = NakedObjectActionType.valueOf(typeStr);
    	if (type == null) {
    		throw new IllegalArgumentException();
    	} 
    	return type;
    }

    private final NakedObjectActionPeer nakedObjectActionPeer;
    /**
     * Lazily initialized by {@link #getParameters()} (so don't use directly!)
     */
    private NakedObjectActionParameter[] parameters;

    /**
     * Controls the initialization of {@link #contributed}.
     */
    private boolean knownWhetherContributed = false;
    /**
     * Lazily initialized, controlled by {@link #knownWhetherContributed}
     * 
     * @see #isContributed()
     */
    private boolean contributed;

    // //////////////////////////////////////////////////////////////////
    // Constructors
    // //////////////////////////////////////////////////////////////////

    public NakedObjectActionImpl(
    		final String methodId, 
    		final NakedObjectActionPeer nakedObjectActionPeer, 
    		final RuntimeContext runtimeContext) {
        super(methodId, nakedObjectActionPeer, MemberType.ACTION, runtimeContext);
        this.nakedObjectActionPeer = nakedObjectActionPeer;
    }

    // //////////////////////////////////////////////////////////////////
    // ReturnType, OnType, Actions (set)
    // //////////////////////////////////////////////////////////////////

    /**
     * Always returns <tt>null</tt>.
     */
    public NakedObjectSpecification getSpecification() {
        return null;
    }

    public NakedObjectSpecification getReturnType() {
        final ActionInvocationFacet facet = getActionInvocationFacet();
        return facet.getReturnType();
    }

    /**
     * Returns true if the represented action returns something, else returns false.
     */
    public boolean hasReturn() {
        return getReturnType() != null;
    }

    public NakedObjectSpecification getOnType() {
        final ActionInvocationFacet facet = getActionInvocationFacet();
        return facet.getOnType();
    }

    public NakedObjectAction[] getActions() {
        return new NakedObjectAction[0];
    }

    // /////////////////////////////////////////////////////////////
    // getInstance
    // /////////////////////////////////////////////////////////////
    
    public Instance getInstance(NakedObject nakedObject) {
        NakedObjectAction specification = this;
        return nakedObject.getInstance(specification);
    }

    // /////////////////////////////////////////////////////////////
    // Target, Type, IsContributed
    // /////////////////////////////////////////////////////////////

    public Target getTarget() {
        final ExecutedFacet facet = getFacet(ExecutedFacet.class);
        final Where executeWhere = facet.value();
        if (executeWhere == Where.LOCALLY) {
            return NakedObjectActionConstants.LOCAL;
        } else if (executeWhere == Where.REMOTELY) {
            return NakedObjectActionConstants.REMOTE;
        } else if (executeWhere == Where.DEFAULT) {
            return NakedObjectActionConstants.DEFAULT;
        } else {
            throw new UnknownTypeException(executeWhere);
        }
    }

    public NakedObjectActionType getType() {
        Facet facet = getFacet(DebugFacet.class);
        if (facet != null) {
            return NakedObjectActionConstants.DEBUG;
        }
        facet = getFacet(ExplorationFacet.class);
        if (facet != null) {
            return NakedObjectActionConstants.EXPLORATION;
        }
        return NakedObjectActionConstants.USER;
    }

    public boolean isContributed() {
    	if (!knownWhetherContributed) {
    		contributed = determineWhetherContributed();
    		knownWhetherContributed = true;
    	}
        return contributed;
    }

	private boolean determineWhetherContributed() {
		if (getOnType().isService() && getParameterCount() > 0) {
            final NakedObjectActionParameter[] params = getParameters();
            for (int i = 0; i < params.length; i++) {
                if (params[i].isObject()) {
                    return true;
                }
            }
        }
        return false;
	}

    // /////////////////////////////////////////////////////////////
    // Parameters
    // /////////////////////////////////////////////////////////////

    public int getParameterCount() {
        return nakedObjectActionPeer.getParameters().length;
    }

    public boolean promptForParameters(final NakedObject target) {
    	NakedObjectActionParameter[] parameters = getParameters();
        if (isContributed() && !target.getSpecification().isService()) {
            return getParameterCount() > 1 || !target.getSpecification().isOfType(parameters[0].getSpecification());
        } else {
            return getParameterCount() > 0;
        }
    }

    /**
     * Build lazily by {@link #getParameters()}.
     * 
     * <p>
     * Although this is lazily loaded, the method is also <tt>synchronized</tt> so there shouldn't be any
     * thread race conditions.
     */
    public synchronized NakedObjectActionParameter[] getParameters() {
        if (this.parameters == null) {
            final int parameterCount = getParameterCount();
            final NakedObjectActionParameter[] parameters = new NakedObjectActionParameter[parameterCount];
            final NakedObjectActionParamPeer[] paramPeers = nakedObjectActionPeer.getParameters();
            for (int i = 0; i < parameterCount; i++) {
                final NakedObjectSpecification specification = paramPeers[i].getSpecification();
                if (specification.isParseable()) {
                    parameters[i] = new NakedObjectActionParameterParseable(i, this, paramPeers[i]);
                } else if (specification.isObject()) {
                    parameters[i] = new OneToOneActionParameterImpl(i, this, paramPeers[i]);
                } else if (specification.isCollection()) {
                    throw new UnknownTypeException("collections not supported as parameters: " + getIdentifier());
                } else {
                    throw new UnknownTypeException(specification);
                }
            }
            this.parameters = parameters;
        }
        return parameters;
    }

    public synchronized NakedObjectSpecification[] getParameterTypes() {
        List<NakedObjectSpecification> parameterTypes = new ArrayList<NakedObjectSpecification>();
        NakedObjectActionParameter[] parameters = getParameters();
        for(NakedObjectActionParameter parameter: parameters) {
            parameterTypes.add(parameter.getSpecification());
        }
        return parameterTypes.toArray(new NakedObjectSpecification[]{});
    }

    public NakedObjectActionParameter[] getParameters(final Filter<NakedObjectActionParameter> filter) {
        final NakedObjectActionParameter[] allParameters = getParameters();
        final NakedObjectActionParameter[] selectedParameters = new NakedObjectActionParameter[allParameters.length];
        int v = 0;
        for (int i = 0; i < allParameters.length; i++) {
            if (filter.accept(allParameters[i])) {
                selectedParameters[v++] = allParameters[i];
            }
        }
        final NakedObjectActionParameter[] parameters = new NakedObjectActionParameter[v];
        System.arraycopy(selectedParameters, 0, parameters, 0, v);
        return parameters;
    }

    private NakedObjectActionParameter getParameter(final int position) {
        final NakedObjectActionParameter[] parameters = getParameters();
        if (position >= parameters.length) {
            throw new IllegalArgumentException("getParameter(int): only " + parameters.length + " parameters, position="
                    + position);
        }
        return parameters[position];
    }

    // /////////////////////////////////////////////////////////////
    // Visible (or hidden)
    // /////////////////////////////////////////////////////////////

    public VisibilityContext<?> createVisibleInteractionContext(
            final AuthenticationSession session,
            final InteractionInvocationMethod invocationMethod,
            final NakedObject targetNakedObject) {
        return new ActionVisibilityContext(session, invocationMethod, targetNakedObject, getIdentifier());
    }

    @Override
    public Consent isVisible(final AuthenticationSession session, final NakedObject target) {
        return super.isVisible(session, realTarget(target));
    }

    // /////////////////////////////////////////////////////////////
    // Usable (or disabled)
    // /////////////////////////////////////////////////////////////

    public UsabilityContext<?> createUsableInteractionContext(
            final AuthenticationSession session,
            final InteractionInvocationMethod invocationMethod,
            final NakedObject targetNakedObject) {
        return new ActionUsabilityContext(session, invocationMethod, targetNakedObject, getIdentifier());
    }

    @Override
    public Consent isUsable(final AuthenticationSession session, final NakedObject target) {
        return super.isUsable(session, realTarget(target));
    }

    // //////////////////////////////////////////////////////////////////
    // validate
    // //////////////////////////////////////////////////////////////////

    /**
     * TODO: currently this method is hard-coded to assume all interactions are initiated
     * {@link InteractionInvocationMethod#BY_USER by user}.
     */
    public Consent isProposedArgumentSetValid(final NakedObject object, final NakedObject[] proposedArguments) {
        return isProposedArgumentSetValidResultSet(realTarget(object), proposedArguments).createConsent();
    }

    private InteractionResultSet isProposedArgumentSetValidResultSet(
            final NakedObject object,
            final NakedObject[] proposedArguments) {
        final InteractionInvocationMethod invocationMethod = InteractionInvocationMethod.BY_USER;

        final InteractionResultSet resultSet = new InteractionResultSet();
        final NakedObjectActionParameter[] actionParameters = getParameters();
        if (proposedArguments != null) {
            // TODO: doesn't seem to be used...
            // NakedObject[] params = realParameters(object, proposedArguments);
            for (int i = 0; i < proposedArguments.length; i++) {
                final ValidityContext<?> ic = actionParameters[i].createProposedArgumentInteractionContext(getAuthenticationSession(),
                        invocationMethod, object, proposedArguments, i);
                InteractionUtils.isValidResultSet(getParameter(i), ic, resultSet);
            }
        }
        // only check the action's own validity if all the arguments are OK.
        if (resultSet.isAllowed()) {
            final ValidityContext<?> ic = createActionInvocationInteractionContext(getAuthenticationSession(), invocationMethod, object,
                    proposedArguments);
            InteractionUtils.isValidResultSet(this, ic, resultSet);
        }
        return resultSet;
    }

    public ActionInvocationContext createActionInvocationInteractionContext(
            final AuthenticationSession session,
            final InteractionInvocationMethod invocationMethod,
            final NakedObject targetObject,
            final NakedObject[] proposedArguments) {
        return new ActionInvocationContext(getAuthenticationSession(), invocationMethod, targetObject, getIdentifier(), proposedArguments);
    }

    // //////////////////////////////////////////////////////////////////
    // execute
    // //////////////////////////////////////////////////////////////////

    public NakedObject execute(final NakedObject object, final NakedObject[] parameters) {
        LOG.debug("execute action " + object + "." + getId());
        final NakedObject[] params = realParameters(object, parameters);
        final NakedObject target = realTarget(object);
        final ActionInvocationFacet facet = getFacet(ActionInvocationFacet.class);
        return facet.invoke(target, params);
    }

    private ActionInvocationFacet getActionInvocationFacet() {
        return nakedObjectActionPeer.getFacet(ActionInvocationFacet.class);
    }

    /**
     * Previously (prior to 3.0.x) this method had a check to see if the action was on an instance. With the
     * reflector redesign this has been removed, because NOF 3.x only supports instance methods, not
     * class-level methods.
     */
    public NakedObject realTarget(final NakedObject target) {
        if (target == null) {
            return findService();
        } else if (target.getSpecification().isService()) {
            return target;
        } else if (isContributed()) {
            return findService();
        } else {
            return target;
        }
    }

    private NakedObject findService() {
        final List<NakedObject> services = getRuntimeContext().getServices();
        for (NakedObject serviceAdapter : services) {
            if (serviceAdapter.getSpecification() == getOnType()) {
                return serviceAdapter;
            }
        }
        throw new NakedObjectException("failed to find service for action " + this.getName());
    }

    private NakedObject[] realParameters(final NakedObject target, final NakedObject[] parameters) {
        if (parameters != null) {
            return parameters;
        }
        return isContributed() ? new NakedObject[] { target } : new NakedObject[0];
    }

    // //////////////////////////////////////////////////////////////////
    // defaults
    // //////////////////////////////////////////////////////////////////

    public NakedObject[] getDefaults(final NakedObject target) {
        final NakedObject realTarget = realTarget(target);

        final int parameterCount = getParameterCount();
        final NakedObjectActionParameter[] parameters = getParameters();

        final Object[] parameterDefaultPojos;

        // TODO here and elsewhere: the target needs to be
        // replaced by the service where the action is for a service!
        // set a flag on entry if for a service - or get from spec using isService
        final ActionDefaultsFacet facet = getFacet(ActionDefaultsFacet.class);
        if (!facet.isNoop()) {
        	// use the old defaultXxx approach
            parameterDefaultPojos = facet.getDefaults(realTarget);
            if (parameterDefaultPojos.length != parameterCount) {
                throw new ModelException("Defaults array of incompatible size; expected " + parameterCount + " elements, but was "
                        + parameterDefaultPojos.length + " for " + facet);
            } 
            for (int i = 0; i < parameterCount; i++) {
                if (parameterDefaultPojos[i] != null) {
                     NakedObjectSpecification componentSpec = getRuntimeContext().getSpecificationLoader().loadSpecification(
                            parameterDefaultPojos[i].getClass());
                    NakedObjectSpecification parameterSpec = parameters[i].getSpecification();
                    if (!componentSpec.isOfType(parameterSpec)) {
                        throw new ModelException("Defaults type incompatible with parameter " + (i + 1) + " type; expected "
                                + parameterSpec.getFullName() + ", but was " + componentSpec.getFullName());
                    }
                }
            }
        } else {
        	// use the new defaultNXxx approach for each param in turn
        	// (the reflector will have made sure both aren't installed).
        	parameterDefaultPojos = new Object[parameterCount];
            for (int i = 0; i < parameterCount; i++) {
            	ActionParameterDefaultsFacet paramFacet = parameters[i].getFacet(ActionParameterDefaultsFacet.class);
            	if (paramFacet != null && !paramFacet.isNoop()) {
            		parameterDefaultPojos[i] = paramFacet.getDefault(realTarget);
            	} else {
            		parameterDefaultPojos[i] = null;
            	}
            }
        }

        final NakedObject[] parameterDefaultAdapters = new NakedObject[parameterCount];
        if (parameterDefaultPojos != null) {
            for (int i = 0; i < parameterCount; i++) {
                parameterDefaultAdapters[i] = adapterFor(parameterDefaultPojos[i]);
            }
        }
        
        // set the target if contributed.
        if (isContributed() && target != null) {
            for (int i = 0; i < parameterCount; i++) {
                if (target.getSpecification().isOfType(parameters[i].getSpecification())) {
                    parameterDefaultAdapters[i] = target;
                }
            }
        }
        return parameterDefaultAdapters;
    }

    private NakedObject adapterFor(final Object pojo) {
        return pojo == null ? null : getRuntimeContext().adapterFor(pojo);
    }


    // /////////////////////////////////////////////////////////////
    // options (choices)
    // /////////////////////////////////////////////////////////////

    public NakedObject[][] getChoices(final NakedObject target) {
        final NakedObject realTarget = realTarget(target);
        
        final int parameterCount = getParameterCount();
        Object[][] parameterChoicesPojos;
        
        final ActionChoicesFacet facet = getFacet(ActionChoicesFacet.class);
        NakedObjectActionParameter[] parameters = getParameters();
        
        if (!facet.isNoop()) {
            // using the old choicesXxx() approach
        	parameterChoicesPojos = facet.getChoices(realTarget);
        	
            // if no options, or not the right number of pojos, then default
            if (parameterChoicesPojos == null) {
                parameterChoicesPojos = new Object[parameterCount][];
            } else if (parameterChoicesPojos.length != parameterCount)  {
                throw new ModelException("Choices array of incompatible size; expected " + parameterCount + " elements, but was " + parameterChoicesPojos.length + " for " + facet);
            }
        } else {
        	// use the new choicesNXxx approach for each param in turn
        	// (the reflector will have made sure both aren't installed).
        	
        	parameterChoicesPojos = new Object[parameterCount][];
            for (int i = 0; i < parameterCount; i++) {
            	ActionParameterChoicesFacet paramFacet = parameters[i].getFacet(ActionParameterChoicesFacet.class);
            	if (paramFacet != null && !paramFacet.isNoop()) {
            		parameterChoicesPojos[i] = paramFacet.getChoices(realTarget);
            	} else {
            		parameterChoicesPojos[i] = new Object[0];
            	}
            }
        }


        final NakedObject[][] parameterChoicesAdapters = new NakedObject[parameterCount][];
        for (int i = 0; i < parameterCount; i++) {
            final NakedObjectSpecification paramSpec = parameters[i].getSpecification();

            if (parameterChoicesPojos[i] != null && parameterChoicesPojos[i].length > 0) {
                NakedObjectActionParameterAbstract.checkChoicesType(getRuntimeContext(), parameterChoicesPojos[i], paramSpec);
                parameterChoicesAdapters[i] = new NakedObject[parameterChoicesPojos[i].length];
                for (int j = 0; j < parameterChoicesPojos[i].length; j++) {
                    parameterChoicesAdapters[i][j] = adapterFor(parameterChoicesPojos[i][j]);
                }
            } else if (SpecificationFacets.isBoundedSet(paramSpec)) {
                QueryFindAllInstances query = new QueryFindAllInstances(paramSpec);
				final List<NakedObject> allInstancesAdapter = getRuntimeContext().allMatchingQuery(query);
                parameterChoicesAdapters[i] = new NakedObject[allInstancesAdapter.size()];
                int j = 0;
                for(NakedObject adapter: allInstancesAdapter) {
                    parameterChoicesAdapters[i][j++] = adapter;
                }
            } else if (paramSpec.isObject()) {
                parameterChoicesAdapters[i] = new NakedObject[0];
            } else {
                throw new UnknownTypeException(paramSpec);
            }

            if (parameterChoicesAdapters[i].length == 0) {
                parameterChoicesAdapters[i] = null;
            }
        }

        return parameterChoicesAdapters;
    }


    // //////////////////////////////////////////////////////////////////
    // debug, toString
    // //////////////////////////////////////////////////////////////////

    public String debugData() {
        final DebugString debugString = new DebugString();
        nakedObjectActionPeer.debugData(debugString);
        return debugString.toString();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("Action [");
        sb.append(super.toString());
        sb.append(",type=");
        sb.append(getType());
        sb.append(",returns=");
        sb.append(getReturnType());
        sb.append(",parameters={");
        for (int i = 0; i < getParameterCount(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(getParameters()[i].getSpecification().getShortName());
        }
        sb.append("}]");
        return sb.toString();
    }






}
// Copyright (c) Naked Objects Group Ltd.
