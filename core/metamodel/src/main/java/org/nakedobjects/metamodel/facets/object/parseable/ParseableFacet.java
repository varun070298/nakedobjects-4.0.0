package org.nakedobjects.metamodel.facets.object.parseable;

import org.nakedobjects.metamodel.adapter.InvalidEntryException;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.TextEntryParseException;
import org.nakedobjects.metamodel.facets.MultipleValueFacet;
import org.nakedobjects.metamodel.facets.object.ident.title.TitleFacet;


/**
 * Indicates that this class can parse an entry string.
 */
public interface ParseableFacet extends MultipleValueFacet {

    /**
     * Parses a text entry made by a user and sets the domain object's value.
     * 
     * <p>
     * Equivalent to <tt>Parser#parseTextEntry(Object, String)</tt>, though may be implemented through some
     * other mechanism.
     * 
     * @throws InvalidEntryException
     * @throws TextEntryParseException
     */
    NakedObject parseTextEntry(NakedObject original, String text);

    /**
     * A title for the object that is valid but which may be easier to edit than the title provided by a
     * {@link TitleFacet}.
     * 
     * <p>
     * The idea here is that the viewer can display a parseable title for an existing object when, for
     * example, the user initially clicks in the field. So, a date might be rendered via a {@link TitleFacet}
     * as <tt>May 2, 2007</tt>, but its parseable form might be <tt>20070502</tt>.
     */
    public String parseableTitle(NakedObject obj);
}
