package org.nakedobjects.metamodel.interactions;

import static org.nakedobjects.metamodel.util.NakedObjectUtils.unwrap;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.applib.events.ParseValueEvent;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.consent.InteractionContextType;
import org.nakedobjects.metamodel.consent.InteractionInvocationMethod;


/**
 * See {@link InteractionContext} for overview; analogous to {@link ParseValueEvent}.
 */
public class ParseValueContext extends ValidityContext<ParseValueEvent> implements ProposedHolder {

    private final NakedObject proposed;

	public ParseValueContext(
            final AuthenticationSession session,
            final InteractionInvocationMethod invocationMethod,
            final NakedObject target,
            final Identifier identifier,
            final NakedObject proposed) {
        super(InteractionContextType.PARSE_VALUE, session, invocationMethod, identifier, target);
        this.proposed = proposed;
    }

	public NakedObject getProposed() {
		return proposed;
	}

    @Override
    public ParseValueEvent createInteractionEvent() {
        String proposedPojo = (String)unwrap(getProposed());
		return new ParseValueEvent(unwrap(getTarget()), getIdentifier(), proposedPojo);
    }

}
