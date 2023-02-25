package org.nakedobjects.metamodel.facets.propparam.typicallength;

import org.nakedobjects.metamodel.facets.SingleIntValueFacet;


/**
 * The typical length of a property or a parameter.
 * 
 * <p>
 * Intended to be used by the viewer as a rendering hint to size the UI field to an appropriate size.
 * 
 * <p>
 * In the standard Naked Objects Programming Model, corresponds to the <tt>@TypicalLength</tt> annotation.
 */
public interface TypicalLengthFacet extends SingleIntValueFacet {

    public int value();

}
