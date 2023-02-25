package org.nakedobjects.metamodel.facets.object.ident.title;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.object.ident.icon.IconFacet;
import org.nakedobjects.metamodel.facets.object.ident.plural.PluralFacet;


/**
 * Mechanism for obtaining the title of an instance of a class, used to label the instance in the viewer
 * (usually alongside an icon representation).
 * 
 * <p>
 * In the standard Naked Objects Programming Model, typically corresponds to a method named <tt>title</tt>.
 * 
 * @see IconFacet
 * @see PluralFacet
 */
public interface TitleFacet extends Facet {

    String title(final NakedObject object);
}
