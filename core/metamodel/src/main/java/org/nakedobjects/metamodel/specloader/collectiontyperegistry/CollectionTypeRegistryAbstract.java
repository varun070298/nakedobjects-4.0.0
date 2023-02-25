package org.nakedobjects.metamodel.specloader.collectiontyperegistry;




public abstract class CollectionTypeRegistryAbstract implements CollectionTypeRegistry {



    /**
     * Default implementation does nothing.
     */
	public void init() {
	}

    /**
     * Default implementation does nothing.
     */
	public void shutdown() {
	}

	
	
    public void injectInto(Object candidate) {
        if (CollectionTypeRegistryAware.class.isAssignableFrom(candidate.getClass())) {
            CollectionTypeRegistryAware cast = CollectionTypeRegistryAware.class.cast(candidate);
            cast.setCollectionTypeRegistry(this);
        }
    }

}

// Copyright (c) Naked Objects Group Ltd.
