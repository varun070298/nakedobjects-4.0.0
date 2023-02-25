package org.nakedobjects.metamodel.facets.properties.modify;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;


/**
 * Mechanism for clearing a property of an object (that is, setting it to <tt>null</tt>).
 * 
 * <p>
 * In the standard Naked Objects Programming Model, typically corresponds to a method named <tt>clearXxx</tt>
 * (for a property <tt>getXxx</tt>). As a fallback the standard model also supports invoking the
 * <tt>setXxx</tt> method with <tt>null</tt>.
 */
public interface PropertyClearFacet extends Facet {

    public void clearProperty(NakedObject inObject);
}
