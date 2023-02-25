package org.nakedobjects.metamodel.facets;

import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;


public abstract class SingleClassValueFacetAbstract extends FacetAbstract implements SingleClassValueFacet {

    private final Class<?> value;
    private final SpecificationLoader specificationLoader;

    public SingleClassValueFacetAbstract(
            final Class<? extends Facet> facetType,
            final FacetHolder holder,
            final Class<?> value,
            final SpecificationLoader specificationLoader) {
        super(facetType, holder, false);
        this.value = value;
        this.specificationLoader = specificationLoader;
    }

    public Class<?> value() {
        return value;
    }

    /**
     * The {@link NakedObjectSpecification} of the {@link #value()}.
     */
    public NakedObjectSpecification valueSpec() {
        final Class<?> valueType = value();
        return valueType != null ? getSpecificationLoader().loadSpecification(valueType) : null;
    }

    private SpecificationLoader getSpecificationLoader() {
        return specificationLoader;
    }

}
