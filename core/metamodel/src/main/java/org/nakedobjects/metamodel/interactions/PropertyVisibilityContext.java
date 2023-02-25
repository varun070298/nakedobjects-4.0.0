package org.nakedobjects.metamodel.interactions;

import static org.nakedobjects.metamodel.util.NakedObjectUtils.unwrap;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.applib.events.PropertyVisibilityEvent;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.consent.InteractionContextType;
import org.nakedobjects.metamodel.consent.InteractionInvocationMethod;


/**
 * See {@link InteractionContext} for overview; analogous to {@link PropertyVisibilityEvent}.
 */
public class PropertyVisibilityContext extends VisibilityContext<PropertyVisibilityEvent> {

    public PropertyVisibilityContext(
            final AuthenticationSession session,
            final InteractionInvocationMethod invocationMethod,
            final NakedObject target,
            final Identifier identifier) {
        super(InteractionContextType.PROPERTY_VISIBLE, session, invocationMethod, identifier, target);
    }

    @Override
    public PropertyVisibilityEvent createInteractionEvent() {
        return new PropertyVisibilityEvent(unwrap(getTarget()), getIdentifier());
    }

}
