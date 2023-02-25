package org.nakedobjects.plugins.berkeley;

import java.util.List;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.runtime.persistence.objectstore.ObjectStore;
import org.nakedobjects.runtime.persistence.objectstore.transaction.CreateObjectCommand;
import org.nakedobjects.runtime.persistence.objectstore.transaction.DestroyObjectCommand;
import org.nakedobjects.runtime.persistence.objectstore.transaction.SaveObjectCommand;
import org.nakedobjects.runtime.persistence.oidgenerator.OidGenerator;
import org.nakedobjects.runtime.persistence.oidgenerator.simple.SimpleOidGenerator;
import org.nakedobjects.runtime.persistence.query.PersistenceQuery;
import org.nakedobjects.runtime.transaction.NakedObjectTransaction;
import org.nakedobjects.runtime.transaction.PersistenceCommand;

public class BerkeleyObjectStore implements ObjectStore {
    private static final String SERIAL_NUMBER = "serialNumber";
    private SimpleOidGenerator oidGenerator;
    private BerkleyDb db;
    private boolean isDataLoaded;
    
    public BerkeleyObjectStore(NakedObjectConfiguration configuration) {
        db = new BerkleyDb();
        
        
        db.open();
        isDataLoaded = db.containsData();
        byte[] data = db.read(SERIAL_NUMBER);
        long loadSerialNumber;
        if( data == null) {
            loadSerialNumber = 1;
        } else {
            loadSerialNumber = Long.valueOf(new String(data));
        }
        oidGenerator = new SimpleOidGenerator(loadSerialNumber);
        db.close();
    }

    public OidGenerator getOidGenerator() {
        return oidGenerator;
    }

    public CreateObjectCommand createCreateObjectCommand(final NakedObject object) {
        return new CreateObjectCommand() {
            public void execute(NakedObjectTransaction transaction) {
                db.write(new ObjectData(object));
            }

            public NakedObject onObject() {
                return object;
            }
        };
    }

    public DestroyObjectCommand createDestroyObjectCommand(final NakedObject object) {
        return new DestroyObjectCommand() {
            public void execute(NakedObjectTransaction transaction) {
                db.delete(new ObjectData(object));
            }

            public NakedObject onObject() {
                return object;
            }
        };
    }

    public SaveObjectCommand createSaveObjectCommand(final NakedObject object) {
        return new SaveObjectCommand() {
            public void execute(NakedObjectTransaction transaction) {
                db.write(new ObjectData(object));
            }

            public NakedObject onObject() {
                return object;
            }
        };
    }

    public void execute(List<PersistenceCommand> commands) {
        db.startTransaction();
        for (PersistenceCommand command : commands) {
            // TODO excute should be called with an "execution context" so commands can be run directly; there should be no need to pass in db to the command objects 
            command.execute(null);
        }
        saveOidSequence();
        db.endTransaction();
    }

    public NakedObject[] getInstances(PersistenceQuery persistenceQuery) {
        ObjectData[] data = db.getAll(persistenceQuery.getSpecification());
        NakedObject[] array = new NakedObject[data.length];
        for (int i = 0; i < array.length; i++) {
            array[i] = data[i].getObject();
        }
        return array;
    }

    public NakedObject getObject(Oid oid, NakedObjectSpecification hint) {
        return db.getObject(oid, hint).getObject();
    }

    public Oid getOidForService(String name) {
        return db.getService(name);
    }

    public boolean hasInstances(NakedObjectSpecification specification) {
        return db.hasInstances(specification);
    }

    public boolean isFixturesInstalled() {
        return isDataLoaded;
    }

    public void registerService(String name, Oid oid) {
        db.addService(name, oid);
    }

    public void reset() {}

    public void resolveField(NakedObject object, NakedObjectAssociation field) {
        NakedObject fieldValue = field.get(object);
        if (fieldValue != null) {
            resolveImmediately(fieldValue);
        }
    }

    public void resolveImmediately(NakedObject object) {
        db.update(new ObjectData(object));
    }

    public void debugData(DebugString debug) {
        // TODO show details
    }

    public String debugTitle() {
        return "Personal Object Store";
    }

    public void close() {
        db.close();
    }

    private void saveOidSequence() {
        long serialNumber = oidGenerator.getMemento().getPersistentSerialNumber();
        db.write(SERIAL_NUMBER, Long.toString(serialNumber));
    }

    public void open() {
        db.open();
    }

    public String name() {
        return "personal object store";
    }

    public void abortTransaction() {}

    public void endTransaction() {}

    public void startTransaction() {}

}


// Copyright (c) Naked Objects Group Ltd.
