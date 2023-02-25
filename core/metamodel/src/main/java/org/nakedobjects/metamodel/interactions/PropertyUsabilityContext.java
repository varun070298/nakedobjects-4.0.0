package org.nakedobjects.metamodel.interactions;

import static org.nakedobjects.metamodel.util.NakedObjectUtils.unwrap;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.applib.events.PropertyUsabilityEvent;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.consent.InteractionContextType;
import org.nakedobjects.metamodel.consent.InteractionInvocationMethod;


/**
 * See {@link InteractionContext} for overview; analogous to {@link PropertyUsabilityEvent}.
 */
public class PropertyUsabilityContext extends UsabilityContext<PropertyUsabilityEvent> {

    public PropertyUsabilityContext(
            final AuthenticationSession session,
            final InteractionInvocationMethod invocationMethod,
            final NakedObject target,
            final Identifier identifier) {
        super(InteractionContextType.PROPERTY_USABLE, session, invocationMethod, identifier, target);
    }

    @Override
    public PropertyUsabilityEvent createInteractionEvent() {
        return new PropertyUsabilityEvent(unwrap(getTarget()), getIdentifier());
    }

}
