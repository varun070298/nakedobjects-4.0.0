package org.nakedobjects.metamodel.facets;


import java.lang.reflect.Method;

import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public interface FacetFactory {

    /**
     * The {@link NakedObjectFeatureType feature type}s that this facet factory can create {@link Facet}s for.
     * 
     * <p>
     * Used by the Java5 Reflector's <tt>ProgrammingModel</tt> to reduce the number of {@link FacetFactory factory}s that are
     * queried when building up the meta-model.
     */
    NakedObjectFeatureType[] getFeatureTypes();

    /**
     * Process the class, and return the correctly setup annotation if present.
     * 
     * @param cls
     *            - class being processed
     * @param methodRemover
     *            - allow any methods of the class to be removed
     * @param holder
     *            - to attach the facets to
     * 
     * @return <tt>true</tt> if any facets were added, <tt>false</tt> otherwise.
     */
    boolean process(Class<?> cls, MethodRemover methodRemover, FacetHolder holder);

    /**
     * Process the method, and return the correctly setup annotation if present.
     * @param cls TODO
     * @param method
     *            - method representing the feature being processed (getter for property or collection, or
     *            action)
     * @param methodRemover
     *            - allow any methods of the class to be removed
     * @param holder
     *            - to attach the facets to
     * 
     * @return <tt>true</tt> if any facets were added and therefore should be removed, <tt>false</tt>
     *         otherwise. Returning true will cause the method to be removed
     */
    boolean process(Class<?> cls, Method method, MethodRemover methodRemover, FacetHolder holder);

    /**
     * Process the parameters of the method, and return the correctly setup annotation if present.
     * 
     * @param method
     *            - method representing the feature being processed (getter for property or collection, or
     *            action)
     * @param paramNum
     *            - 0-based index to the parameter to be processed.
     * @param holder
     *            - to attach the facets to
     * 
     * @return <tt>true</tt> if any facets were added, <tt>false</tt> otherwise.
     */
    boolean processParams(Method method, int paramNum, FacetHolder holder);

}
