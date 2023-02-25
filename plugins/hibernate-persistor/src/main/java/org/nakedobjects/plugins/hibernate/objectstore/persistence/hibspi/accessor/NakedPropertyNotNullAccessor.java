package org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.accessor;

import org.hibernate.PropertyNotFoundException;
import org.hibernate.property.Getter;


/**
 * Access properties of a naked object, where the value persisted cannot be null.
 */
public class NakedPropertyNotNullAccessor extends NakedPropertyAccessorAbstract {

    @SuppressWarnings("unchecked")
    public Getter getGetter(final Class theClass, final String propertyName) throws PropertyNotFoundException {
        return getGetter(theClass, propertyName, false);
    }

}
// Copyright (c) Naked Objects Group Ltd.
