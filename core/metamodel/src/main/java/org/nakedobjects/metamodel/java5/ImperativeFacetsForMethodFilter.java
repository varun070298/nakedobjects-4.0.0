package org.nakedobjects.metamodel.java5;

import java.lang.reflect.Method;
import java.util.List;

import org.nakedobjects.metamodel.commons.filters.AbstractFilter;
import org.nakedobjects.metamodel.facets.Facet;

/**
 * Finds the {@link ImperativeFacet}(s) that correspond to the provided method. 
 */
public final class ImperativeFacetsForMethodFilter extends AbstractFilter<Facet> {
	private final Method method;

	ImperativeFacetsForMethodFilter(Method method) {
		this.method = method;
	}

	public boolean accept(Facet facet) {
		ImperativeFacet imperativeFacet = 
			ImperativeFacetUtils.getImperativeFacet(facet);
		if (imperativeFacet == null) {
			return false;
		}
		List<Method> methods = imperativeFacet.getMethods();
		for(Method method: methods) {
			// Method has value semantics
			if (method.equals(this.method)) {
				return true;
			}
		}
		return false;
	}
}