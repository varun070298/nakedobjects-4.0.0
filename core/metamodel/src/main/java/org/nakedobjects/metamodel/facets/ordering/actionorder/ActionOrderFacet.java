package org.nakedobjects.metamodel.facets.ordering.actionorder;

import org.nakedobjects.metamodel.facets.SingleStringValueFacet;
import org.nakedobjects.metamodel.facets.ordering.fieldorder.FieldOrderFacet;
import org.nakedobjects.metamodel.facets.ordering.memberorder.MemberOrderFacet;


/**
 * (One of the) mechanism(s) for determining the order in which the actions of the object should be rendered.
 * 
 * <p>
 * In the standard Naked Objects Programming Model, typically corresponds to the <tt>actionOrder</tt> method
 * which returns a comma-separated list of action names. An alternative (and preferred, because it is
 * refactoring-safe) mechanism is to annotate each of the methods using <tt>@MemberOrder</tt>.
 * 
 * @see MemberOrderFacet
 * @see FieldOrderFacet
 */
public interface ActionOrderFacet extends SingleStringValueFacet {

}
