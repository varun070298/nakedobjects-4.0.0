package org.nakedobjects.metamodel.java5;

import org.nakedobjects.metamodel.facets.FacetFactory;
import org.nakedobjects.metamodel.facets.PropertyOrCollectionIdentifyingFacetFactory;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;
import org.nakedobjects.metamodel.specloader.collectiontyperegistry.CollectionTypeRegistry;
import org.nakedobjects.metamodel.specloader.collectiontyperegistry.CollectionTypeRegistryAware;


public abstract class PropertyOrCollectionIdentifyingFacetFactoryAbstract extends MethodPrefixBasedFacetFactoryAbstract implements
        PropertyOrCollectionIdentifyingFacetFactory, CollectionTypeRegistryAware {

    private CollectionTypeRegistry collectionTypeRegistry;

    public PropertyOrCollectionIdentifyingFacetFactoryAbstract(
            final String[] prefixes,
            final NakedObjectFeatureType[] featureTypes) {
        super(prefixes, featureTypes);
    }

    protected boolean isCollectionOrArray(final Class<?> cls) {
        return getCollectionTypeRepository().isCollectionType(cls) || getCollectionTypeRepository().isArrayType(cls);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Injected: CollectionTypeRegistry
    // /////////////////////////////////////////////////////////////////////////

    protected CollectionTypeRegistry getCollectionTypeRepository() {
        return collectionTypeRegistry;
    }

    /**
     * Injected so can propogate to any {@link #registerFactory(FacetFactory) registered} {@link FacetFactory}
     * s that are also {@link CollectionTypeRegistryAware}.
     */
    public void setCollectionTypeRegistry(final CollectionTypeRegistry collectionTypeRegistry) {
        this.collectionTypeRegistry = collectionTypeRegistry;
    }

}

// Copyright (c) Naked Objects Group Ltd.
