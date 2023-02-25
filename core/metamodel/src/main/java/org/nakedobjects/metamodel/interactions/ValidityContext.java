package org.nakedobjects.metamodel.interactions;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.applib.events.ValidityEvent;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.consent.InteractionContextType;
import org.nakedobjects.metamodel.consent.InteractionInvocationMethod;


/**
 * See {@link InteractionContext} for overview; analogous to {@link ValidityEvent}
 */
public abstract class ValidityContext<T extends ValidityEvent> extends InteractionContext<T> {

    public ValidityContext(
            final InteractionContextType interactionType,
            final AuthenticationSession session,
            final InteractionInvocationMethod invocationMethod,
            final Identifier identifier,
            final NakedObject target) {
        super(interactionType, session, invocationMethod, identifier, target);
    }

}

// Copyright (c) Naked Objects Group Ltd.
