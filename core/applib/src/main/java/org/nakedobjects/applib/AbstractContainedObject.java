package org.nakedobjects.applib;

import java.util.List;

import org.nakedobjects.applib.annotation.Hidden;
import org.nakedobjects.applib.query.Query;
import org.nakedobjects.applib.security.UserMemento;


/**
 * Convenience super class for all classes that wish to interact with the container.
 * 
 * @see org.nakedobjects.applib.DomainObjectContainer
 */
public abstract class AbstractContainedObject {


	// {{ factory methods
    @Hidden
    protected <T> T newTransientInstance(final Class<T> ofType) {
        return getContainer().newTransientInstance(ofType);
    }
    // }}

	// {{ allInstances, allMatches
	/**
	 * Convenience method that delegates to {@link DomainObjectContainer}.
	 * 
	 * @see DomainObjectContainer#allInstances(Class)
	 */
    protected <T> List<T> allInstances(final Class<T> ofType) {
        return getContainer().allInstances(ofType);
    }
	/**
	 * Convenience method that delegates to {@link DomainObjectContainer}.
	 * 
	 * @see DomainObjectContainer#allMatches(Class, Filter)
	 */
	protected <T> List<T> allMatches(final Class<T> ofType, final Filter<T> filter) {
		return getContainer().allMatches(ofType, filter);
	}

	/**
	 * Convenience method that delegates to {@link DomainObjectContainer}.
	 * 
	 * @see DomainObjectContainer#allMatches(Class, Object)
	 */
	protected <T> List<T> allMatches(final Class<T> ofType, final T pattern) {
		return getContainer().allMatches(ofType, pattern);
	}

	/**
	 * Convenience method that delegates to {@link DomainObjectContainer}.
	 * 
	 * @see DomainObjectContainer#allMatches(Class, String)
	 */
	protected <T> List<T> allMatches(final Class<T> ofType, final String title) {
		return getContainer().allMatches(ofType, title);
	}

	/**
	 * Convenience method that delegates to {@link DomainObjectContainer}.
	 * 
	 * @see DomainObjectContainer#allMatches(Query)
	 */
	protected <T> List<T> allMatches(final Query<T> query) {
		return getContainer().allMatches(query);
	}
	// }}

	// {{ firstMatch
	/**
	 * Convenience method that delegates to {@link DomainObjectContainer}.
	 * 
	 * @see DomainObjectContainer#firstMatch(Class, Filter)
	 */
	protected <T> T firstMatch(final Class<T> ofType, final Filter<T> filter) {
		return getContainer().firstMatch(ofType, filter);
	}

	/**
	 * Convenience method that delegates to {@link DomainObjectContainer}.
	 * 
	 * @see DomainObjectContainer#firstMatch(Class, Object)
	 */
	protected <T> T firstMatch(final Class<T> ofType, final T pattern) {
		return getContainer().firstMatch(ofType, pattern);
	}

	/**
	 * Convenience method that delegates to {@link DomainObjectContainer}.
	 * 
	 * @see DomainObjectContainer#firstMatch(Class, String)
	 */
	protected <T> T firstMatch(final Class<T> ofType, final String title) {
		return getContainer().firstMatch(ofType, title);
	}

	/**
	 * Convenience method that delegates to {@link DomainObjectContainer}.
	 * 
	 * @see DomainObjectContainer#firstMatch(Query)
	 */
	protected <T> T firstMatch(final Query<T> query) {
		return getContainer().firstMatch(query);
	}
	// }}

	
	// {{ uniqueMatch
	/**
	 * Convenience method that delegates to {@link DomainObjectContainer}.
	 * 
	 * @see DomainObjectContainer#uniqueMatch(Class, Filter)
	 */
	protected <T> T uniqueMatch(final Class<T> ofType, final Filter<T> filter) {
		return getContainer().uniqueMatch(ofType, filter);
	}

	/**
	 * Convenience method that delegates to {@link DomainObjectContainer}.
	 * 
	 * @see DomainObjectContainer#uniqueMatch(Class, String)
	 */
	protected <T> T uniqueMatch(final Class<T> ofType, final String title) {
		return getContainer().uniqueMatch(ofType, title);
	}

	/**
	 * Convenience method that delegates to {@link DomainObjectContainer}.
	 * 
	 * @see DomainObjectContainer#uniqueMatch(Class, Object)
	 */
	protected <T> T uniqueMatch(final Class<T> ofType, final T pattern) {
		return getContainer().uniqueMatch(ofType, pattern);
	}

	/**
	 * Convenience method that delegates to {@link DomainObjectContainer}.
	 * 
	 * @see DomainObjectContainer#uniqueMatch(Query)
	 */
	protected <T> T uniqueMatch(final Query<T> query) {
		return getContainer().uniqueMatch(query);
	}
	// }}


    // {{ isValid, validate
	/**
	 * Convenience methods that delegates to {@link DomainObjectContainer}.
	 * 
	 * @see DomainObjectContainer#isValid(Object)
	 */
    protected boolean isValid(Object domainObject) {
    	return getContainer().isValid(domainObject);
    }
	/**
	 * Convenience methods that delegates to {@link DomainObjectContainer}.
	 * 
	 * @see DomainObjectContainer#validate(Object)
	 */
    protected String validate(Object domainObject) {
    	return getContainer().validate(domainObject);
    }
    // }}

	// {{ isPersistent, persist, remove
    /**
     * Whether the provided object is persistent.
     */
    @Hidden
    protected boolean isPersistent(final Object domainObject) {
        return getContainer().isPersistent(domainObject);
    }

    /**
     * Save provided object to the persistent object store.
     * 
     * <p>
     * If the object {@link #isPersistent(Object) is persistent} already, then
     * will throw an exception.
     * 
     * @see #persistIfNotAlready(Object)
     */
    @Hidden
    protected void persist(final Object transientDomainObject) {
        getContainer().persist(transientDomainObject);
    }

    /**
     * Saves the object, but only if not already {@link #isPersistent() persistent}.
     * 
     * @see #isPersistent()
     * @see #persist(Object)
     */
    @Hidden
    protected void persistIfNotAlready(final Object domainObject) {
        getContainer().persistIfNotAlready(domainObject);
    }

    /**
     * Delete the provided object from the persistent object store.
     */
    protected void remove(final Object persistentDomainObject) {
        getContainer().remove(persistentDomainObject);
    }
	// }}
	
	
	
    // {{ Error/Warning Methods
    /**
     * Display the specified message to the user, in a non-intrusive fashion.
     */
    protected void informUser(final String message) {
        getContainer().informUser(message);
    }

    /**
     * Display the specified message as a warning to the user, in a more visible fashion, but without
     * requiring explicit acknowledgement.
     */
    protected void warnUser(final String message) {
        getContainer().warnUser(message);
    }

    /**
     * Display the specified message as an error to the user, ensuring that it is acknowledged.
     */
    protected void raiseError(final String message) {
        getContainer().raiseError(message);
    }
    // }}

    // {{ Security Methods
    protected UserMemento getUser() {
        return getContainer().getUser();
    }
    // }}
    
    
    // {{ Injected: Container
    private DomainObjectContainer container;

    /**
     * This field is not persisted, nor displayed to the user.
     */
    @Hidden
    protected DomainObjectContainer getContainer() {
        return this.container;
    }

    /**
     * Injected by the application container itself.
     */
    public final void setContainer(final DomainObjectContainer container) {
        this.container = container;
    }
    // }}

}

// Copyright (c) Naked Objects Group Ltd.
