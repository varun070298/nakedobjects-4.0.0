package org.nakedobjects.metamodel.interactions;

import static org.nakedobjects.metamodel.util.NakedObjectUtils.unwrap;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.applib.events.CollectionAccessEvent;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.consent.InteractionContextType;
import org.nakedobjects.metamodel.consent.InteractionInvocationMethod;


/**
 * See {@link InteractionContext} for overview; analogous to {@link CollectionAccessEvent}.
 */
public class CollectionAccessContext extends AccessContext<CollectionAccessEvent> {

    public CollectionAccessContext(
            final AuthenticationSession session,
            final InteractionInvocationMethod invocationMethod,
            final NakedObject target,
            final Identifier identifier) {
        super(InteractionContextType.COLLECTION_READ, session, invocationMethod, identifier, target);
    }

    @Override
    public CollectionAccessEvent createInteractionEvent() {
        return new CollectionAccessEvent(unwrap(getTarget()), getIdentifier());
    }

}
