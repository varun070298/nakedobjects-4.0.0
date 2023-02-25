/**
 * Authorization API..
 * 
 * <p>
 * Used in two different ways:
 * <ul>
 * <li>in prototyping or single user, used by the {@link org.nakedobjects.runtime.authorization.standard.AuthorizationFacetFactoryImpl facet factory}
 *     implementations, that is, enforcing authorization on the "client-side".
 *     </li>
 * <li>In client/server mode, used by a {@link org.nakedobjects.metamodel.facetdecorator.FacetDecorator} for proxy authorization,
 *     that is, enforcing authorization by delegating to the "server-side".</li>
 * </ul>
 */
package org.nakedobjects.runtime.authorization;