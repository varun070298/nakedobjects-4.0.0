package org.nakedobjects.metamodel.consent;

import org.nakedobjects.metamodel.interactions.InteractionContext;

/**
 * Whether an {@link InteractionContext} was invoked by the user, or is programmatic.
 */
public enum InteractionInvocationMethod {

    BY_USER("By user"), PROGRAMMATIC("Programmatic");

    private final String description;

    private InteractionInvocationMethod(final String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
