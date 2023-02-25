package org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.accessor;

/**
 * Convert properties to and from a value holder.
 * <p>
 * The naming convention used for classes is &lt;persistent format&gt;&lt;naked object format&gt;Converter, eg
 * IntegerToWholeNumberConverter converts an Integer from the database to a WholeNumber.
 */
public interface PropertyConverter {

    /**
     * Set the value in the valueHolder, converting from the persistent format.
     */
    public void setValue(final Object valueHolder, final Object value);

    /**
     * Return the value in the valueHolder, converting to the format used to persist.
     */
    public Object getPersistentValue(final Object valueHolder, final boolean isNullable);

    /**
     * Return the java type of persistent objects
     */
    public Class<?> getPersistentType();

    /**
     * Return the Hibernate type of persistent objects
     */
    public String getHibernateType();
}
// Copyright (c) Naked Objects Group Ltd.
