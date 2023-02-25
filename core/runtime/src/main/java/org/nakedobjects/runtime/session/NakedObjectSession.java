package org.nakedobjects.runtime.session;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.component.ApplicationScopedComponent;
import org.nakedobjects.metamodel.commons.component.SessionScopedComponent;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.system.NakedObjectsSystem;
import org.nakedobjects.runtime.transaction.NakedObjectTransaction;
import org.nakedobjects.runtime.userprofile.UserProfile;


/**
 * Analogous to a Hibernate <tt>Session</tt>, holds the current set of components for a 
 * specific execution context (such as on a thread).
 * 
 * <p>
 * The <tt>NakedObjectsContext</tt> class (in <tt>nof-core</tt>) is responsible for locating
 * the current execution context.
 * 
 * @see NakedObjectSessionFactory
 */
public interface NakedObjectSession extends SessionScopedComponent {

    

    ////////////////////////////////////////////////////////
    // ExecutionContextFactory
    ////////////////////////////////////////////////////////

    /**
     * The creating {@link NakedObjectSessionFactory factory}.
     * 
     * <p>
     * Note that from the factory we can {@link NakedObjectSessionFactory#getNakedObjectsSystem() get to} 
     * the {@link NakedObjectsSystem}, and thus other {@link ApplicationScopedComponent}s.
     */
    public NakedObjectSessionFactory getSessionFactory();
    

    ////////////////////////////////////////////////////////
    // closeAll
    ////////////////////////////////////////////////////////
    
    /**
     * Normal lifecycle is managed using callbacks in {@link SessionScopedComponent}.
     * This method is to allow the outer {@link ApplicationScopedComponent}s to 
     * shutdown, closing any and all running {@link NakedObjectSession}s.
     */
    public void closeAll();


    ////////////////////////////////////////////////////////
    // Id
    ////////////////////////////////////////////////////////


    /**
     * A descriptive identifier for this {@link NakedObjectSession}.
     */
    public String getId();


    ////////////////////////////////////////////////////////
    // Authentication Session
    ////////////////////////////////////////////////////////

    /**
     * Returns the {@link AuthenticationSession} representing this user 
     * for this {@link NakedObjectSession}.
     */
    public AuthenticationSession getAuthenticationSession();



    ////////////////////////////////////////////////////////
    // Persistence Session
    ////////////////////////////////////////////////////////

    /**
     * The {@link PersistenceSession} within this {@link NakedObjectSession}.
     * 
     * <p>
     * Would have been created by the {@link #getSessionFactory() owning factory}'s
     * 
     */
    public PersistenceSession getPersistenceSession();

    
    
    ////////////////////////////////////////////////////////
    // Perspective
    ////////////////////////////////////////////////////////

    /**
     * Returns the {@link NakedObject adapted} <tt>Perspective</tt> for the user 
     * who is using this {@link NakedObjectSession}.
     */

    public UserProfile getUserProfile();


    ////////////////////////////////////////////////////////
    // Transaction (if in progress)
    ////////////////////////////////////////////////////////

    public NakedObjectTransaction getCurrentTransaction();

    

    ////////////////////////////////////////////////////////
    // Debugging
    ////////////////////////////////////////////////////////

    public void debugAll(DebugString debug);

    public void debug(DebugString debug);

    public void debugState(DebugString debug);



}
// Copyright (c) Naked Objects Group Ltd.
