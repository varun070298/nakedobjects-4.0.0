package org.nakedobjects.metamodel.interactions;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.applib.events.InteractionEvent;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.consent.InteractionContextType;
import org.nakedobjects.metamodel.consent.InteractionInvocationMethod;
import org.nakedobjects.metamodel.facets.Facet;


/**
 * Represents an interaction between the framework and (a {@link Facet} of) the domain object.
 * 
 * <p>
 * There are two main responsibilities:
 * <ul>
 * <li>Wraps up a target object, parameters and a {@link AuthenticationSession}. Defining this as a separate interface makes
 * for a more stable API</li>
 * <li>Acts as a factory for the corresponding {@link InteractionEvent} (more on this below).</li>
 * </ul>
 * 
 * <p>
 * The {@link InteractionContext} hierarchy is parallel to the {@link InteractionEvent} hierarchy. Having
 * parallel hierarchies is a bit of a code-smell. However, it is required because the
 * {@link InteractionContext context} hierarchy is internal to the framework (with references to
 * {@link NakedObject}s, {@link AuthenticationSession}s and so forth), whereas the {@link InteractionEvent event} hierarchy
 * is part of the corelib, that is public API.
 * 
 * <p>
 * The class is genericized so that the {@link #createInteractionEvent() factory method} can return the
 * correct subclass without having to downcast.
 */
public abstract class InteractionContext<T extends InteractionEvent> {

    private final InteractionContextType interactionType;
    private final Identifier identifier;
    private final InteractionInvocationMethod invocation;
    private final AuthenticationSession session;
    private final NakedObject target;

    public InteractionContext(
            final InteractionContextType interactionType,
            final AuthenticationSession session,
            final InteractionInvocationMethod invocationMethod,
            final Identifier identifier,
            final NakedObject target) {
        this.interactionType = interactionType;
        this.invocation = invocationMethod;
        this.identifier = identifier;
        this.session = session;
        this.target = target;
    }

    /**
     * The type of interaction.
     * 
     * <p>
     * Available for use by {@link Facet}s that apply only in certain conditions. For example, some facets for
     * collections will care only when an object is being added to the collection, but won't care when an
     * object is being removed from the collection.
     * 
     * <p>
     * Alternatively, {@link Facet}s can use <tt>instanceof</tt>.
     */
    public InteractionContextType getInteractionType() {
        return interactionType;
    }

    /**
     * The identifier of the object or member that is being identified with.
     * 
     * <p>
     * If the {@link #getInteractionType() type} is {@link InteractionContextType#OBJECT_VALIDATE}, will be the
     * identifier of the {@link #getTarget() target} object's specification. Otherwise will be the identifier
     * of the member.
     */
    public Identifier getIdentifier() {
        return identifier;
    }

    /**
     * The {@link AuthenticationSession user or role} that is performing this interaction.
     */
    public AuthenticationSession getSession() {
        return session;
    }

    /**
     * How the interaction was initiated.
     */
    public InteractionInvocationMethod getInvocationMethod() {
        return invocation;
    }

    /**
     * Convenience method that indicates whether the {@link #getInvocationMethod() interaction was invoked}
     * programmatically.
     */
    public boolean isProgrammatic() {
        return invocation == InteractionInvocationMethod.PROGRAMMATIC;
    }

    /**
     * The target object that this interaction is with.
     */
    public NakedObject getTarget() {
        return target;
    }

    /**
     * Factory method to create corresponding {@link InteractionEvent}.
     * 
     * @return
     */
    public abstract T createInteractionEvent();

}

// Copyright (c) Naked Objects Group Ltd.
