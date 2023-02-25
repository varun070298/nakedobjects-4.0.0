package org.nakedobjects.metamodel.interactions;

import static org.nakedobjects.metamodel.util.NakedObjectUtils.unwrap;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.applib.events.ObjectValidityEvent;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.consent.InteractionContextType;
import org.nakedobjects.metamodel.consent.InteractionInvocationMethod;


/**
 * See {@link InteractionContext} for overview; analogous to {@link ObjectValidityEvent}.
 */
public class ObjectValidityContext extends ValidityContext<ObjectValidityEvent> implements ProposedHolder {

    public ObjectValidityContext(
            final AuthenticationSession session,
            final InteractionInvocationMethod invocationMethod,
            final NakedObject target,
            final Identifier identifier) {
        super(InteractionContextType.OBJECT_VALIDATE, session, invocationMethod, identifier, target);
    }

    @Override
    public ObjectValidityEvent createInteractionEvent() {
        return new ObjectValidityEvent(unwrap(getTarget()), getIdentifier());
    }

	public NakedObject getProposed() {
		return getTarget();
	}

}
