package org.nakedobjects.metamodel.facets.object.dirty;

import org.nakedobjects.metamodel.adapter.NakedObject;


/**
 * Explicitly indicate that this object is no longer dirty and so does not need to be persisted.
 * 
 * <p>
 * In the standard Naked Objects Programming Model, typically corresponds to method named <tt>markDirty</tt>.
 * 
 * <p>
 * TODO: this was introduced for Naked Objects 2.0, but not sure if they are still required for Naked Objects
 * 3.0.
 * 
 * @see IsDirtyObjectFacet
 * @see MarkDirtyObjectFacet
 */
public interface ClearDirtyObjectFacet extends DirtyObjectFacet {

    public void invoke(NakedObject object);

}
