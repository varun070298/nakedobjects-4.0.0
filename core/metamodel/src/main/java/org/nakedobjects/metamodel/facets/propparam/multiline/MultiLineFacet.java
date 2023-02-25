package org.nakedobjects.metamodel.facets.propparam.multiline;

import org.nakedobjects.metamodel.facets.MultipleValueFacet;


/**
 * Whether the (string) property or parameter should be rendered over multiple lines.
 * 
 * <p>
 * In the standard Naked Objects Programming Model, corresponds to the <tt>@MultiLine</tt> annotation.
 */
public interface MultiLineFacet extends MultipleValueFacet {

    /**
     * How many lines to use.
     */
    public int numberOfLines();

    /**
     * Whether carriage returns should be used to split over multiple lines or not.
     * 
     * <p>
     * If set to <tt>true</tt>, then user must use carriage returns to split. If set to <tt>false</tt>, then
     * the viewer should automatically wrap when spills over the length of one line.
     */
    public boolean preventWrapping();

}
