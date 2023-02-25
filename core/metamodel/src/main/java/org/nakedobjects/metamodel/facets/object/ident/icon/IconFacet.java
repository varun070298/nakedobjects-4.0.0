package org.nakedobjects.metamodel.facets.object.ident.icon;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.object.bounded.BoundedFacet;
import org.nakedobjects.metamodel.facets.object.ident.plural.PluralFacet;
import org.nakedobjects.metamodel.facets.object.ident.title.TitleFacet;


/**
 * Mechanism for obtaining the name of the icon for <i>this instance</i> of a class.
 * 
 * <p>
 * Typically a single icon is used for every instance of a class (for example, by placing an appropriately
 * named image file into a certain directory). This facet allows the icon to be changed on an
 * instance-by-instance basis. For example, the icon might be adapted with an overlay to represent its state
 * through some well-defined lifecycle (eg pending approval, approved, rejected). Alternatively a
 * {@link BoundedFacet bounded} class might have completely different icons for its instances (eg Visa,
 * Mastercard, Amex).
 * 
 * <p>
 * In the standard Naked Objects Programming Model, typically corresponds to a method named <tt>iconName</tt>.
 * 
 * @see TitleFacet
 * @see PluralFacet
 */
public interface IconFacet extends Facet {

    public String iconName(final NakedObject object);
}
