package org.nakedobjects.metamodel.interactions;

import static org.nakedobjects.metamodel.util.NakedObjectUtils.unwrap;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.applib.events.CollectionAddToEvent;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.consent.InteractionContextType;
import org.nakedobjects.metamodel.consent.InteractionInvocationMethod;


/**
 * See {@link InteractionContext} for overview; analogous to {@link CollectionAddToEvent}.
 */
public class CollectionAddToContext extends ValidityContext<CollectionAddToEvent> {

    private final NakedObject proposed;

    public CollectionAddToContext(
            final AuthenticationSession session,
            final InteractionInvocationMethod invocationMethod,
            final NakedObject target,
            final Identifier id,
            final NakedObject proposed) {
        super(InteractionContextType.COLLECTION_ADD_TO, session, invocationMethod, id, target);

        this.proposed = proposed;
    }

    public NakedObject getProposed() {
        return proposed;
    }

    @Override
    public CollectionAddToEvent createInteractionEvent() {
        return new CollectionAddToEvent(unwrap(getTarget()), getIdentifier(), unwrap(getProposed()));
    }

}
