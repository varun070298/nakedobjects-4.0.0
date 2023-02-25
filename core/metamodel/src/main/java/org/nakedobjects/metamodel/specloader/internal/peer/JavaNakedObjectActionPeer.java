package org.nakedobjects.metamodel.specloader.internal.peer;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.actions.debug.DebugFacet;
import org.nakedobjects.metamodel.facets.actions.executed.ExecutedFacet;
import org.nakedobjects.metamodel.facets.actions.exploration.ExplorationFacet;
import org.nakedobjects.metamodel.facets.actions.invoke.ActionInvocationFacet;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.Target;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionConstants;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionType;
import org.nakedobjects.metamodel.specloader.ReflectiveActionException;


/*
 * TODO (in all Java...Peer classes) make all methodsArray throw ReflectiveActionException when 
 * an exception occurs when calling a method reflectively (see execute method).  Then instead of 
 * calling invocationExcpetion() the exception will be passed though, and dealt with generally by 
 * the reflection package (which will be the same for all reflectors and will allow the message to
 * be better passed back to the client).
 */
public class JavaNakedObjectActionPeer extends JavaNakedObjectMemberPeer implements NakedObjectActionPeer {

    private final JavaNakedObjectActionParamPeer[] parameters;

    public JavaNakedObjectActionPeer(final Identifier identifier, final JavaNakedObjectActionParamPeer[] parameters) {
        super(identifier);
        this.parameters = parameters;
    }

    // ////////////////////// Type etc ////////////////////

    public Target getTarget() {
        final ExecutedFacet executedFacet = getFacet(ExecutedFacet.class);
        return executedFacet == null ? NakedObjectActionConstants.DEFAULT : executedFacet.getTarget();
    }

    public NakedObjectActionType getType() {
        final DebugFacet debugFacet = getFacet(DebugFacet.class);
        if (debugFacet != null) {
            return NakedObjectActionConstants.DEBUG;
        }
        final ExplorationFacet explorationFacet = getFacet(ExplorationFacet.class);
        if (explorationFacet != null) {
            return NakedObjectActionConstants.EXPLORATION;
        }
        return NakedObjectActionConstants.USER;
    }

    // ////////////////////// execute ////////////////////

    public NakedObject execute(final NakedObject inObject, final NakedObject[] parameters) throws ReflectiveActionException {
        final ActionInvocationFacet facet = getFacet(ActionInvocationFacet.class);
        return facet.invoke(inObject, parameters);
    }

    // ////////////////////// Parameters ////////////////////

    public int getParameterCount() {
        return parameters.length;
    }

    public NakedObjectActionParamPeer[] getParameters() {
        return parameters;
    }

    // ///////////////////////// toString, debugData /////////////////////////

    @Override
    public String toString() {
        final StringBuffer parameters = new StringBuffer();
        final ActionInvocationFacet facet = getFacet(ActionInvocationFacet.class);
        final NakedObjectSpecification onType = facet.getOnType();
        return "JavaAction [name=" + getIdentifier().getMemberName() + ",type=" + onType.getShortName() + ",parameters="
                + parameters + "]";
    }

}
// Copyright (c) Naked Objects Group Ltd.
