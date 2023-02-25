package org.nakedobjects.runtime.persistence;


/**
 * Creates a {@link PersistenceSession} on behalf of a {@link PersistenceSessionFactory}.
 */
public interface PersistenceSessionFactoryDelegate {

    
    /**
     * As per {@link PersistenceSessionFactory#createPersistenceSession()}, but
     * passing a {@link PersistenceSessionFactory} to act as the {@link PersistenceSession}'s
     * {@link PersistenceSession#getPersistenceSessionFactory() owning factory}.
     */
    PersistenceSession createPersistenceSession(PersistenceSessionFactory persistenceSessionFactory);

}


// Copyright (c) Naked Objects Group Ltd.
