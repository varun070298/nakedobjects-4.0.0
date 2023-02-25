/**
 * This interface is used by the {@link org.nakedobjects.runtime.persistence.PersistenceSession} and
 * is generally not intended to be implemented directly.  
 * 
 * <p>
 * The {@link PersistAlgorithm} defines how persistence-by-reachability is enacted.  This only
 * applies to the <tt>ObjectStorePersistor</tt> implementation, but has been brought up into
 * <tt>architecture</tt> module because it is very much a peer of the other helper objects
 * that influence the {@link org.nakedobjects.runtime.persistence.PersistenceSession}'s behaviour, such
 * as {@link ClassSubstitutor} and {@link org.nakedobjects.runtime.persistence.oidgenerator.OidGenerator}. 
 * 
 * <p>
 * Since there is a close dependency between the {@link org.nakedobjects.runtime.persistence.PersistenceSession}
 * and the {@link PersistAlgorithm} implementation, it is the job of the {@link org.nakedobjects.runtime.persistence.PersistenceMechanismInstaller} to
 * ensure that the correct {@link PersistAlgorithm} is setup.
 * 
 * @see org.nakedobjects.metamodel.specloader.classsubstitutor.classsubstitor.ClassSubstitutor.ClassStrategy
 * @see org.nakedobjects.runtime.persistence.oidgenerator.OidGenerator
 */
package org.nakedobjects.runtime.persistence.objectstore.algorithm;