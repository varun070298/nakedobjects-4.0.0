package org.nakedobjects.plugins.xml.objectstore.internal.data;

import org.nakedobjects.runtime.persistence.ObjectNotFoundException;
import org.nakedobjects.runtime.persistence.oidgenerator.simple.SerialOid;
import org.nakedobjects.runtime.transaction.ObjectPersistenceException;


public interface DataManager {

    void shutdown();

    
    /**
     * Return data for all instances that match the pattern.
     */
    public ObjectDataVector getInstances(final ObjectData pattern);
    
    
    /**
     * Return the number of instances that match the specified data
     */
    public int numberOfInstances(final ObjectData pattern);

    public Data loadData(final SerialOid oid);

    
    
    /**
     * Save the data for an object and adds the reference to a list of instances
     */
    void insertObject(ObjectData data);

    void remove(SerialOid oid) throws ObjectNotFoundException, ObjectPersistenceException;

    /**
     * Save the data for latter retrieval.
     */
    void save(Data data);

    String getDebugData();

    boolean isFixturesInstalled();
}
// Copyright (c) Naked Objects Group Ltd.
