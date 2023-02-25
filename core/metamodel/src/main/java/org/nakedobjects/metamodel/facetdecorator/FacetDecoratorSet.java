package org.nakedobjects.metamodel.facetdecorator;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.nakedobjects.metamodel.commons.component.ApplicationScopedComponent;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.exceptions.ReflectionException;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAction;

public class FacetDecoratorSet implements ApplicationScopedComponent {

	private final Map<Class<? extends Facet>, List<FacetDecorator>> facetDecoratorByFacetType = new HashMap<Class<? extends Facet>, List<FacetDecorator>>();
	private final Set<FacetDecorator> facetDecoratorSet = new LinkedHashSet<FacetDecorator>();

	// ////////////////////////////////////////////////////////////
	// init, shutdown
	// ////////////////////////////////////////////////////////////

	public void init() {
	}

	public void shutdown() {
	}

	// ////////////////////////////////////////////////////////////
	// add, get, isEmpty
	// ////////////////////////////////////////////////////////////

	public void add(final FacetDecorator decorator) {
		final Class<? extends Facet>[] decoratedFacetTypes = decorator
				.getFacetTypes();
		for (int i = 0; i < decoratedFacetTypes.length; i++) {
			Class<? extends Facet> decoratedFacetType = decoratedFacetTypes[i];
			getFacetDecoratorList(decoratedFacetType).add(decorator);
			facetDecoratorSet.add(decorator);
		}
	}

	private List<FacetDecorator> getFacetDecoratorList(
			Class<? extends Facet> decoratedFacetType) {
		List<FacetDecorator> facetDecoratorList = facetDecoratorByFacetType
				.get(decoratedFacetType);
		if (facetDecoratorList == null) {
			facetDecoratorList = new ArrayList<FacetDecorator>();
			facetDecoratorByFacetType.put(decoratedFacetType,
					facetDecoratorList);
		}
		return facetDecoratorList;
	}

	public void add(final List<FacetDecorator> decorators) {
		for (FacetDecorator decorator : decorators) {
			add(decorator);
		}
	}

	public Set<FacetDecorator> getFacetDecorators() {
		return Collections.unmodifiableSet(facetDecoratorSet);
	}

	public boolean isEmpty() {
		return facetDecoratorByFacetType.isEmpty();
	}

	// ////////////////////////////////////////////////////////////
	// decorate
	// ////////////////////////////////////////////////////////////

	public void decorateAllFacets(final FacetHolder holder) {
		if (isEmpty()) {
			return;
		}
		final Class<? extends Facet>[] facetTypes = holder.getFacetTypes();
		for (int i = 0; i < facetTypes.length; i++) {
			final Facet facet = holder.getFacet(facetTypes[i]);
			decorateFacet(facet, holder);
		}
	}

	/**
	 * REVIEW: the design is a little clumsy here.  We want to decorate the
	 * provided {@link Facet}, but its owning {@link FacetHolder holder} turns
	 * out to be a runtime peer (eg <tt>JavaAction</tt>) rather than the
	 * metamodel (eg {@link NakedObjectAction}). Since we want to decorate the
	 * {@link NakedObjectAction}, we have to pass it through.
	 */
	private void decorateFacet(final Facet facet, FacetHolder requiredHolder) {
		Class<? extends Facet> facetType = facet.facetType();
		final Class<? extends Facet> cls = facetType;
		final List<FacetDecorator> decoratorList = facetDecoratorByFacetType
				.get(cls);
		if (decoratorList == null) {
			return;
		}
		for (FacetDecorator facetDecorator : decoratorList) {
			Facet decoratingFacet = facetDecorator.decorate(facet, requiredHolder);
			if (decoratingFacet == null) {
				continue;
			}
			ensureDecoratorMetContract(facetDecorator, decoratingFacet,
					facetType, requiredHolder);
		}
	}

	private void ensureDecoratorMetContract(FacetDecorator facetDecorator,
			Facet decoratingFacet, Class<? extends Facet> facetType,
			FacetHolder originalFacetHolder) {
		if (decoratingFacet.facetType() != facetType) {
			throw new ReflectionException(
					MessageFormat
							.format(
									"Problem with facet decorator '{0}'; inconsistent decorating facetType() for {1}; was {2} but expectected facetType() of {3}",
									facetDecorator.getClass().getName(),
									decoratingFacet.getClass().getName(),
									decoratingFacet.facetType().getName(),
									facetType.getName()));
		}
		Facet facetForFacetType = originalFacetHolder.getFacet(decoratingFacet
				.facetType());
		if (facetForFacetType != decoratingFacet) {
			throw new ReflectionException(
					MessageFormat
							.format(
									"Problem with facet decorator '{0}'; has not replaced original facet for facetType() of {1}",
									facetDecorator.getClass().getName(),
									facetType.getName()));
		}
	}

	// ////////////////////////////////////////////////////////////
	// debugging
	// ////////////////////////////////////////////////////////////

	public void debugData(final DebugString str) {
		str.appendTitle("Facet decorators");
		str.indent();
		Set<Class<? extends Facet>> facetTypes = facetDecoratorByFacetType
				.keySet();
		if (facetTypes.size() == 0) {
			str.append("none");
		} else {
			for (final Class<? extends Facet> cls : facetTypes) {
				str.appendln(cls.getName(), facetDecoratorByFacetType.get(cls));
			}
		}
		str.unindent();
	}

}

// Copyright (c) Naked Objects Group Ltd.
