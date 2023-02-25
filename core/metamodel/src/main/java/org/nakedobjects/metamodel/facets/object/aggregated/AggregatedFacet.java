package org.nakedobjects.metamodel.facets.object.aggregated;

import org.nakedobjects.metamodel.facets.MarkerFacet;
import org.nakedobjects.metamodel.facets.object.immutable.ImmutableFacet;


/**
 * Indicates that this class is aggregated, that is, wholly contained within a larger object.
 * 
 * <p>
 * The object may or may not be {@link ImmutableFacet immutable}, and may reference regular entity domain
 * objects or other aggregated objects.
 * 
 * <p>
 * In the standard Naked Objects Programming Model, typically corresponds to applying the <tt>@Aggregated</tt>
 * annotation at the class level.
 * 
 * <p>
 * In terms of an analogy, aggregated is similar to Hibernate's component types (for larger mutable in-line
 * objects) or to Hibernate's user-defined types (for smaller immutable values).
 * 
 * <p>
 * TODO: should also be able to apply to associations, indicating that the reference is aggregating.
 */
public interface AggregatedFacet extends MarkerFacet {

}
