package org.nakedobjects.metamodel.consent;



public interface Consent {

    /**
     * Returns true if this object is giving permission.
     */
    boolean isAllowed();

    /**
     * Returns true if this object is NOT giving permission.
     */
    boolean isVetoed();

    /**
     * Why consent is being vetoed.
     * 
     * <p>
     * Will be non-<tt>null</tt> and non-empty if vetoed.  Will be <tt>null</tt>
     * (<i>not</i> the empty string) if this is consent is is allowed.
     * 
     * <p>
     * Will correspond to the {@link InteractionResult#getReason() reason} in the contained
     * {@link #getInteractionResult() InteractionResult} (if one was specified).
     */
    String getReason();

    /**
     * Description of the interaction that this consent represents.
     * 
     * <p>
     * May be <tt>null</tt>.
     */
    String getDescription();
    

    /**
     * Allows the description of the interaction to which this consent relates to be
     * specified or refined.
     * 
     * @param description
     * @return this consent
     */
    Consent setDescription(String description);

    /**
     * The {@link InteractionResult} that created this {@link Consent}.
     * 
     * @return - may be <tt>null</tt> if created as a legacy {@link Consent}.
     * 
     */
    public InteractionResult getInteractionResult();

}
// Copyright (c) Naked Objects Group Ltd.
