package org.nakedobjects.applib.events;

import org.nakedobjects.applib.Identifier;


/**
 * Represents a check as to whether a particular argument for an action is valid or not.
 * 
 * <p>
 * If {@link #getReason()} is not <tt>null</tt> then provides the reason why the argument is invalid;
 * otherwise the argument is valid.
 * 
 * <p>
 * Called once per argument, and before {@link ActionInvocationEvent}.
 */
public class ActionArgumentEvent extends ValidityEvent implements ProposedHolderEvent {

    private static final long serialVersionUID = 1L;

    private final Object[] args;
    private final int position;
    private final Object proposed;

    public ActionArgumentEvent(final Object source, final Identifier actionIdentifier, final Object[] args, final int position) {
        super(source, actionIdentifier);
        this.args = args;
        this.position = position;
        this.proposed = args[position];
    }

    public Object[] getArgs() {
        return args;
    }

    /**
     * The position (0-based) of the invalid argument.
     * 
     * @return
     */
    public int getPosition() {
        return position;
    }

    public Object getProposed() {
        return proposed;
    }

}
