/**
 * Object Persistor API.
 * 
 * <p>
 * Concrete implementations are in the <tt>persistor-xxx</tt> modules.  The 
 * role of the {@link PersistenceSession} is to manage the lifecycle of
 * domain objects, creating them, retrieving them, persisting them, deleting them.
 * However, this object management role applies when deployed in client/server mode
 * as well as standalone.
 * 
 * <p>
 * There are therefore just two implementations:
 * <ul>
 * <li> the <tt>persistor-objectstore</tt> implementation delegates to an <tt>NakedObjectStore</tt>
 *      API that actually persists objects to some persistent store (such as XML or RDBMS)</li>
 * <li> the <tt>persistor-proxy</tt> implementation in effect provides the client-side remoting library,
 *      using the remoting protocol defined in the <tt>remoting-command</tt> module.
 * </ul>
 * 
 * <p>
 * Note that the {@link PersistenceSession} both extends a number of superinterfaces as well as uses implementations of
 * various helpers (for example {@link org.nakedobjects.metamodel.services.ServicesInjector} and {@link org.nakedobjects.runtime.persistence.oidgenerator.OidGenerator}).
 * These superinterfaces and helper interfaces are not normally implemented directly, and it is the
 * responsibility of the {@link PersistenceMechanismInstaller} to ensure that the correct helper objects
 * are passed to the {@link PersistenceSession} implementation. 
 */
package org.nakedobjects.runtime.persistence;