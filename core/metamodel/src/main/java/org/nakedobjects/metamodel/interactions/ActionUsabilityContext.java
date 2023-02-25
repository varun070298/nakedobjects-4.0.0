package org.nakedobjects.metamodel.interactions;

import static org.nakedobjects.metamodel.util.NakedObjectUtils.unwrap;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.applib.events.ActionUsabilityEvent;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.consent.InteractionContextType;
import org.nakedobjects.metamodel.consent.InteractionInvocationMethod;


/**
 * See {@link InteractionContext} for overview; analogous to {@link ActionUsabilityEvent}.
 */
public class ActionUsabilityContext extends UsabilityContext<ActionUsabilityEvent> {

    public ActionUsabilityContext(
            final AuthenticationSession session,
            final InteractionInvocationMethod invocationMethod,
            final NakedObject target,
            final Identifier id) {
        super(InteractionContextType.ACTION_USABLE, session, invocationMethod, id, target);
    }

    @Override
    public ActionUsabilityEvent createInteractionEvent() {
        return new ActionUsabilityEvent(unwrap(getTarget()), getIdentifier());
    }

}
