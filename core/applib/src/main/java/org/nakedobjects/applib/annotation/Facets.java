package org.nakedobjects.applib.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Indicates that the class has additional facets, and specifies the how to obtain the <tt>FacetFactory</tt>
 * to manufacture them.
 * 
 * <p>
 * At least one named factory (as per {@link #facetFactoryNames()}) or one class factory (as per
 * {@link #facetFactoryClasses()}) should be specified.
 */
@Inherited
@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Facets {
    /**
     * Array of fully qualified names of classes each implementing
     * <tt>org.nakedobjects.metamodel.facets.FacetFactory</tt>.
     * 
     * <p>
     * Either the array provided by this method or by {@link #facetFactoryClasses()} should be non-empty.
     */
    String[] facetFactoryNames() default {};

    /**
     * Array of {@link Class}s, each indicating a class implementing
     * <tt>org.nakedobjects.metamodel.facets.FacetFactory</tt>.
     * 
     * <p>
     * Either the array provided by this method or by {@link #facetFactoryNames()} should be non-empty.
     */
    Class<?>[] facetFactoryClasses() default {};

}
