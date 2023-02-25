package org.nakedobjects.metamodel.consent;

import static org.hamcrest.CoreMatchers.is;
import static org.nakedobjects.metamodel.commons.matchers.NofMatchers.nonEmptyStringOrNull;

import java.io.Serializable;

import org.nakedobjects.metamodel.commons.ensure.Ensure;

public abstract class ConsentAbstract implements Serializable, Consent {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Factory method.
     * 
     * <p>
     * Used extensively by the DnD viewer. 
     */
    public static Consent allowIf(final boolean allowed) {
        return allowed ? Allow.DEFAULT : Veto.DEFAULT;
    }
    
    private final InteractionResult interactionResult;
    private final String reason;
    
    /**
     * Can be subsequently {@link #setDescription(String) modified}, but is only a
     * description of the event to which this consent applies and does not change
     * whether the Consent represents an allow or a veto.
     */
    private String description;

    private static String determineReason(final InteractionResult interactionResult) {
        if (interactionResult == null) {
            return null;
        }
        return interactionResult.getReason();
    }


    /**
     * 
     * @param interactionResult
     *            - if <tt>null</tt> then defaults to an {@link #isAllowed() allowing} {@link Consent}.
     */
    protected ConsentAbstract(final InteractionResult interactionResult) {
        this(interactionResult, null, determineReason(interactionResult));
    }

    /**
     * Enable legacy {@link Consent}s (not created using an {@link InteractionResult}) to 
     * create an {@link Consent}, specifying a {@link #getDescription() description} of the
     * event and the {@link #getReason() reason} (if any) that the consent is vetoed.
     *
     * @param description - a description of the event to which this consent relates
     * @param reason - if not <tt>null</tt> and not empty, is the reason this consent is vetoed.
     */
    protected ConsentAbstract(final String description, final String reason) {
        this(null, description, reason);
    }

    private ConsentAbstract(
            final InteractionResult interactionResult, 
            final String description, 
            final String reason) {
        this.interactionResult = interactionResult;
        this.description = description;
        Ensure.ensureThatArg(reason, is(nonEmptyStringOrNull()));
        this.reason = reason;
    }    

    /**
     * The reason why this has been vetoed.
     */
    public String getReason() {
        return isVetoed()? this.reason: null;
    }

    public Consent setDescription(final String description) {
        this.description = description;
        return this;
    }

    /**
     * Returns <tt>true</tt> if this object is giving permission
     * (if the {@link #getReason() reason} is <tt>null</tt> or empty.
     * 
     * @see #getReason()
     */
    public boolean isAllowed() {
        return this.reason == null || this.reason.equals("");
    }

    /**
     * Returns true if this object is NOT giving permission.
     * 
     * @see #isAllowed()
     */
    public boolean isVetoed() {
        return !isAllowed();
    }

    /**
     * Underlying {@link InteractionResult} that created this {@link Consent}
     * (may be <tt>null</tt>).
     * 
     */
    public InteractionResult getInteractionResult() {
        return interactionResult;
    }

    /**
     * Description of the action allowed by this event.
     * 
     * <p>
     * (Previously, {@link Allow} consents overloaded the {@link #getReason() reason} property with
     * a description of the event.  This has now been changed so that a non-<tt>null</tt> reason
     * always implies a {@link Veto}.  This property captures the description.
     * 
     * @return
     */
    public String getDescription() {
        return description;
    }
    
    @Override
    public String toString() {
        return (isVetoed() ? "VETOED" : "ALLOWED") + ", reason=" + reason;
    }

}
// Copyright (c) Naked Objects Group Ltd.
