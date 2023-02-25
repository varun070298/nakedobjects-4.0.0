package org.nakedobjects.applib;

public abstract class AbstractFactoryAndRepository extends AbstractService {

    // {{ newPersistenceInstance, newInstance
    /**
     * Returns a new instance of the specified class that will have been persisted.
     * 
     * <p>
     * This method isn't quite deprecated, but generally consider using
     * {@link #newTransientInstance(Class)} instead.
     */
    protected <T> T newPersistentInstance(final Class<T> ofClass) {
        return getContainer().newPersistentInstance(ofClass);
    }
    /**
     * Returns a new instance of the specified class that has the sane persisted state as the specified object.
     * 
     * <p>
     * This method isn't quite deprecated, but generally consider using
     * {@link #newTransientInstance(Class)} instead.
     */
    protected <T> T newInstance(final Class<T> ofClass, final Object object) {
    	return getContainer().newInstance(ofClass, object);
    }
    // }}
    

	
}

// Copyright (c) Naked Objects Group Ltd.
