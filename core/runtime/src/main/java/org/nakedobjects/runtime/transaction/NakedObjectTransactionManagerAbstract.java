package org.nakedobjects.runtime.transaction;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.runtime.session.NakedObjectSession;
import org.nakedobjects.runtime.transaction.messagebroker.MessageBroker;
import org.nakedobjects.runtime.transaction.messagebroker.MessageBrokerDefault;
import org.nakedobjects.runtime.transaction.updatenotifier.UpdateNotifier;
import org.nakedobjects.runtime.transaction.updatenotifier.UpdateNotifierDefault;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.nakedobjects.metamodel.commons.ensure.Ensure.ensureThatState;

public abstract class NakedObjectTransactionManagerAbstract<T extends NakedObjectTransaction> implements NakedObjectTransactionManager {

    private static final Logger LOG = Logger.getLogger(NakedObjectTransactionManagerAbstract.class);

    private NakedObjectSession session;
    
    /**
     * Holds the current or most recently completed transaction.
     */
    private T transaction;

    //////////////////////////////////////////////////////////////////
    // constructor
    //////////////////////////////////////////////////////////////////

    
    public NakedObjectTransactionManagerAbstract() {
    }

    //////////////////////////////////////////////////////////////////
    // open, close
    //////////////////////////////////////////////////////////////////

    public void open() {
        ensureThatState(session, is(notNullValue()), "session is required");
    }

    public void close() {
        if (getTransaction() != null) {
            try {
                abortTransaction();
            } catch (final Exception e2) {
                LOG.error("failure during abort", e2);
            }
        }
        session = null;
    }


    ////////////////////////////////////////////////////////
    // current transaction (if any)
    ////////////////////////////////////////////////////////

    /**
     * Current transaction (if any).
     */
    public T getTransaction() {
        return transaction;
    }

    /**
     * Convenience method returning the {@link UpdateNotifier}
     * of the {@link #getTransaction() current transaction}.
     */
    protected UpdateNotifier getUpdateNotifier() {
        return getTransaction().getUpdateNotifier();
    }

    /**
     * Convenience method returning the {@link MessageBroker}
     * of the {@link #getTransaction() current transaction}.
     */
    protected MessageBroker getMessageBroker() {
        return getTransaction().getMessageBroker();
    }

    
    //////////////////////////////////////////////////////////////////
    // Transactional Execution
    //////////////////////////////////////////////////////////////////
    
	public void executeWithinTransaction(TransactionalClosure closure) {
		boolean initiallyInTransaction = inTransaction();
		if(!initiallyInTransaction) {
			startTransaction();
		}
		try {
			closure.preExecute();
			closure.execute();
			closure.onSuccess();
			if(!initiallyInTransaction) {
				endTransaction();
			}
		} catch (RuntimeException ex) {
			closure.onFailure();
			if(!initiallyInTransaction) {
			    // temp TODO fix swallowing of exception
		//	    System.out.println(ex.getMessage());
		//	    ex.printStackTrace();
			    try {
			        abortTransaction();
			    } catch (Exception e) {
			        LOG.error("Abort failure after exception", e);
	          //      System.out.println(e.getMessage());
	          //      e.printStackTrace();
			        throw new NakedObjectTransactionManagerException("Abort failure: " + e.getMessage(), ex);
                }
			}
			throw ex;
		}
	}

	public <Q> Q executeWithinTransaction(TransactionalClosureWithReturn<Q> closure) {
		boolean initiallyInTransaction = inTransaction();
		if(!initiallyInTransaction) {
			startTransaction();
		}
		try {
			closure.preExecute();
			Q retVal = closure.execute();
			closure.onSuccess();
			if(!initiallyInTransaction) {
				endTransaction();
			}
			return retVal;
		} catch (RuntimeException ex) {
			closure.onFailure();
			if(!initiallyInTransaction) {
				abortTransaction();
			}
			throw ex;
		}
	}

	public boolean inTransaction() {
		return getTransaction() != null && !getTransaction().getState().isComplete();
	}
    
    //////////////////////////////////////////////////////////////////
    // create transaction, + hooks
    //////////////////////////////////////////////////////////////////

    /**
     * Creates a new transaction and saves, to be accessible in {@link #getTransaction()}.
     */
    protected final T createTransaction() {
        this.transaction = createTransaction(createMessageBroker(), createUpdateNotifier());
        return transaction;
    }

    
    /**
     * Overridable hook.
     * 
     * <p>
     * The provided {@link MessageBroker} and {@link UpdateNotifier} are obtained from
     * the hook methods ({@link #createMessageBroker()} and {@link #createUpdateNotifier()}).
     * 
     * @see #createMessageBroker()
     * @see #createUpdateNotifier()
     */
    protected abstract T createTransaction(MessageBroker messageBroker, UpdateNotifier updateNotifier);

    /**
     * Overridable hook, used in {@link #createTransaction(MessageBroker, UpdateNotifier)
     * 
     * <p>
     * Called when a new {@link NakedObjectTransaction} is created.
     */
    protected MessageBroker createMessageBroker() {
        return new MessageBrokerDefault();
    }
    
    /**
     * Overridable hook, used in {@link #createTransaction(MessageBroker, UpdateNotifier)
     * 
     * <p>
     * Called when a new {@link NakedObjectTransaction} is created.
     */
    protected UpdateNotifier createUpdateNotifier() {
        return new UpdateNotifierDefault();
    }


    
    
    //////////////////////////////////////////////////////////////////
    // helpers
    //////////////////////////////////////////////////////////////////

    protected void ensureTransactionInProgress() {
        ensureThatState(
                getTransaction() != null && !getTransaction().getState().isComplete(), 
                is(true), "No transaction in progress");
    }

    protected void ensureTransactionNotInProgress() {
        ensureThatState(
                getTransaction() != null && !getTransaction().getState().isComplete(), 
                is(false), "Transaction in progress");
    }


    // ////////////////////////////////////////////////////////////////////
    // injectInto
    // ////////////////////////////////////////////////////////////////////

    public void injectInto(Object candidate) {
        if (NakedObjectTransactionManagerAware.class.isAssignableFrom(candidate.getClass())) {
            NakedObjectTransactionManagerAware cast = NakedObjectTransactionManagerAware.class.cast(candidate);
            cast.setTransactionManager(this);
        }
    }

    
    ////////////////////////////////////////////////////////
    // debugging
    ////////////////////////////////////////////////////////

    public void debugData(final DebugString debug) {
        debug.appendln("Transaction", getTransaction());
    }


    //////////////////////////////////////////////////////////////////
    // Dependencies (injected)
    //////////////////////////////////////////////////////////////////
    
    public NakedObjectSession getSession() {
        return session;
    }


    /**
     * Should be injected prior to {@link #open() opening}
     */
    public void setSession(NakedObjectSession session) {
        this.session = session;
    }


}


// Copyright (c) Naked Objects Group Ltd.
