package org.nakedobjects.metamodel.interactions;

import static org.nakedobjects.metamodel.util.NakedObjectUtils.unwrap;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.applib.events.PropertyModifyEvent;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.consent.InteractionContextType;
import org.nakedobjects.metamodel.consent.InteractionInvocationMethod;


/**
 * See {@link InteractionContext} for overview; analogous to {@link PropertyModifyEvent}.
 */
public class PropertyModifyContext extends ValidityContext<PropertyModifyEvent> implements ProposedHolder {

    private final NakedObject proposed;

    public PropertyModifyContext(
            final AuthenticationSession session,
            final InteractionInvocationMethod invocationMethod,
            final NakedObject target,
            final Identifier id,
            final NakedObject proposed) {
        super(InteractionContextType.PROPERTY_MODIFY, session, invocationMethod, id, target);

        this.proposed = proposed;
    }

    /**
     * The (proposed) new value for a property.
     */
    public NakedObject getProposed() {
        return proposed;
    }

    @Override
    public PropertyModifyEvent createInteractionEvent() {
        return new PropertyModifyEvent(unwrap(getTarget()), getIdentifier(), unwrap(getProposed()));
    }

}
