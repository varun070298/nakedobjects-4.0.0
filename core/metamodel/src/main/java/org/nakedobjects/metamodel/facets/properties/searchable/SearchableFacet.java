package org.nakedobjects.metamodel.facets.properties.searchable;

import org.nakedobjects.metamodel.facets.MultipleValueFacet;


/**
 * Indicates that this property should be used as part of a generic searching capability (for example, query
 * by example).
 * 
 * <p>
 * In the standard Naked Objects Programming Model, corresponds to annotating the property with the
 * <tt>@Searchable</tt> annotation.
 * 
 * <p>
 * TODO: not yet implemented by the framework or any viewer. Originally introduced for the
 * nakedrcp.sourceforge.net viewer as an extension point plug-in for the Search menu (
 * <tt>org.eclipse.search.searchPages</tt>).
 */
public interface SearchableFacet extends MultipleValueFacet {

    /**
     * The (class of the) repository to delegate to.
     */
    public Class<?> repository();

    /**
     * Whether this is a query by example search.
     */
    public boolean queryByExample();

}
