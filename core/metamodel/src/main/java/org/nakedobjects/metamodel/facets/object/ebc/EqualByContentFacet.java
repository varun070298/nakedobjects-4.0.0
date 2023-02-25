package org.nakedobjects.metamodel.facets.object.ebc;

import org.nakedobjects.metamodel.facets.MarkerFacet;
import org.nakedobjects.metamodel.facets.object.immutable.ImmutableFacet;
import org.nakedobjects.metamodel.facets.object.value.ValueFacet;


/**
 * Indicates that the instances of this class are equal-by-content.
 * 
 * <p>
 * In the standard Naked Objects Programming Model, typically corresponds to applying the
 * <tt>@EqualByContent</tt> annotation at the class level.
 * 
 * @see ImmutableFacet
 * @see ValueFacet
 */
public interface EqualByContentFacet extends MarkerFacet {

}
