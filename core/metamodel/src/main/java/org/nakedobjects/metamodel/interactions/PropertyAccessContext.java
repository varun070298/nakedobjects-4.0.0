package org.nakedobjects.metamodel.interactions;

import static org.nakedobjects.metamodel.util.NakedObjectUtils.unwrap;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.applib.events.PropertyAccessEvent;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.consent.InteractionContextType;
import org.nakedobjects.metamodel.consent.InteractionInvocationMethod;


/**
 * See {@link InteractionContext} for overview; analogous to {@link PropertyAccessEvent}.
 */
public class PropertyAccessContext extends AccessContext<PropertyAccessEvent> {

    private final NakedObject value;

    public PropertyAccessContext(
            final AuthenticationSession session,
            final InteractionInvocationMethod invocationMethod,
            final NakedObject target,
            final Identifier id,
            final NakedObject value) {
        super(InteractionContextType.PROPERTY_READ, session, invocationMethod, id, target);

        this.value = value;
    }

    /**
     * The current value for a property.
     */
    public NakedObject getValue() {
        return value;
    }

    @Override
    public PropertyAccessEvent createInteractionEvent() {
        return new PropertyAccessEvent(unwrap(getTarget()), getIdentifier(), unwrap(getValue()));
    }

}
