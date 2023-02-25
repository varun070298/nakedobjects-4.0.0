/**
 * Authentication API.
 * 
 * <p>
 * Concrete implementations are in the <tt>authentication-xxx</tt> modules.
 * <ul>
 * <li>In client/server mode (using <tt>remoting-command</tt> distribution), 
 *     just use the <tt>authentication-proxy</tt> on client side, and 
 *     specify desired authentication implementation on server.
 *     </li>
 * <li>In standalone mode, ignore the proxy authentication use the required 
 *     authentication implementation directly.</li>
 * </ul>
 */
package org.nakedobjects.runtime.authentication;