/**
 * Remoting Command API.
 * 
 * <p>
 * Defines two installers, one for client and one for server:
 * <ul>
 * <li> the {@link ClientConnectionInstaller} is an extension of {@link org.nakedobjects.runtime.persistence.PersistenceMechanismInstaller},
 *      intended to install a <tt>persistor-proxy</tt> as well as any additional {@link org.nakedobjects.metamodel.facetdecorator.FacetDecorator}s
 *      for authentication, authorisation and so forth.  The implementation must specify the marshalling mechanism (encoding, xstream etc) as
 *      well as the transport (sockets etc).
 * <li> the {@link NakedObjectsViewerInstaller} sets up a listener to run on the server,
 *      for a (marshalling, transport) combination.
 * </ul>
 */
package org.nakedobjects.runtime.remoting;

