package org.nakedobjects.metamodel.spec.feature;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.InteractionInvocationMethod;
import org.nakedobjects.metamodel.interactions.AccessContext;
import org.nakedobjects.metamodel.interactions.InteractionContext;
import org.nakedobjects.metamodel.interactions.PropertyAccessContext;
import org.nakedobjects.metamodel.interactions.ValidityContext;


/**
 * Provides reflective access to a field on a domain object that is used to reference another domain object.
 */
public interface OneToOneAssociation extends NakedObjectAssociation, OneToOneFeature, MutableCurrentHolder {

    /**
     * Initialise this field in the specified object with the specified reference - this call should only
     * affect the specified object, and not any related objects. It should also not be distributed. This is
     * strictly for re-initialising the object and not specifying an association, which is only done once.
     */
    void initAssociation(NakedObject inObject, NakedObject associate);

    /**
     * Creates an {@link InteractionContext} that represents access to this property.
     */
    public PropertyAccessContext createAccessInteractionContext(
            AuthenticationSession session,
            InteractionInvocationMethod interactionMethod,
            NakedObject targetNakedObject);

    /**
     * Creates an {@link InteractionContext} that represents validation of a proposed new value for the
     * property.
     * 
     * <p>
     * Typically it is easier to just call {@link #isAssociationValid(NakedObject, NakedObject)} or
     * {@link #isAssociationValidResult(NakedObject, NakedObject)}; this is provided as API for symmetry with
     * interactions (such as {@link AccessContext} accesses) have no corresponding vetoing methods.
     */
    public ValidityContext<?> createValidateInteractionContext(
            AuthenticationSession session,
            InteractionInvocationMethod interactionMethod,
            NakedObject targetNakedObject,
            NakedObject proposedValue);

    /**
     * Determines if the specified reference is valid for setting this field in the specified object,
     * represented as a {@link Consent}.
     */
    Consent isAssociationValid(NakedObject inObject, NakedObject associate);

    /**
     * Set up the association represented by this field in the specified object with the specified reference -
     * this call sets up the logical state of the object and might affect other objects that share this
     * association (such as back-links or bidirectional association). To initialise a recreated object to this
     * logical state the <code>initAssociation</code> method should be used on each of the objects.
     * 
     * @see #initAssociation(NakedObject, NakedObject)
     */
    void setAssociation(NakedObject inObject, NakedObject associate);

    /**
     * Clear this reference field (make it <code>null</code>) in the specified object, and remove any
     * association back-link.
     * 
     * @see #setAssociation(NakedObject, NakedObject)
     */
    void clearAssociation(NakedObject inObject);

}
// Copyright (c) Naked Objects Group Ltd.
