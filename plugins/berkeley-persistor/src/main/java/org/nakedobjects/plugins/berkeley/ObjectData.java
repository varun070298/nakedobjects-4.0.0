package org.nakedobjects.plugins.berkeley;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.facets.object.encodeable.EncodableFacet;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociationContainer;
import org.nakedobjects.metamodel.spec.feature.OneToOneAssociation;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManager;
import org.nakedobjects.runtime.persistence.oidgenerator.simple.SerialOid;


public class ObjectData {
    private final NakedObject object;

    public ObjectData(NakedObject object) {
        this.object = object;
    }

    public ObjectData(byte[] data) {

        try {
            DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(data));
            String className = inputStream.readUTF();
            NakedObjectSpecification specification = NakedObjectsContext.getSpecificationLoader().loadSpecification(className);
            long id = inputStream.readLong();
            SerialOid oid = SerialOid.createPersistent(id);

            object = getAdapter(specification, oid);
            if (object.getResolveState().isResolved()) {
                // TODO - CHECK version and update
                return;
            }

            loadState(data, inputStream);
            inputStream.close();
        } catch (IOException e) {
            throw new BerkeleyObjectStoreException(e);
        }
    }

    private void loadState(byte[] data, DataInputStream inputStream) throws IOException {
        ResolveState resolveState = ResolveState.RESOLVING;
        object.changeState(resolveState);

        NakedObjectAssociationContainer specification = object.getSpecification();
        NakedObjectAssociation[] associations = specification.getAssociations();
        for (NakedObjectAssociation association : associations) {
            if (association.getSpecification().isValueOrIsAggregated()) {
                String fieldData = inputStream.readUTF();
                EncodableFacet encodeableFacet = association.getSpecification().getFacet(EncodableFacet.class);
                if (encodeableFacet != null) {
                    NakedObject value = encodeableFacet.fromEncodedString(fieldData);
                    ((OneToOneAssociation) association).set(object, value);
                } else {
                    ((OneToOneAssociation) association).set(object, null);
                }
            } else {
                NakedObject fieldObject;
                long id2 = inputStream.readLong();
                if (id2 == 0) {
                    fieldObject = null;
                } else {
                    SerialOid oid2 = SerialOid.createPersistent(id2);
                    fieldObject = getAdapter(association.getSpecification(), oid2);
                }
                ((OneToOneAssociation) association).set(object, fieldObject);
            }
        }
        object.changeState(resolveState.getEndState());
    }

    protected NakedObject getAdapter(final NakedObjectSpecification specification, final Oid oid) {
        AdapterManager objectLoader = NakedObjectsContext.getPersistenceSession().getAdapterManager();
        NakedObject adapter = objectLoader.getAdapterFor(oid);
        if (adapter != null) {
            return adapter;
        } else {
            return NakedObjectsContext.getPersistenceSession().recreateAdapter(oid, specification);
        }
    }

    public byte[] getData() {
        try {
            NakedObjectAssociation[] associations = object.getSpecification().getAssociations();
            ByteArrayOutputStream dataStream = new ByteArrayOutputStream();

            DataOutputStream outputStream = new DataOutputStream(dataStream);
            String specName = object.getSpecification().getFullName();
            outputStream.writeUTF(specName);
            long serialNo = ((SerialOid) object.getOid()).getSerialNo();
            outputStream.writeLong(serialNo);

            for (NakedObjectAssociation association : associations) {
                NakedObject field = association.get(object);
                if (association.getSpecification().isValueOrIsAggregated()) {
                    String data;
                    if (field == null) {
                        data = "";
                    } else {
                        EncodableFacet encodeableFacet = field.getSpecification().getFacet(EncodableFacet.class);
                        data = encodeableFacet.toEncodedString(field);
                    }
                    outputStream.writeUTF(data);

                } else {
                    if (field == null) {
                        outputStream.writeLong(0L);
                    } else {
                        long serialNo2 = ((SerialOid) field.getOid()).getSerialNo();
                        outputStream.writeLong(serialNo2);
                    }
                }
            }

            // TODO add version etc

            return dataStream.toByteArray();
        } catch (IOException e) {
            throw new BerkeleyObjectStoreException(e);
        }
    }

    public String getKey() {
        Oid oid = object.getOid();
        return getKey(oid);
    }

    protected static String getKey(Oid oid) {
        long serialNo = ((SerialOid) oid).getSerialNo();
        return Long.toString(serialNo);
    }

    protected static SerialOid getOid(DataInputStream inputStream) throws IOException {
        long id = inputStream.readLong();
        SerialOid oid = SerialOid.createPersistent(id);
        return oid;
    }

    public static Oid getOid(byte[] data) {
        try {
            DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(data));
            return getOid(inputStream);
        } catch (IOException e) {
            throw new BerkeleyObjectStoreException(e);
        }
    }

    public NakedObject getObject() {
        return object;
    }

    public void update(byte[] data) {
        try {
            DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(data));

            inputStream.readUTF();
            inputStream.readLong();

            loadState(data, inputStream);
            inputStream.close();
        } catch (IOException e) {
            throw new BerkeleyObjectStoreException(e);
        }
    }

}

// Copyright (c) Naked Objects Group Ltd.
