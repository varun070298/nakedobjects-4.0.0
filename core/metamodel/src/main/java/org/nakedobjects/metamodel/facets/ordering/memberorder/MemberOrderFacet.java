package org.nakedobjects.metamodel.facets.ordering.memberorder;

import org.nakedobjects.metamodel.facets.MultipleValueFacet;
import org.nakedobjects.metamodel.facets.ordering.actionorder.ActionOrderFacet;
import org.nakedobjects.metamodel.facets.ordering.fieldorder.FieldOrderFacet;


/**
 * The preferred mechanism for determining the order in which the members of the object should be rendered.
 * 
 * <p>
 * In the standard Naked Objects Programming Model, corresponds to annotating each of the member methods with
 * the <tt>@MemberOrder</tt>. An alternative appraoch is to use the {@link ActionOrderFacet actionOrder} or
 * {@link FieldOrderFacet field order}.
 * 
 * @see MemberOrderFacet
 * @see FieldOrderFacet
 */
public interface MemberOrderFacet extends MultipleValueFacet {

    /**
     * To group members.
     */
    public String name();

    /**
     * The sequence, in dewey-decimal notation.
     */
    public String sequence();

}
