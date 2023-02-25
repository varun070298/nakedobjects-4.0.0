package org.nakedobjects.metamodel.facets.object.immutable;

import org.nakedobjects.metamodel.facets.SingleWhenValueFacet;
import org.nakedobjects.metamodel.facets.object.ebc.EqualByContentFacet;
import org.nakedobjects.metamodel.facets.object.value.ValueFacet;
import org.nakedobjects.metamodel.interactions.DisablingInteractionAdvisor;


/**
 * Indicates that the instances of this class are immutable and so may not be modified either through the
 * viewer or indeed programmatically.
 * 
 * <p>
 * In the standard Naked Objects Programming Model, typically corresponds to applying the <tt>@Immutable</tt>
 * annotation at the class level.
 * 
 * @see EqualByContentFacet
 * @see ValueFacet
 */
public interface ImmutableFacet extends SingleWhenValueFacet, DisablingInteractionAdvisor {

}
