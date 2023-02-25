package org.nakedobjects.plugins.headless.applib;

import org.nakedobjects.applib.ApplicationException;
import org.nakedobjects.applib.Identifier;
import org.nakedobjects.applib.events.InteractionEvent;


public abstract class InteractionException extends ApplicationException {

    private static final long serialVersionUID = 1L;

    private final InteractionEvent interactionEvent;

    public InteractionException(final InteractionEvent interactionEvent) {
        super(interactionEvent.getReason());
        this.interactionEvent = interactionEvent;
    }

    /**
     * The {@link InteractionEvent event} passed into the
     * {@link #InteractionException(InteractionEvent) constructor}.
     * 
     * <p>
     * Not part of the API, but made available so that subclasses can expose as the appropriate subtype of
     * {@link InteractionEvent}. This would have been more obvious to see if {@link InteractionException} was
     * generic, but generic subclasses of {@link Throwable} are (apparently) not allowed.
     * 
     * @return
     */
    protected InteractionEvent getInteractionEvent() {
        return interactionEvent;
    }

    /**
     * Convenience method that returns the {@link InteractionEvent#getAdvisorClass() advisor class} of the
     * wrapped {@link #getInteractionEvent() interaction event}.
     * 
     * @return
     */
    public Class<?> getAdvisorClass() {
        return interactionEvent.getAdvisorClass();
    }

    /**
     * Convenience method that returns the {@link InteractionEvent#getIdentifier() identifier} of the wrapped
     * {@link #getInteractionEvent() interaction event}.
     * 
     * @return
     */
    public Identifier getIdentifier() {
        return interactionEvent.getIdentifier();
    }

}
