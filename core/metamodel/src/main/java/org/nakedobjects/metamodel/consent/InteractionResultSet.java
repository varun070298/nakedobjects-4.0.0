package org.nakedobjects.metamodel.consent;

import java.util.ArrayList;
import java.util.List;



public class InteractionResultSet {

    private final List<InteractionResult> results = new ArrayList<InteractionResult>();
    private InteractionResult firstResult = null;

    public InteractionResultSet() {}

    public InteractionResultSet add(final InteractionResult result) {
        if (firstResult == null) {
            firstResult = result;
        }
        this.results.add(result);
        return this;
    }

    /**
     * Empty only if all the {@link #add(InteractionResult) contained} {@link InteractionResult}s are also
     * {@link InteractionResult#isNotVetoing() empty}.
     */
    public boolean isAllowed() {
        return !isVetoed();
    }

    /**
     * Vetoed if any of the {@link #add(InteractionResult) contained} {@link InteractionResult}s are also
     * {@link InteractionResult#isVetoing() not empty}.
     * 
     * @return
     */
    public boolean isVetoed() {
        for (final InteractionResult result : results) {
            if (result.isVetoing()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the {@link Consent} corresponding to {@link #getInteractionResult()}, or an {@link Allow} if
     * there have been no {@link InteractionResult}s {@link #add(InteractionResult) added}.
     * 
     * @return
     */
    public Consent createConsent() {
        final InteractionResult interactionResult = getInteractionResult();
        if (interactionResult == null) {
            return Allow.DEFAULT;
        }
        return interactionResult.createConsent();
    }

    /**
     * Returns the &quot;best&quot; contained {@link InteractionResult}.
     * 
     * <p>
     * This will be the first {@link InteractionResult} that has vetoed the interaction, or the first
     * {@link InteractionResult} {@link #add(InteractionResult) added} if none have vetoed.
     * 
     * @return
     */
    public InteractionResult getInteractionResult() {
        for (final InteractionResult result : results) {
            if (!result.isNotVetoing()) {
                return result;
            }
        }
        return firstResult != null ? firstResult : null;
    }

    @Override
    public String toString() {
    	return super.toString();
    }
}

// Copyright (c) Naked Objects Group Ltd.
