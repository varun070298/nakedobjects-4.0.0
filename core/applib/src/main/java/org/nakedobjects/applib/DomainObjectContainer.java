package org.nakedobjects.applib;

import java.util.List;

import org.nakedobjects.applib.query.Query;
import org.nakedobjects.applib.security.UserMemento;



/**
 * Represents a container that the domain objects work within. It provides access to the persistence mechanism
 * and user interface.
 */
public interface DomainObjectContainer {

	//////////////////////////////////////////////////////////////////
	// resolve, objectChanged
	//////////////////////////////////////////////////////////////////

    /**
     * Ensure that the specified object is completely loaded into memory.
     * 
     * <p>
     * This forces the lazy loading mechanism to load the object if it is not already loaded.
     */
    void resolve(Object domainObject);

    /**
     * Ensure that the specified object is completely loaded into memory, though only if the supplied field
     * reference is <tt>null</tt>.
     * 
     * <p>
     * This forces the lazy loading mechanism to load the object if it is not already loaded.
     */
    void resolve(Object domainObject, Object field);

    /**
     * Flags that the specified object's state has changed and its changes need to be saved.
     */
    void objectChanged(Object domainObject);

    
	//////////////////////////////////////////////////////////////////
	// flush, commit
	//////////////////////////////////////////////////////////////////

    /**
     * Flush all changes to the object store.
     * 
     * <p>
     * Typically only for use by tests.
     * 
     * @return <tt>true</tt>
     */
    boolean flush();

    /**
     * Commit all changes to the object store.
     * 
     * <p>
     * Typically only for use by tests.
     */
    void commit();

	//////////////////////////////////////////////////////////////////
	// new{Transient/Persistent}Instance
	//////////////////////////////////////////////////////////////////

    /**
     * Create a new instance of the specified class, but do not persist it.
     * 
     * @see #newPersistentInstance(Class)
     */
    <T> T newTransientInstance(Class<T> ofType);

    /**
     * Returns a new instance of the specified class that will have been persisted.
     */
    <T> T newPersistentInstance(final Class<T> ofType) ;
    
    /**
     * Returns a new instance of the specified class that has the sane persisted state as the specified object.
     */
    <T> T newInstance(final Class<T> ofType, final Object object);


	//////////////////////////////////////////////////////////////////
	// isValid, validate
	//////////////////////////////////////////////////////////////////

    /**
     * Whether the object is in a valid state, that is that none of the validation of
     * properties, collections and object-level is vetoing.
     * 
     * @see #validate(Object)
     */
	boolean isValid(Object domainObject);

    /**
     * The reason, if any why the object is in a invalid state
     * 
     * <p>
     * Checks the validation of all of the properties, collections and object-level.
     * 
     * @see #isValid(Object)
     */
	String validate(Object domainObject);

	//////////////////////////////////////////////////////////////////
	// isPersistent, persist, remove
	//////////////////////////////////////////////////////////////////

    /**
     * Determines if the specified object is persistent (that it is stored permanently outside of the virtual
     * machine).
     */
    boolean isPersistent(Object domainObject);

    /**
     * Make the specified transient object persistent.
     * 
     * <p>
     * Throws an exception if object is already persistent.
     * 
     * @see #isPersistent(Object)
     * @see #persistIfNotAlready(Object)
     */
    void persist(Object transientDomainObject);

    /**
     * Make the specified object persistent if not already.
     * 
     * <p>
     * Does nothing otherwise.
     * 
     * @see #isPersistent(Object)
     * @see #persist(Object)
     */
    void persistIfNotAlready(Object domainObject);

    /**
     * Removes (deletes) the persisted object.
     * 
     * @param persistentDomainObject
     */
    void remove(Object persistentDomainObject);

    
	//////////////////////////////////////////////////////////////////
	// info, warn, error
	//////////////////////////////////////////////////////////////////

    /**
     * Make the specified message available to the user. Note this will probably be displayed in transitory
     * fashion, so is only suitable for useful but optional information.
     * 
     * @see #warnUser(String)
     * @see #raiseError(String)
     */
    void informUser(String message);

    /**
     * Warn the user about a situation with the specified message. The container should guarantee to display
     * this warning to the user.
     * 
     * @see #raiseError(String)
     * @see #informUser(String)
     */
    void warnUser(String message);

    /**
     * Notify the user of an application error with the specified message. Note this will probably be
     * displayed in an alarming fashion, so is only suitable for errors
     * 
     * @see #warnUser(String)
     * @see #informUser(String)
     */
    void raiseError(String message);

	//////////////////////////////////////////////////////////////////
	// security
	//////////////////////////////////////////////////////////////////

    /**
     * Get the details about the current user.
     */
    UserMemento getUser();

    
    
	//////////////////////////////////////////////////////////////////
	// allInstances, allMatches, firstMatch, uniqueMatch
	//////////////////////////////////////////////////////////////////

