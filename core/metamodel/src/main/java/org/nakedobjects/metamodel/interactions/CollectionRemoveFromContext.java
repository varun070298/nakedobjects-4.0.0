package org.nakedobjects.metamodel.interactions;

import static org.nakedobjects.metamodel.util.NakedObjectUtils.unwrap;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.applib.events.CollectionRemoveFromEvent;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.consent.InteractionContextType;
import org.nakedobjects.metamodel.consent.InteractionInvocationMethod;


/**
 * See {@link InteractionContext} for overview; analogous to {@link CollectionRemoveFromEvent}.
 */
public class CollectionRemoveFromContext extends ValidityContext<CollectionRemoveFromEvent> {

    private final NakedObject proposed;

    public CollectionRemoveFromContext(
            final AuthenticationSession session,
            final InteractionInvocationMethod invocationMethod,
            final NakedObject target,
            final Identifier identifier,
            final NakedObject proposed) {
        super(InteractionContextType.COLLECTION_REMOVE_FROM, session, invocationMethod, identifier, target);

        this.proposed = proposed;
    }

    public NakedObject getProposed() {
        return proposed;
    }

    @Override
    public CollectionRemoveFromEvent createInteractionEvent() {
        return new CollectionRemoveFromEvent(unwrap(getTarget()), getIdentifier(), unwrap(getProposed()));
    }

}
