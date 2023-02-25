package org.nakedobjects.runtime.transaction;

import org.nakedobjects.metamodel.commons.component.Injectable;
import org.nakedobjects.metamodel.commons.component.SessionScopedComponent;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.session.NakedObjectSession;


public interface NakedObjectTransactionManager extends SessionScopedComponent, Injectable {


    //////////////////////////////////////////////////////////////////////
    // Session
    //////////////////////////////////////////////////////////////////////

    /**
     * The owning {@link NakedObjectSession}.
     *
     * <p>
     * Will be non-<tt>null</tt> when {@link #open() open}ed, but <tt>null</tt> if {@link #close() close}d .
     */
    NakedObjectSession getSession();


    //////////////////////////////////////////////////////////////////////
    // Transaction Management
    //////////////////////////////////////////////////////////////////////

    
    void startTransaction();
    
    boolean flushTransaction();

    void abortTransaction();

    /**
     * Ends the transaction if nesting level is 0. 
     */
    void endTransaction();


    /**
     * The current transaction, if any.
     */
    NakedObjectTransaction getTransaction();



    //////////////////////////////////////////////////////////////////////
    // Transactional Execution
    //////////////////////////////////////////////////////////////////////

    /**
     * Run the supplied {@link Runnable block of code (closure)} in a {@link NakedObjectTransaction transaction}.
     * 
     * <p>
     * If a transaction is {@link NakedObjectsContext#inTransaction() in progress}, then
     * uses that.  Otherwise will {@link #startTransaction() start} a transaction before
     * running the block and {@link #endTransaction() commit} it at the end.  If the
     * closure throws an exception, then will {@link #abortTransaction() abort} the transaction. 
     */
    public void executeWithinTransaction(TransactionalClosure closure);


    /**
     * Run the supplied {@link Runnable block of code (closure)} in a {@link NakedObjectTransaction transaction}.
     * 
     * <p>
     * If a transaction is {@link NakedObjectsContext#inTransaction() in progress}, then
     * uses that.  Otherwise will {@link #startTransaction() start} a transaction before
     * running the block and {@link #endTransaction() commit} it at the end.  If the
     * closure throws an exception, then will {@link #abortTransaction() abort} the transaction. 
     */
    public <T> T executeWithinTransaction(TransactionalClosureWithReturn<T> closure);

    //////////////////////////////////////////////////////////////////////
    // Debugging
    //////////////////////////////////////////////////////////////////////

    void debugData(DebugString debug);



}

// Copyright (c) Naked Objects Group Ltd.
