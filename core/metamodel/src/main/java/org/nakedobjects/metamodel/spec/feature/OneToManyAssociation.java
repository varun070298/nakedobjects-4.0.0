package org.nakedobjects.metamodel.spec.feature;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.InteractionInvocationMethod;
import org.nakedobjects.metamodel.interactions.AccessContext;
import org.nakedobjects.metamodel.interactions.InteractionContext;
import org.nakedobjects.metamodel.interactions.ValidityContext;


public interface OneToManyAssociation extends NakedObjectAssociation, OneToManyFeature {

    // /////////////////////////////////////////////////////////////
    // add
    // /////////////////////////////////////////////////////////////

    /**
     * Creates an {@link InteractionContext} that represents validation of a candidate object to be added to
     * the collection.
     * 
     * <p>
     * Typically it is easier to just call {@link #isValidToAdd(NakedObject, NakedObject)} or
     * {@link #isValidToAddResult(NakedObject, NakedObject)}; this is provided as API for symmetry with
     * interactions (such as {@link AccessContext} accesses) have no corresponding vetoing methods.
     */
    public ValidityContext<?> createValidateAddInteractionContext(
            AuthenticationSession session,
            InteractionInvocationMethod invocationMethod,
            NakedObject owningNakedObject,
            NakedObject proposedObjectToAdd);

    /**
     * Determines if the specified element can be added to the collection field, represented as a
     * {@link Consent}.
     * 
     * <p>
     * If allowed the {@link #addElement(NakedObject, NakedObject) add} method can be called with the same
     * parameters, .
     * 
     * @see #isValidToAddResult(NakedObject, NakedObject)
     */
    Consent isValidToAdd(NakedObject owningNakedObject, NakedObject proposedObjectToAdd);

    /**
     * Add the specified element to this collection field in the specified object.
     */
    void addElement(NakedObject owningNakedObject, NakedObject objectToAdd);

    // /////////////////////////////////////////////////////////////
    // remove
    // /////////////////////////////////////////////////////////////

    /**
     * Creates an {@link InteractionContext} that represents validation of a candidate object to be removed
     * from the collection.
     * 
     * <p>
     * Typically it is easier to just call {@link #isValidToAdd(NakedObject, NakedObject)} or
     * {@link #isValidToAddResult(NakedObject, NakedObject)}; this is provided as API for symmetry with
     * interactions (such as {@link AccessContext} accesses) have no corresponding vetoing methods.
     */
    ValidityContext<?> createValidateRemoveInteractionContext(
            AuthenticationSession session,
            InteractionInvocationMethod invocationMethod,
            NakedObject owningNakedObject,
            NakedObject proposedObjectToRemove);

    /**
     * Determines if the specified element can be removed from the collection field, represented as a
     * {@link Consent}.
     * 
     * <p>
     * If allowed the {@link #removeElement(NakedObject, NakedObject) remove} method can be called with the
     * same parameters, .
     * 
     * @see #removeElement(NakedObject, NakedObject)
     * @see #isValidToAddResult(NakedObject, NakedObject)
     */
    Consent isValidToRemove(NakedObject owningNakedObject, NakedObject proposedObjectToRemove);

    /**
     * Remove the specified element from this collection field in the specified object.
     */
    void removeElement(NakedObject owningNakedObject, NakedObject oObjectToRemove);

    // /////////////////////////////////////////////////////////////
    // clear
    // /////////////////////////////////////////////////////////////

    /**
     * Remove all elements from this collection field in the specified object.
     */
    void clearCollection(NakedObject inObject);

}
// Copyright (c) Naked Objects Group Ltd.
