package org.nakedobjects.metamodel.facets.object.cached;

import org.nakedobjects.metamodel.facets.MarkerFacet;


/**
 * Whether the instances of this class are cached.
 * 
 * <p>
 * In the standard Naked Objects Programming Model, corresponds to annotating the member with <tt>@Cached</tt>.
 * 
 * <p>
 * Cached does not necessarily imply immutable. The idea though is that the developer is indicating that the
 * performance cost of obtaining all instances of an instance is low; viewer implementations might be able to
 * exploit this information.
 * 
 * <p>
 * Not yet implemented by any viewer.
 */
public interface CachedFacet extends MarkerFacet {

}
