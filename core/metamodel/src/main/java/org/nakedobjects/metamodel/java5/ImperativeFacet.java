package org.nakedobjects.metamodel.java5;

import java.lang.reflect.Method;
import java.util.List;

import org.nakedobjects.applib.DomainObjectContainer;
import org.nakedobjects.metamodel.commons.filters.AbstractFilter;
import org.nakedobjects.metamodel.commons.filters.Filter;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectMember;


/**
 * A {@link Facet} implementation that ultimately wraps a {@link Method} or possibly several equivalent methods, 
 * for a Java implementation of a {@link NakedObjectMember}.
 * 
 * <p>
 * Used by <tt>JavaSpecification#getMember(Method)</tt> in order to reverse lookup {@link NakedObjectMember}s
 * from underlying {@link Method}s. So, for example, the facets that represents an action xxx, or an
 * <tt>validateXxx</tt> method, or an <tt>addToXxx</tt> collection, can all be used to lookup the member.
 * 
 * <p>
 * Note that {@link Facet}s relating to the class itself (ie for {@link NakedObjectSpecification}) should not
 * implement this interface.
 */
public interface ImperativeFacet {

    /**
     * The {@link Method}s invoked by this {@link Facet}.
     * 
     * <p>
     * In the vast majority of cases there is only a single {@link Method} (eg wrapping a property's getter).  However,
     * some {@link Facet}s, such as those for callbacks, could map to multiple {@link Method}s.
     * Implementations that will return multiple {@link Method}s should implement the
     * {@link ImperativeFacetMulti} sub-interface that provides the ability to
     * {@link ImperativeFacetMulti#addMethod(Method) add} {@link Method}s as part of the interface API.  For example:
     * <pre>
     * if (someFacet instanceof ImperativeFacetMulti) {
     *     ImperativeFacetMulti ifm = (ImperativeFacetMulti)someFacet;
     *     ifm.addMethod(...);
     * }
     * </pre> 
     */
    public List<Method> getMethods();

    /**
     * For use by {@link FacetHolder#getFacets(org.nakedobjects.metamodel.facets.org.nakedobjects.nof.arch.facets.Facet.Filter)}
     */
    public static Filter<Facet> FILTER = new AbstractFilter<Facet>() {
        @Override
        public boolean accept(final Facet facet) {
            return ImperativeFacetUtils.isImperativeFacet(facet);
        }
    };

    /**
     * Whether invoking this requires a {@link DomainObjectContainer#resolve(Object)} to occur first. 
     */
	public boolean impliesResolve();

	/**
	 * Whether invoking this method requires an {@link DomainObjectContainer#objectChanged(Object)}
	 * to occur afterwards.
	 * @return
	 */
	public boolean impliesObjectChanged();

}
