package org.nakedobjects.metamodel.facets.object.ident.plural;

import org.nakedobjects.metamodel.facets.SingleStringValueFacet;
import org.nakedobjects.metamodel.facets.object.ident.icon.IconFacet;
import org.nakedobjects.metamodel.facets.object.ident.title.TitleFacet;


/**
 * Mechanism for obtaining the plural title of an instance of a class, used to label a collection of a certain
 * class.
 * 
 * <p>
 * In the standard Naked Objects Programming Model, typically corresponds to a method named
 * <tt>pluralName</tt>. If no plural name is provided, then the framework will attempt to guess the plural
 * name (by adding an <i>s</i> or <i>ies</i> suffix).
 * 
 * @see IconFacet
 * @see TitleFacet
 */
public interface PluralFacet extends SingleStringValueFacet {

}
