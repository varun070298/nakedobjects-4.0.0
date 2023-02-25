package org.nakedobjects.metamodel.interactions;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.applib.events.VisibilityEvent;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.consent.InteractionContextType;
import org.nakedobjects.metamodel.consent.InteractionInvocationMethod;


/**
 * See {@link InteractionContext} for overview; analogous to {@link VisibilityEvent}.
 */
public abstract class VisibilityContext<T extends VisibilityEvent> extends InteractionContext<T> {

    public VisibilityContext(
            final InteractionContextType interactionType,
            final AuthenticationSession session,
            final InteractionInvocationMethod invocationMethod,
            final Identifier identifier,
            final NakedObject target) {
        super(interactionType, session, invocationMethod, identifier, target);
    }

}

// Copyright (c) Naked Objects Group Ltd.
