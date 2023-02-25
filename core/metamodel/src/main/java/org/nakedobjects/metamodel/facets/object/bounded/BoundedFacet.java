package org.nakedobjects.metamodel.facets.object.bounded;

import org.nakedobjects.metamodel.facets.MarkerFacet;
import org.nakedobjects.metamodel.interactions.DisablingInteractionAdvisor;
import org.nakedobjects.metamodel.interactions.ValidatingInteractionAdvisor;


/**
 * Whether the number of instances of this class is bounded.
 * 
 * <p>
 * Typically viewers will interpret this information by displaying all instances of the class in a drop-down
 * list box or similar widget.
 * 
 * <p>
 * In the standard Naked Objects Programming Model, corresponds to annotating the member with
 * <tt>@Bounded</tt>.
 */
public interface BoundedFacet extends MarkerFacet, DisablingInteractionAdvisor, ValidatingInteractionAdvisor {

}
