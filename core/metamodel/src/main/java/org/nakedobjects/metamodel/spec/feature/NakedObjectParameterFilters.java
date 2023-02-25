package org.nakedobjects.metamodel.spec.feature;

import org.nakedobjects.metamodel.commons.filters.AbstractFilter;
import org.nakedobjects.metamodel.commons.filters.Filter;


public class NakedObjectParameterFilters {

    /**
     * Filters only parameters that are for objects (ie 1:1 associations)
     */
    public static final Filter<NakedObjectActionParameter> PARAMETER_ASSOCIATIONS = new AbstractFilter<NakedObjectActionParameter>() {
        @Override
        public boolean accept(final NakedObjectActionParameter parameter) {
            return parameter.getSpecification().isObject();
        }
    };

    private NakedObjectParameterFilters() {}

}
