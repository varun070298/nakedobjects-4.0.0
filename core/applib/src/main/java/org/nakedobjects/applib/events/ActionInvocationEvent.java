package org.nakedobjects.applib.events;

import org.nakedobjects.applib.Identifier;


/**
 * Represents a check as to whether a particular argument for an action is valid or not.
 * 
 * <p>
 * If {@link #getReason()} is not <tt>null</tt> then provides the reason why the set of arguments are
 * invalid; otherwise the arguments are valid.
 * 
 * <p>
 * Called after each of the {@link ActionArgumentEvent}s.
 */
public class ActionInvocationEvent extends ValidityEvent {

    private static final long serialVersionUID = 1L;

    public ActionInvocationEvent(final Object source, final Identifier actionIdentifier, final Object[] args) {
        super(source, actionIdentifier);
        this.args = args;
    }

    private Object[] args;

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(final Object[] args) {
        this.args = args;
    }

}
