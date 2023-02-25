package org.nakedobjects.metamodel.facets;


import java.lang.reflect.Method;
import java.util.List;

import org.nakedobjects.metamodel.spec.feature.OneToManyAssociation;
import org.nakedobjects.metamodel.spec.feature.OneToOneAssociation;


/**
 * A {@link FacetFactory} implementation that is able to identify a property or collection.
 * 
 * <p>
 * For example, a <i>getter</i> method is most commonly used to represent either a property (value or
 * reference) or a collection, with the return type indicating which.
 * 
 * <p>
 * Used by the Java5 Reflector's <tt>ProgrammingModel</tt> to determine which facet factories to ask whether a {@link Method}
 * represents a property or a collection.
 * 
 */
public interface PropertyOrCollectionIdentifyingFacetFactory extends FacetFactory {

    /**
     * Whether (this facet is able to determine that) the supplied {@link Method} possibly represents the
     * accessor of either a {@link ValueAssociation value property}, {@link OneToOneAssociation reference
     * property} or a {@link OneToManyAssociation collection}.
     * 
     * <p>
     * For example, if a method name has a prefix of <tt>get</tt> or alternatively has a prefix of <tt>is</tt>
     * and returns a <tt>boolean</tt>, then it would be a candidate.
     */
    public boolean isPropertyOrCollectionAccessorCandidate(Method method);

    /**
     * Whether (this facet is able to determine that) the supplied {@link Method} represents <i>either</i> a
     * {@link ValueAssociation value property} or a {@link OneToOneAssociation reference property}.
     */
    public boolean isPropertyAccessor(Method method);

    /**
     * Whether (this facet is able to determine that) the supplied {@link Method} represents a
     * {@link OneToManyAssociation collection}.
     */
    public boolean isCollectionAccessor(Method method);

    /**
     * Use the provided {@link MethodRemover} to remove all reference property accessors, and append them to
     * the supplied methodList.
     */
    public void findAndRemovePropertyAccessors(MethodRemover methodRemover, List<Method> methodListToAppendTo);

    /**
     * Use the provided {@link MethodRemover} to remove all collection accessors, and append them to the
     * supplied methodList.
     */
    public void findAndRemoveCollectionAccessors(MethodRemover methodRemover, List<Method> methodListToAppendTo);
}
