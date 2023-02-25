package org.nakedobjects.metamodel.facets.object.value;

import org.nakedobjects.metamodel.facets.MarkerFacet;
import org.nakedobjects.metamodel.facets.MultiTypedFacet;


/**
 * Indicates that this class has value semantics.
 * 
 * <p>
 * In the standard Naked Objects Programming Model, corresponds to the <tt>@Value</tt> annotation. However,
 * note that value semantics is just a convenient term for a number of mostly optional semantics all of which
 * are defined elsewhere.
 */
public interface ValueFacet extends MarkerFacet, MultiTypedFacet {

}
