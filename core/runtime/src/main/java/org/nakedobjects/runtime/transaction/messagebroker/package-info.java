/**
 * Message Broker API.
 * 
 * <p>
 * Used to collate messages to send back to the client.
 * 
 * <p>
 * Not generally intended to be implemented; the default implementation in
 * <tt>nof-core</tt> should normally suffice.  However, provides for the
 * opportunity for more exotic remoting mechanisms to send out warning messages,
 * for example JMS.
 * 
 * @see org.nakedobjects.runtime.transaction.updatenotifier.UpdateNotifier
 */
package org.nakedobjects.runtime.transaction.messagebroker;