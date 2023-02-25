package org.nakedobjects.runtime.persistence.objectstore;

import java.util.List;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.commons.component.NamedComponent;
import org.nakedobjects.metamodel.commons.component.SessionScopedComponent;
import org.nakedobjects.metamodel.commons.debug.DebugInfo;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.runtime.persistence.ObjectNotFoundException;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.persistence.PersistenceSessionFactory;
import org.nakedobjects.runtime.persistence.objectstore.transaction.CreateObjectCommand;
import org.nakedobjects.runtime.persistence.objectstore.transaction.DestroyObjectCommand;
import org.nakedobjects.runtime.persistence.objectstore.transaction.SaveObjectCommand;
import org.nakedobjects.runtime.persistence.query.PersistenceQuery;
import org.nakedobjects.runtime.transaction.PersistenceCommand;


public interface ObjectStorePersistence extends DebugInfo, SessionScopedComponent, NamedComponent {

    
    /**
     * Determine if the object store has been initialized with its set of start up objects.
     * 
     * <p>
     * This method is called only once after the {@link #init()} has been called. If it returns <code>false</code>
     * then the framework will run the fixtures to initialise the object store.
     */
    boolean isFixturesInstalled();


    /////////////////////////////////////////////////////////
    // reset
    /////////////////////////////////////////////////////////
    
    /**
     * TODO: move to {@link PersistenceSessionFactory} ??
     */
    public void reset();

    

    /////////////////////////////////////////////////////////
    // Command Creation
    /////////////////////////////////////////////////////////

    /**
     * Makes a naked object persistent. The specified object should be stored away via this object store's
     * persistence mechanism, and have an new and unique OID assigned to it (by calling the object's
     * <code>setOid</code> method). The object, should also be added to the cache as the object is
     * implicitly 'in use'.
     * 
     * <p>
     * If the object has any associations then each of these, where they aren't already persistent, should
     * also be made persistent by recursively calling this method.
     * </p>
     * 
     * <p>
     * If the object to be persisted is a collection, then each element of that collection, that is not
     * already persistent, should be made persistent by recursively calling this method.
     * </p>
     * 
     */
    CreateObjectCommand createCreateObjectCommand(NakedObject object);

    /**
     * Persists the specified object's state. Essentially the data held by the persistence mechanism should be
     * updated to reflect the state of the specified objects. Once updated, the object store should issue a
     * notification to all of the object's users via the <class>UpdateNotifier </class> object. This can be
     * achieved simply, if extending the <class> AbstractObjectStore </class> by calling its
     * <method>broadcastObjectUpdate </method> method.
     */
    SaveObjectCommand createSaveObjectCommand(NakedObject object);


    /**
     * Removes the specified object from the object store. The specified object's data should be removed from
     * the persistence mechanism and, if it is cached (which it probably is), removed from the cache also.
     */
    DestroyObjectCommand createDestroyObjectCommand(NakedObject object);


    /////////////////////////////////////////////////////////
    // Command Execution
    /////////////////////////////////////////////////////////

    void execute(final List<PersistenceCommand> commands);


    /////////////////////////////////////////////////////////
    // getObject
    /////////////////////////////////////////////////////////

    /**
     * Retrieves the object identified by the specified OID from the object store. The cache should be checked
     * first and, if the object is cached, the cached version should be returned. It is important that if this
     * method is called again, while the originally returned object is in working memory, then this method
     * must return that same Java object.
     * 
     * <p>
     * Assuming that the object is not cached then the data for the object should be retreived from the
     * persistence mechanism and the object recreated (as describe previously). The specified OID should then
     * be assigned to the recreated object by calling its <method>setOID </method>. Before returning the
     * object its resolved flag should also be set by calling its <method>setResolved </method> method as
     * well.
     * 
     * <p>If the persistence mechanism does not known of an object with the specified {@link Oid} then a
     * {@link ObjectNotFoundException} should be thrown.
     * 
     * <para>Note that the OID could be for an internal collection, and is therefore related to the parent
     * object (using a <class>CompositeOid </class>). The elements for an internal collection are commonly
     * stored as part of the parent object, so to get element the parent object needs to be retrieved first,
     * and the internal collection can be got from that. </para>
     * 
     * <p>
     * Returns the stored NakedObject object .
     * 
     * 
     * @return the requested {@link NakedObject} that has the specified {@link Oid}.
     * 
     * @throws ObjectNotFoundException
     *             when no object corresponding to the oid can be found
     */
    NakedObject getObject(Oid oid, NakedObjectSpecification hint);

    
    /////////////////////////////////////////////////////////
    // getInstances, hasInstances
    /////////////////////////////////////////////////////////

    NakedObject[] getInstances(PersistenceQuery persistenceQuery);

    boolean hasInstances(NakedObjectSpecification specification);

    
    /////////////////////////////////////////////////////////
    // resolveField, resolveImmediately
    /////////////////////////////////////////////////////////

    /**
     * Called by the resolveEagerly method in NakedObjectManager.
     * 
     * @see PersistenceSession#resolveField(NakedObject, NakedObjectAssociation)
     */
    void resolveField(NakedObject object, NakedObjectAssociation field);

    /**
     * Called by the resolveImmediately method in NakedObjectManager.
     * 
     * @see PersistenceSession#resolveImmediately(NakedObject)
     */
    void resolveImmediately(NakedObject object);
    

    /////////////////////////////////////////////////////////
    // Services
    /////////////////////////////////////////////////////////

    void registerService(String name, Oid oid);

    /**
     * Returns the OID for the adapted service.
     */
    Oid getOidForService(String name);





}
// Copyright (c) Naked Objects Group Ltd.