	/**
	 * Returns all the instances of the specified type (including subtypes).
	 * 
	 * <p>
	 * If there are no instances the list will be empty. This method creates a
	 * new {@link List} object each time it is called so the caller is free to
	 * use or modify the returned {@link List}, but the changes will not be
	 * reflected back to the repository.
	 * 
	 * <p>
	 * This method should only be called where the number of instances is known to
	 * be relatively low.
	 */
    public <T> List<T> allInstances(Class<T> ofType);


    
	/**
	 * Returns all the instances of the specified type (including subtypes) that
	 * the filter object accepts.
	 * 
	 * <p>
	 * If there are no instances the list will be empty. This method creates a
	 * new {@link List} object each time it is called so the caller is free to
	 * use or modify the returned {@link List}, but the changes will not be
	 * reflected back to the repository.
	 * 
	 * <p>
	 * This method is useful during exploration/prototyping, but you may want to use
	 * {@link #allMatches(Query)} for production code.
	 */
	public <T> List<T> allMatches(final Class<T> ofType, final Filter<T> filter);

	/**
	 * Returns all the instances of the specified type (including subtypes) that
	 * have the given title.
	 * 
	 * <p>
	 * If there are no instances the list will be empty. This method creates a
	 * new {@link List} object each time it is called so the caller is free to
	 * use or modify the returned {@link List}, but the changes will not be
	 * reflected back to the repository.
	 * 
	 * <p>
	 * This method is useful during exploration/prototyping, but you may want to use
	 * {@link #allMatches(Query)} for production code.
	 */
    public <T> List<T> allMatches(Class<T> ofType, String title);

	/**
	 * Returns all the instances of the specified type (including subtypes) that
	 * match the given object: where any property that is set will be tested and
	 * properties that are not set will be ignored.
	 * 
	 * <p>
	 * If there are no instances the list will be empty. This method creates a
	 * new {@link List} object each time it is called so the caller is free to
	 * use or modify the returned {@link List}, but the changes will not be
	 * reflected back to the repository.
	 * 
	 * <p>
	 * This method is useful during exploration/prototyping, but you may want to use
	 * {@link #allMatches(Query)} for production code.
	 */
    <T> List<T> allMatches(Class<T> ofType, T pattern);

	/**
	 * Returns all the instances that match the given {@link Query}.
	 * 
	 * <p>
	 * If there are no instances the list will be empty. This method creates a
	 * new {@link List} object each time it is called so the caller is free to
	 * use or modify the returned {@link List}, but the changes will not be
	 * reflected back to the repository.
	 */
    <T> List<T> allMatches(Query<T> query);

    
	/**
	 * Returns the first instance of the specified type (including subtypes)
	 * that matches the supplied {@link Filter}, or <tt>null</tt> if none.
	 * 
	 * <p>
	 * This method is useful during exploration/prototyping, but you may want to use
	 * {@link #firstMatch(Query)} for production code.
	 */
	public <T> T firstMatch(final Class<T> ofType, final Filter<T> filter);
    
	/**
	 * Returns the first instance of the specified type (including subtypes)
	 * that matches the supplied title, or <tt>null</tt> if none.
	 * 
	 * <p>
	 * This method is useful during exploration/prototyping, but you may want to use
	 * {@link #firstMatch(Query)} for production code.
	 */
    <T> T firstMatch(Class<T> ofType, String title);

	/**
	 * Returns the first instance of the specified type (including subtypes)
	 * that matches the supplied object as a pattern, or <tt>null</tt> if none.
	 * 
	 * <p>
	 * This method is useful during exploration/prototyping, but you may want to use
	 * {@link #firstMatch(Query)} for production code.
	 */
    <T> T firstMatch(Class<T> ofType, T pattern);

	/**
	 * Returns the first instance that matches the supplied query, 
	 * or <tt>null</tt> if none.
	 */
    <T> T firstMatch(Query<T> query);
    
    
	/**
	 * Find the only instance of the specified type (including subtypes) that
	 * has the specified title.
	 * 
	 * <p>
	 * If no instance is found then <tt>null</tt> will be return, while if there
	 * is more that one instances a run-time exception will be thrown.
	 * 
	 * <p>
	 * This method is useful during exploration/prototyping, but you may want to use
	 * {@link #uniqueMatch(Query)} for production code.
	 */
	public <T> T uniqueMatch(final Class<T> ofType, final Filter<T> filter);

	/**
	 * Find the only instance of the specified type (including subtypes) that
	 * has the specified title.
	 * 
	 * <p>
	 * If no instance is found then <tt>null</tt> will be returned, while if
	 * there is more that one instances a run-time exception will be thrown.
	 * 
	 * <p>
	 * This method is useful during exploration/prototyping, but you may want to use
	 * {@link #uniqueMatch(Query)} for production code.
	 */
    <T> T uniqueMatch(Class<T> ofType, String title);

	/**
	 * Find the only instance of the patterned object type (including subtypes)
	 * that matches the set fields in the pattern object: where any property
	 * that is set will be tested and properties that are not set will be
	 * ignored.
	 * 
	 * <p>
	 * If no instance is found then null will be return, while if there is more
	 * that one instances a run-time exception will be thrown.
	 * 
	 * <p>
	 * This method is useful during exploration/prototyping, but you may want to use
	 * {@link #uniqueMatch(Query)} for production code.
	 */
    <T> T uniqueMatch(Class<T> ofType, T pattern);

	/**
	 * Find the only instance that matches the provided query.
	 * 
	 * <p>
	 * If no instance is found then null will be return, while if there is more
	 * that one instances a run-time exception will be thrown.
	 */
    <T> T uniqueMatch(Query<T> query);


}
// Copyright (c) Naked Objects Group Ltd.
