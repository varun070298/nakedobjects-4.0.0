package org.nakedobjects.applib.events;

import org.nakedobjects.applib.Identifier;


/**
 * Represents an interaction with a collection object itself.
 * 
 */
public class CollectionMethodEvent extends AccessEvent {

    private static final long serialVersionUID = 1L;
    private final Object domainObject;
    private final String methodName;
    private final Object[] args;
    private final Object returnValue;

    public CollectionMethodEvent(
            final Object source,
            final Identifier collectionIdentifier,
            final Object domainObject,
            final String methodName,
            final Object[] args,
            final Object returnValue) {
        super(source, collectionIdentifier);
        this.domainObject = domainObject;
        this.methodName = methodName;
        this.args = args;
        this.returnValue = returnValue;
    }

    /**
     * The collection object (an instance of a <tt>List</tt> or a <tt>Set</tt> etc) that is the originator
     * of this event.
     * 
     * <p>
     * The owning domain object is available using {@link #getDomainObject()}.
     * 
     * @see #getDomainObject()
     */
    @Override
    public Object getSource() {
        return super.getSource();
    }

    /**
     * The owner of the collection (an instance of <tt>Customer/tt> or <tt>Order</tt>, say).
     *
     * @see #getSource()
     */
    public Object getDomainObject() {
        return domainObject;
    }

    /**
     * The name of the method invoked on this collection, for example <tt>isEmpty</tt>.
     * 
     * @return
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * The arguments with which the collection's {@link #getMethodName() method} was invoked.
     */
    public Object[] getArgs() {
        return args;
    }

    /**
     * The return value from the {@link #getMethodName() method} invocation.
     */
    public Object getReturnValue() {
        return returnValue;
    }

}
