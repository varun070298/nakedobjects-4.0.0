package org.nakedobjects.metamodel.facets;

import org.nakedobjects.metamodel.spec.NamedAndDescribedFacetHolder;


/**
 * For base subclasses or, more likely, to help write tests.
 */
public class NamedAndDescribedFacetHolderImpl extends FacetHolderImpl implements NamedAndDescribedFacetHolder {

    private final String name;
    private final String description;

    public NamedAndDescribedFacetHolderImpl(final String name) {
        this(name, null);
    }

    public NamedAndDescribedFacetHolderImpl(final String name, final String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

}
