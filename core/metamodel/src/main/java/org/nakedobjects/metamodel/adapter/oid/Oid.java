package org.nakedobjects.metamodel.adapter.oid;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.encoding.Encodable;

public interface Oid extends Encodable {
    
    /**
     * Copies the content of the specified oid into this oid.
     * 
     * <p>
     * After this call the {@link #hashCode()} return by both the specified object and this 
     * object will be the same, and both objects will be {@link #equals(Object) equal}.
     */
    void copyFrom(Oid oid);

    /**
     * Returns the pending oid if there is one.
     * 
     * @see #hasPrevious()
     * @see #clearPrevious()
     */
    Oid getPrevious();

    /**
     * Returns true if this oid contains a {@link #getPrevious() previous} value, specifically that the {@link Oid} was changed from
     * transient to persistent.
     * 
     * @see #getPrevious()
     * @see #clearPrevious()
     */
    boolean hasPrevious();
    
    
    /**
     * Indicate that the {@link #getPrevious() previous value} has been used to remap the {@link NakedObject adapter} 
     * and should not been cleared.
     * 
     * @see #getPrevious()
     * @see #hasPrevious()
     */
    void clearPrevious();

    /**
     * Flags whether this OID is temporary, and is for a transient object..
     */
    boolean isTransient();

    /**
     * Marks the Oid as persistent.
     */
    void makePersistent();
}
// Copyright (c) Naked Objects Group Ltd.
