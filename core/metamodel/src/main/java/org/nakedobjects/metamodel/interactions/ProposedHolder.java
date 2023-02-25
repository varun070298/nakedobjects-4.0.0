package org.nakedobjects.metamodel.interactions;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;


/**
 * Implemented by some of the {@link InteractionContext} subclasses, making it easier for facets to process
 * multiple at the same time.
 * 
 * <p>
 * Where this is used most often is for {@link PropertyModifyContext} and {@link ActionArgumentContext}, where
 * a {@link Facet} (such as <tt>MandatoryFacet</tt> or <tt>MaxLengthFacet</tt>) would want to perform the same
 * checks on the {@link #getProposed() proposed} value/argument.
 */
public interface ProposedHolder {

    NakedObject getProposed();
}
