package org.nakedobjects.metamodel.facets.object.dirty;

import org.nakedobjects.metamodel.adapter.NakedObject;


/**
 * Determine whether this object has been explicitly {@link MarkDirtyObjectFacet marked as dirty}.
 * 
 * <p>
 * In the standard Naked Objects Programming Model, typically corresponds to method named <tt>isDirty</tt>.
 * 
 * <p>
 * TODO: this was introduced for Naked Objects 2.0, but not sure if they are still required for Naked Objects
 * 3.0.
 * 
 * @see ClearDirtyObjectFacet
 * @see MarkDirtyObjectFacet
 */
public interface IsDirtyObjectFacet extends DirtyObjectFacet {

    public boolean invoke(NakedObject object);

}
