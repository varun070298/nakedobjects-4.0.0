package org.nakedobjects.runtime.transaction;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.nakedobjects.metamodel.commons.ensure.Ensure.ensureThatArg;
import static org.nakedobjects.metamodel.commons.ensure.Ensure.ensureThatState;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.runtime.transaction.messagebroker.MessageBroker;
import org.nakedobjects.runtime.transaction.updatenotifier.UpdateNotifier;

public abstract class NakedObjectTransactionAbstract implements NakedObjectTransaction {

    private static final Logger LOG = Logger.getLogger(NakedObjectTransactionAbstract.class);

    private final NakedObjectTransactionManager transactionManager;
    private final MessageBroker messageBroker;
    private final UpdateNotifier updateNotifier;
    
    private State state;

    private RuntimeException cause;
    
    public NakedObjectTransactionAbstract(
            final NakedObjectTransactionManager transactionManager, 
            final MessageBroker messageBroker, 
            final UpdateNotifier updateNotifier) {
        
        ensureThatArg(transactionManager, is(not(nullValue())), "transaction manager is required");
        ensureThatArg(messageBroker, is(not(nullValue())), "message broker is required");
        ensureThatArg(updateNotifier, is(not(nullValue())), "update notifier is required");
        
        this.transactionManager = transactionManager;
        this.messageBroker = messageBroker;
        this.updateNotifier = updateNotifier;
        
        this.state = State.IN_PROGRESS;
    }



    //////////////////////////////////////////////////////////////////
    // State 
    //////////////////////////////////////////////////////////////////

    public State getState() {
        return state;
    }
    
    private void setState(State state) {
        this.state = state;
    }

    
    //////////////////////////////////////////////////////////////////
    // commit, abort 
    //////////////////////////////////////////////////////////////////

    public final void flush() {
        ensureThatState(getState().canFlush(), is(true), "state is: " + getState());
        if (LOG.isInfoEnabled()) {
            LOG.info("flush transaction " + this);
        }
        
        try {
            doFlush();
        } catch(RuntimeException ex) {
            setState(State.MUST_ABORT);
            setAbortCause(ex);
            throw ex;
        }
    }


    public final void commit() {
        ensureThatState(getState().canCommit(), is(true), "state is: " + getState());

        if (LOG.isInfoEnabled()) {
            LOG.info("commit transaction " + this);
        }

        if (getState() == State.COMMITTED) {
            if (LOG.isInfoEnabled()) {
                LOG.info("already committed; ignoring");
            }
            return;
        }
        try {
            doFlush();
            setState(State.COMMITTED);
        } catch(RuntimeException ex) {
            setAbortCause(ex);
            throw ex;
        }
    }

    public final void abort() {
        ensureThatState(getState().canAbort(), is(true), "state is: " + getState());
        if (LOG.isInfoEnabled()) {
            LOG.info("abort transaction " + this);
        }

        try {
            doAbort();
        } catch(RuntimeException ex) {
            setAbortCause(ex);
            throw ex;
        } finally {
            setState(State.ABORTED);
        }
    }

    /**
     * Mandatory hook method for subclasses to persist all pending changes.
     * 
     * <p>
     * Called by both {@link #commit()} and by {@link #flush()}:
     * <table>
     * <tr>
     * <th>called from</th><th>next {@link #getState() state} if ok</th><th>next {@link #getState() state} if exception</th>
     * </tr>
     * <tr>
     * <td>{@link #commit()}</td><td>{@link State#COMMITTED}</td><td>{@link State#ABORTED}</td>
     * </tr>
     * <tr>
     * <td>{@link #flush()}</td><td>{@link State#IN_PROGRESS}</td><td>{@link State#MUST_ABORT}</td>
     * </tr>
     * </table>
     */
    protected abstract void doFlush();

    /**
     * Mandatory hook method for subclasses to perform additional processing on abort.
     * 
     * <p>
     * After this call the {@link #getState() state} will always be set to 
     * {@link State#ABORTED}, irrespective of whether an exception is thrown or not.
     */
    protected abstract void doAbort();



    //////////////////////////////////////////////////////////////////
    // Abort Cause 
    //////////////////////////////////////////////////////////////////

    protected void setAbortCause(RuntimeException cause) {
        this.cause = cause;
    }
    /**
     * The cause (if any) for the transaction being aborted.
     * 
     * <p>
     * There will be a cause if an exception is thrown either by {@link #doFlush()} or
     * {@link #doAbort()}.
     */
    public RuntimeException getAbortCause() {
        return cause;
    }



    //////////////////////////////////////////////////////////////////
    // toString 
    //////////////////////////////////////////////////////////////////
    
    @Override
    public String toString() {
        return appendTo(new ToString(this)).toString();
    }

    protected ToString appendTo(ToString str) {
        str.append("state", state);
        return str;
    }

    //////////////////////////////////////////////////////////////////
    // Depenendencies  (from constructor) 
    //////////////////////////////////////////////////////////////////

    /**
     * Injected in constructor
     */
    public NakedObjectTransactionManager getTransactionManager() {
        return transactionManager;
    }

    /**
     * Injected in constructor
     */
    public MessageBroker getMessageBroker() {
        return messageBroker;
    }
    
    /**
     * Injected in constructor
     */
    public UpdateNotifier getUpdateNotifier() {
        return updateNotifier;
    }
    

    
}


// Copyright (c) Naked Objects Group Ltd.
