/**
 * Interaction events, corresponding to gestures in the user interface.
 * 
 * <p>
 * The applib does not provide any means of listening to these events directly, and typically
 * domain objects would not be interested in them either.  However, they can be subscribed to
 * using the headlessviewer (which effectively provides a drop-in replacement for the
 * {@link org.nakedobjects.applib.DomainObjectContainer} that implements the <tt>HeadlessViewer</tt>
 * interface).  
 */
package org.nakedobjects.applib.events;