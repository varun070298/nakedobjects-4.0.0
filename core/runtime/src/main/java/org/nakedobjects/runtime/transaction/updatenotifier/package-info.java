/**
 * Update Notifier API.
 * 
 * <p>
 * Used to collate those objects that have been modified as the result of
 * an action; these are then distributed back to the client.
 * 
 * <p>
 * Not generally intended to be implemented; the default implementation in
 * <tt>nof-core</tt> should normally suffice.  However, provides the 
 * opportunity for more exotic remoting mechanisms to send out notifications
 * of changes, for example JMS.
 * 
 * @see org.nakedobjects.runtime.transaction.messagebroker.MessageBroker
 */
package org.nakedobjects.runtime.transaction.updatenotifier;