/**
 * (Naked Object) Adapter Factory API.
 * 
 * <p>
 * Concrete implementations are in the <tt>objectadapter-xxx</tt> modules.
 * In most scenarios the default implementation is expected to suffice.  However,
 * the API was introduced to allow custom viewers to install more sophisticated
 * adapters, specifically to support observer (MVC) patterns in the UI.
 */
package org.nakedobjects.runtime.persistence.adapterfactory;