package org.nakedobjects.runtime.persistence.objectstore;

import java.util.List;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.runtime.persistence.objectstore.transaction.CreateObjectCommand;
import org.nakedobjects.runtime.persistence.objectstore.transaction.DestroyObjectCommand;
import org.nakedobjects.runtime.persistence.objectstore.transaction.SaveObjectCommand;
import org.nakedobjects.runtime.persistence.query.PersistenceQuery;
import org.nakedobjects.runtime.transaction.PersistenceCommand;

/**
 * Implementation that simply delegates to underlying {@link ObjectStore}.
 * 
 * <p>
 * Useful for quickly writing decorating implementations.
 */
public abstract class NakedObjectStoreDelegating implements ObjectStore {
    
    private final ObjectStore underlying;
    private final String name;

    public NakedObjectStoreDelegating(final ObjectStore underlying, final String name) {
        this.underlying = underlying;
        this.name = name;
    }

    
    //////////////////////////////////////////////////
    // name
    //////////////////////////////////////////////////

    public String name() {
        return name + "(" + underlying.name() + ")";
    }

    //////////////////////////////////////////////////
    // init, shutdown, reset, isInitialized
    //////////////////////////////////////////////////

    public void open() {
        underlying.open();
    }

    public void close() {
        underlying.close();
    }
    
    public void reset() {
        underlying.reset();
    }

    public boolean isFixturesInstalled() {
        return underlying.isFixturesInstalled();
    }


    //////////////////////////////////////////////////
    // createXxxCommands
    //////////////////////////////////////////////////

    public CreateObjectCommand createCreateObjectCommand(NakedObject object) {
        return underlying.createCreateObjectCommand(object);
    }

    public DestroyObjectCommand createDestroyObjectCommand(NakedObject object) {
        return underlying.createDestroyObjectCommand(object);
    }

    public SaveObjectCommand createSaveObjectCommand(NakedObject object) {
        return underlying.createSaveObjectCommand(object);
    }

    //////////////////////////////////////////////////
    // execute
    //////////////////////////////////////////////////

    public void execute(final List<PersistenceCommand> commands) {
        underlying.execute(commands);
    }

    
    //////////////////////////////////////////////////
    // TransactionManagement
    //////////////////////////////////////////////////
    
    public void startTransaction() {
        underlying.startTransaction();
    }
    
    public void endTransaction() {
        underlying.endTransaction();
    }

    public void abortTransaction() {
        underlying.abortTransaction();
    }
    
    //////////////////////////////////////////////////
    // getObject, resolveImmediately, resolveField
    //////////////////////////////////////////////////

    public NakedObject getObject(Oid oid, NakedObjectSpecification hint) {
        return underlying.getObject(oid, hint);
    }

    public void resolveField(NakedObject object, NakedObjectAssociation field) {
        underlying.resolveField(object, field);
    }

    public void resolveImmediately(NakedObject object) {
        underlying.resolveImmediately(object);
    }

    
    //////////////////////////////////////////////////
    // getInstances, hasInstances
    //////////////////////////////////////////////////

    public NakedObject[] getInstances(PersistenceQuery persistenceQuery) {
        return underlying.getInstances(persistenceQuery);
    }

    public boolean hasInstances(NakedObjectSpecification specification) {
        return underlying.hasInstances(specification);
    }

        
    //////////////////////////////////////////////////
    // services
    //////////////////////////////////////////////////

    public Oid getOidForService(String name) {
        return getOidForService(name);
    }

    public void registerService(String name, Oid oid) {
        underlying.registerService(name, oid);
    }

    //////////////////////////////////////////////////
    // debug
    //////////////////////////////////////////////////

    public void debugData(DebugString debug) {
        underlying.debugData(debug);
    }

    public String debugTitle() {
        return underlying.debugTitle();
    }

}
