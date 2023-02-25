package org.nakedobjects.metamodel.interactions;

import static org.nakedobjects.metamodel.util.NakedObjectUtils.unwrap;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.applib.events.CollectionVisibilityEvent;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.consent.InteractionContextType;
import org.nakedobjects.metamodel.consent.InteractionInvocationMethod;


/**
 * See {@link InteractionContext} for overview; analogous to {@link CollectionVisibilityEvent}.
 */
public class CollectionVisibilityContext extends VisibilityContext<CollectionVisibilityEvent> {

    public CollectionVisibilityContext(
            final AuthenticationSession session,
            final InteractionInvocationMethod invocationMethod,
            final NakedObject target,
            final Identifier identifier) {
        super(InteractionContextType.COLLECTION_VISIBLE, session, invocationMethod, identifier, target);
    }

    @Override
    public CollectionVisibilityEvent createInteractionEvent() {
        return new CollectionVisibilityEvent(unwrap(getTarget()), getIdentifier());
    }

}
