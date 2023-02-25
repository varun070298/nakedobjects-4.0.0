package org.nakedobjects.metamodel.facets.properties.choices;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;


/**
 * Provides a set of choices for a property.
 * 
 * <p>
 * Viewers would typically represent this as a drop-down list box for the property.
 * 
 * <p>
 * In the standard Naked Objects Programming Model, corresponds to the <tt>choicesXxx</tt> supporting method
 * for the property with accessor <tt>getXxx</tt>.
 * 
 * <p>
 * Note: an alternative mechanism may be to use the <tt>@Bounded</tt> annotation against the referenced class.
 */
public interface PropertyChoicesFacet extends Facet {

    /**
     * Gets the available choices for this property.
     */
    public Object[] getChoices(NakedObject adapter);
}
