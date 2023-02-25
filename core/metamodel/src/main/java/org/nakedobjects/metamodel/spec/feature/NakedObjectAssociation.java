package org.nakedobjects.metamodel.spec.feature;

import org.nakedobjects.metamodel.adapter.NakedObject;


/**
 * Provides reflective access to a field on a domain object.
 */
public interface NakedObjectAssociation extends NakedObjectMember, CurrentHolder {

    /**
     * Get the name for the business key, if one has been specified.
     */
    String getBusinessKeyName();

    /**
     * Return the default for this property.
     */
    NakedObject getDefault(NakedObject nakedObject);

    /**
     * Set the property to it default references/values.
     */
    public void toDefault(NakedObject target);

    /**
     * Returns a list of possible references/values for this field, which the user can choose from.
     */
    public NakedObject[] getChoices(NakedObject object);

    /**
     * Returns true if calculated from other data in the object, that is, should not be persisted.
     */
    boolean isDerived();

    /**
     * Returns <code>true</code> if this field on the specified object is deemed to be empty, or has no
     * content.
     */
    boolean isEmpty(NakedObject target);

    /**
     * Determines if this field must be complete before the object is in a valid state
     */
    boolean isMandatory();

    /**
     * Whether there are any choices provided (eg <tt>choicesXxx</tt> supporting method) for the association.
     */
    public boolean hasChoices();

}
// Copyright (c) Naked Objects Group Ltd.
