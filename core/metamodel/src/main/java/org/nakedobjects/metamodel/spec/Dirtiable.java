package org.nakedobjects.metamodel.spec;

import org.nakedobjects.metamodel.adapter.NakedObject;


public interface Dirtiable {

    /**
     * Clear the dirty flag so that a call to <tt>isDirty()</tt>, and before <tt>markDirty()</tt> is called,
     * will return false;
     * 
     * @see #isDirty(NakedObject)
     * @see #markDirty(NakedObject)
     */
    void clearDirty(NakedObject object);

    /**
     * Checks if the specified object has been changed, and hence needs persisting.
     * 
     * @see #markDirty(NakedObject)
     * @see #clearDirty(NakedObject)
     */
    boolean isDirty(NakedObject object);

    /**
     * Mark the specified object as having been changed, and hence needing persisting.
     * 
     * @see #isDirty(NakedObject)
     * @see #clearDirty(NakedObject)
     */
    void markDirty(NakedObject object);

}
