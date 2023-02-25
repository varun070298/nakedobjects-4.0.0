/**
 * This interface is used by the {@link org.nakedobjects.runtime.persistence.PersistenceSession} and
 * is generally not intended to be implemented directly.  
 * 
 * <p>
 * Used by object store implementations to specify how to manufacture
 * {@link org.nakedobjects.metamodel.adapter.oid.Oid}s (permanent unique identifiers
 * for each domain object managed by Naked Objects).  For example, an
 * in-memory object store will just use a unique Id, whereas a generator
 * for Hibernate will hook into Hibernate's own identity generators.
 * 
 * <p>
 * Since there is a close dependency between the {@link org.nakedobjects.runtime.persistence.PersistenceSession}
 * and the {@link ClassSubstitutor} implementation, it is the job of the {@link org.nakedobjects.runtime.persistence.PersistenceMechanismInstaller} to
 * ensure that the correct {@link OidGenerator} is setup.
 * 
 * @see org.nakedobjects.runtime.persistence.objectstore.algorithm.PersistAlgorithm
 * @see org.nakedobjects.metamodel.specloader.classsubstitutor.classsubstitor.ClassSubstitutor.ClassStrategy
 */
package org.nakedobjects.runtime.persistence.oidgenerator;