package org.nakedobjects.metamodel.interactions;

import static org.nakedobjects.metamodel.util.NakedObjectUtils.unwrap;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.applib.events.ActionInvocationEvent;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.consent.InteractionContextType;
import org.nakedobjects.metamodel.consent.InteractionInvocationMethod;


/**
 * See {@link InteractionContext} for overview; analogous to {@link ActionInvocationEvent}.
 */
public class ActionInvocationContext extends ValidityContext<ActionInvocationEvent> {

    private final NakedObject[] args;

    public ActionInvocationContext(
            final AuthenticationSession session,
            final InteractionInvocationMethod invocationMethod,
            final NakedObject target,
            final Identifier id,
            final NakedObject[] args) {
        super(InteractionContextType.ACTION_INVOKE, session, invocationMethod, id, target);
        this.args = args;
    }

    public NakedObject[] getArgs() {
        return args;
    }

    @Override
    public ActionInvocationEvent createInteractionEvent() {
        return new ActionInvocationEvent(unwrap(getTarget()), getIdentifier(), unwrap(getArgs()));
    }

}
