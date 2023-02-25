package org.nakedobjects.metamodel.services.container;

import java.util.ArrayList;
import java.util.List;

import org.nakedobjects.applib.DomainObjectContainer;
import org.nakedobjects.applib.Filter;
import org.nakedobjects.applib.PersistFailedException;
import org.nakedobjects.applib.RepositoryException;
import org.nakedobjects.applib.query.Query;
import org.nakedobjects.applib.security.RoleMemento;
import org.nakedobjects.applib.security.UserMemento;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.ensure.Assert;
import org.nakedobjects.metamodel.consent.InteractionResult;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContextAware;
import org.nakedobjects.metamodel.services.container.query.QueryFindAllInstances;
import org.nakedobjects.metamodel.services.container.query.QueryFindByPattern;
import org.nakedobjects.metamodel.services.container.query.QueryFindByTitle;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.util.NakedObjectUtils;


public class DomainObjectContainerDefault implements DomainObjectContainer, RuntimeContextAware {
	
	private RuntimeContext runtimeContext;


    ////////////////////////////////////////////////////////////////////
    // newInstance, disposeInstance
    ////////////////////////////////////////////////////////////////////

	/**
     * @see #doCreateTransientInstance(NakedObjectSpecification)
     */
    @SuppressWarnings("unchecked")
    public <T> T newTransientInstance(final Class<T> ofClass) {
        final NakedObjectSpecification spec = getRuntimeContext().getSpecificationLoader().loadSpecification(ofClass);
        final NakedObject adapter = doCreateTransientInstance(spec);
        return (T) adapter.getObject();
    }

    /**
     * Returns a new instance of the specified class that will have been persisted.
     */
    public <T> T newPersistentInstance(final Class<T> ofClass) {
        T newInstance = newTransientInstance(ofClass);
        persist(newInstance);
        return newInstance;
    }

    /**
     * Returns a new instance of the specified class that has the sane persisted state as the specified object.
     */
    public <T> T newInstance(final Class<T> ofClass, final Object object) {
        if (isPersistent(object)) {
            return newPersistentInstance(ofClass);
        } else {
            return newTransientInstance(ofClass);
        }
    }

    /**
     * Factored out as a potential hook method for subclasses.
     */
    protected NakedObject doCreateTransientInstance(final NakedObjectSpecification spec) {
    	return getRuntimeContext().createTransientInstance(spec);
    }

    public void remove(final Object persistentObject) {
        if (persistentObject == null) {
            throw new IllegalArgumentException("Must specify a reference for disposing an object");
        }
        final NakedObject adapter = getRuntimeContext().getAdapterFor(persistentObject);
        if (!isPersistent(persistentObject)) {
            throw new RepositoryException("Object not persistent: " + adapter);
        }
        
        getRuntimeContext().remove(adapter);
    }

    ////////////////////////////////////////////////////////////////////
    // resolve, objectChanged
    ////////////////////////////////////////////////////////////////////

    public void resolve(final Object parent) {
    	runtimeContext.resolve(parent);
    }

    public void resolve(final Object parent, final Object field) {
    	runtimeContext.resolve(parent, field);
    }

    public void objectChanged(final Object object) {
    	runtimeContext.objectChanged(object);
    }

    ////////////////////////////////////////////////////////////////////
    // flush, commit
    ////////////////////////////////////////////////////////////////////

    public boolean flush() {
    	return runtimeContext.flush();
    }

    public void commit() {
    	runtimeContext.commit();
    }

    ////////////////////////////////////////////////////////////////////
    // isValid, validate
    ////////////////////////////////////////////////////////////////////


	public boolean isValid(final Object domainObject) {
		return validate(domainObject) == null;
	}

	public String validate(final Object domainObject) {
		final NakedObject adapter = getRuntimeContext().adapterFor(domainObject);
		InteractionResult validityResult = adapter.getSpecification().isValidResult(adapter);
		return validityResult.getReason();
	}


    ////////////////////////////////////////////////////////////////////
    // persistence
    ////////////////////////////////////////////////////////////////////

    public boolean isPersistent(final Object domainObject) {
        final NakedObject adapter = getRuntimeContext().adapterFor(domainObject);
        return adapter.isPersistent();
    }

    public void persist(final Object transientObject) {
        final NakedObject adapter = getRuntimeContext().getAdapterFor(transientObject);
        if (isPersistent(transientObject)) {
            throw new PersistFailedException("Object already persistent: " + adapter);
        }
        getRuntimeContext().makePersistent(adapter);
    }
    
    public void persistIfNotAlready(final Object object) {
        if (!isPersistent(object)) {
            persist(object);
        }
    }

    ////////////////////////////////////////////////////////////////////
    // security
    ////////////////////////////////////////////////////////////////////

    public UserMemento getUser() {
        final AuthenticationSession session = getRuntimeContext().getAuthenticationSession();

        final String name = session.getUserName();
        final List<RoleMemento> roleMementos = asRoleMementos(session.getRoles());

        final UserMemento user = new UserMemento(name, roleMementos);
        return user;
    }

