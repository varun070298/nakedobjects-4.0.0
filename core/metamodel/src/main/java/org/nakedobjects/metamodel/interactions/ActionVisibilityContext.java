package org.nakedobjects.metamodel.interactions;

import static org.nakedobjects.metamodel.util.NakedObjectUtils.unwrap;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.applib.events.ActionVisibilityEvent;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.consent.InteractionContextType;
import org.nakedobjects.metamodel.consent.InteractionInvocationMethod;


/**
 * See {@link InteractionContext} for overview; analogous to {@link ActionVisibilityEvent}.
 */
public class ActionVisibilityContext extends VisibilityContext<ActionVisibilityEvent> {

    public ActionVisibilityContext(
            final AuthenticationSession session,
            final InteractionInvocationMethod invocationMethod,
            final NakedObject target,
            final Identifier identifier) {
        super(InteractionContextType.ACTION_VISIBLE, session, invocationMethod, identifier, target);
    }

    @Override
    public ActionVisibilityEvent createInteractionEvent() {
        return new ActionVisibilityEvent(unwrap(getTarget()), getIdentifier());
    }

}
