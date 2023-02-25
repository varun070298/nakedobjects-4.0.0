package org.nakedobjects.metamodel.spec.feature;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.filters.Filter;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.InteractionInvocationMethod;
import org.nakedobjects.metamodel.interactions.AccessContext;
import org.nakedobjects.metamodel.interactions.ActionInvocationContext;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.Target;


public interface NakedObjectAction extends NakedObjectMember {


    ////////////////////////////////////////////////////////
    // Target, realTarget, getOnType
    ////////////////////////////////////////////////////////

    /**
     * Returns where the action should be executed: explicitly locally on the client; explicitly remotely on
     * the server; or where it normally should be executed. By default instance methods should execute on the
     * server, static methods should execute on the client.
     * 
     * @see NakedObjectActionConstants#LOCAL
     * @see NakedObjectActionConstants#REMOTE
     * @see NakedObjectActionConstants#DEFAULT
     */
    Target getTarget();

    /**
     * Determine the real target for this action. If this action represents an object action than the target
     * is returned. If this action is on a service then that service will be returned.
     */
    NakedObject realTarget(NakedObject target);

    /**
     * Returns the specification for the type of object that this action can be invoked upon.
     */
    NakedObjectSpecification getOnType();

    /**
     * Return true if the action is run on a service object using the target object as a parameter.
     */
    boolean isContributed();

    boolean promptForParameters(NakedObject target);

    // //////////////////////////////////////////////////////////////////
    // Type
    // //////////////////////////////////////////////////////////////////

    /**
     * Returns the type of action: user, exploration or debug, or that it is a set of actions.
     * 
     * @see NakedObjectActionConstants#USER
     * @see NakedObjectActionConstants#EXPLORATION
     * @see NakedObjectActionConstants#DEBUG
     * @see NakedObjectActionConstants#SET
     */
    NakedObjectActionType getType();

    // //////////////////////////////////////////////////////////////////
    // ReturnType
    // //////////////////////////////////////////////////////////////////

    /**
     * Returns the specifications for the return type.
     */
    NakedObjectSpecification getReturnType();

    /**
     * Returns true if the represented action returns something, else returns false.
     */
    boolean hasReturn();

    // //////////////////////////////////////////////////////////////////
    // execute
    // //////////////////////////////////////////////////////////////////

    /**
     * Invokes the action's method on the target object given the specified set of parameters.
     */
    NakedObject execute(NakedObject target, NakedObject[] parameters);

    // //////////////////////////////////////////////////////////////////
    // valid
    // //////////////////////////////////////////////////////////////////

    /**
     * Creates an {@link ActionInvocationContext interaction context} representing an attempt to invoke this
     * action.
     * 
     * <p>
     * Typically it is easier to just call {@link #isProposedArgumentSetValid(NakedObject, NakedObject[])
     * {@link #isProposedArgumentSetValidResultSet(NakedObject, NakedObject[])}; this is provided as API for
     * symmetry with interactions (such as {@link AccessContext} accesses) have no corresponding vetoing
     * methods.
     */
    public ActionInvocationContext createActionInvocationInteractionContext(
            AuthenticationSession session,
            InteractionInvocationMethod invocationMethod,
            NakedObject targetObject,
            NakedObject[] proposedArguments);

    /**
     * Whether the provided argument set is valid, represented as a {@link Consent}.
     */
    Consent isProposedArgumentSetValid(NakedObject object, NakedObject[] proposedArguments);

    
    ////////////////////////////////////////////////////////
    // Actions (for action set)
    ////////////////////////////////////////////////////////

    /**
     * Lists the sub-actions that are available under this name. 
     * 
     * <p>
     * If any actions are returned then this action is only a set and not an action itself.
     */
    NakedObjectAction[] getActions();

    
    ////////////////////////////////////////////////////////
    // Parameters (declarative)
    ////////////////////////////////////////////////////////


    /**
     * Returns the number of parameters used by this method.
     */
    int getParameterCount();

    /**
     * Returns set of parameter information.
     * 
     * <p>
     * Implementations may build this array lazily or eagerly as required.
     * 
     * @return
     */
    NakedObjectActionParameter[] getParameters();

    /**
     * Returns the {@link NakedObjectSpecification type} of each of the
     * {@link #getParameters() parameters}.
     */
    NakedObjectSpecification[] getParameterTypes();
    
    /**
     * Returns set of parameter information matching the supplied filter.
     * 
     * @return
     */
    NakedObjectActionParameter[] getParameters(Filter<NakedObjectActionParameter> filter);


    ////////////////////////////////////////////////////////
    // Parameters (per instance)
    ////////////////////////////////////////////////////////

    /**
     * Returns the defaults references/values to be used for the action.
     */
    NakedObject[] getDefaults(NakedObject target);

    /**
     * Returns a list of possible references/values for each parameter, which the user can choose from.
     */
    NakedObject[][] getChoices(NakedObject target);


    


}
// Copyright (c) Naked Objects Group Ltd.