    private List<RoleMemento> asRoleMementos(List<String> roles) {
        List<RoleMemento> mementos = new ArrayList<RoleMemento>();
        if (roles != null) {
            for(String role: roles) {
                mementos.add(new RoleMemento(role));
            }
        }
        return mementos;
    }


    ////////////////////////////////////////////////////////////////////
    // info, warn, error messages
    ////////////////////////////////////////////////////////////////////

    public void informUser(final String message) {
        getRuntimeContext().informUser(message);
    }

    public void raiseError(final String message) {
    	getRuntimeContext().raiseError(message);
    }

    public void warnUser(final String message) {
    	getRuntimeContext().warnUser(message);
    }

    
    ////////////////////////////////////////////////////////////////////
    // allInstances, allMatches
    ////////////////////////////////////////////////////////////////////
    
	public <T> List<T> allInstances(final Class<T> type) {
        return allMatches(new QueryFindAllInstances<T>(type));
    }

	public <T> List<T> allMatches(final Class<T> cls, final Filter<T> filter) {
		final List<T> allInstances = allInstances(cls);
		final List<T> filtered = new ArrayList<T>();
		for (T instance: allInstances) {
			if (filter.accept(instance)) {
				filtered.add(instance);
			}
		}
		return filtered;
	}

	public <T> List<T> allMatches(final Class<T> type, final T pattern) {
        Assert.assertTrue("pattern not compatible with type", type.isAssignableFrom(pattern.getClass()));
        return allMatches(new QueryFindByPattern<T>(type, pattern));
    }

    public <T> List<T> allMatches(final Class<T> type, final String title) {
        return allMatches(new QueryFindByTitle<T>(type, title));
    }

    public <T> List<T> allMatches(final Query<T> query) {
        List<NakedObject> allMatching = getRuntimeContext().allMatchingQuery(query);
		return NakedObjectUtils.unwrap(allMatching);
    }

    ////////////////////////////////////////////////////////////////////
    // firstMatch
    ////////////////////////////////////////////////////////////////////

	public <T> T firstMatch(final Class<T> cls, final Filter<T> filter) {
		final List<T> allInstances = allInstances(cls);
		for (T instance: allInstances) {
			if (filter.accept(instance)) {
				return instance;
			}
		}
		return null;
	}
    
    public <T> T firstMatch(final Class<T> type, final T pattern) {
        final List<T> instances = allMatches(type, pattern);
        return firstInstanceElseNull(instances);
    }

    public <T> T firstMatch(final Class<T> type, final String title) {
        final List<T> instances = allMatches(type, title);
        return firstInstanceElseNull(instances);
    }

    @SuppressWarnings("unchecked")
	public <T> T firstMatch(final Query<T> query) {
        NakedObject firstMatching = getRuntimeContext().firstMatchingQuery(query);
        return (T) NakedObjectUtils.unwrap(firstMatching);
    }

    ////////////////////////////////////////////////////////////////////
    // uniqueMatch
    ////////////////////////////////////////////////////////////////////

	public <T> T uniqueMatch(final Class<T> type, final Filter<T> filter) {
		final List<T> instances = allMatches(type, filter);
		if (instances.size() > 1) {
			throw new RepositoryException(
					"Found more than one instance of " + type + " matching filter " + filter);
		}
		return firstInstanceElseNull(instances);
	}

    public <T> T uniqueMatch(final Class<T> type, final T pattern) {
        final List<T> instances = allMatches(type, pattern);
        if (instances.size() > 1) {
            throw new RepositoryException("Found more that one instance of " + type + " matching pattern " + pattern);
        }
        return firstInstanceElseNull(instances);
    }

    public <T> T uniqueMatch(final Class<T> type, final String title) {
        final List<T> instances = allMatches(type, title);
        if (instances.size() > 1) {
            throw new RepositoryException("Found more that one instance of " + type + " with title " + title);
        }
        return firstInstanceElseNull(instances);
    }

    public <T> T uniqueMatch(final Query<T> query) {
        final List<T> instances = allMatches(query);
        if (instances.size() > 1) {
            throw new RepositoryException("Found more that one instance for query:" + query.getDescription());
        }
        return firstInstanceElseNull(instances);
    }

    private <T> T firstInstanceElseNull(final List<T> instances) {
        return instances.size() == 0 ? null : instances.get(0);
    }


    
    ////////////////////////////////////////////////////////////////////
    // Dependencies (due to being RuntimeContextAware)
    ////////////////////////////////////////////////////////////////////
	
	/**
	 * The {@link RuntimeContext}, as provided by the constructor.
	 * 
	 * <p>
	 * Not API.
	 */
	public RuntimeContext getRuntimeContext() {
		return runtimeContext;
	}

	public void setRuntimeContext(RuntimeContext runtimeContext) {
		this.runtimeContext = runtimeContext;
		runtimeContext.setContainer(this);
	}
	



}
// Copyright (c) Naked Objects Group Ltd.
