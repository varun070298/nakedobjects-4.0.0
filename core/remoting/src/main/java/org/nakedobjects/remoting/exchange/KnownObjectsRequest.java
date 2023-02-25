package org.nakedobjects.remoting.exchange;

import java.util.Hashtable;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.remoting.data.common.ObjectData;


/**
 * A lookup of the objects that are part of a request or response. As only one instance of data per object
 * should be passed from node to node this object provides a way of ensuring this.
 */
public class KnownObjectsRequest {
    private final Hashtable dataToObjectMap = new Hashtable();
    private final Hashtable objectToDataMap = new Hashtable();

    public KnownObjectsRequest() {}

    public boolean containsKey(final NakedObject object) {
        return dataToObjectMap.containsKey(object);
    }

    public boolean containsKey(final ObjectData data) {
        return objectToDataMap.containsKey(data);
    }

    public ObjectData get(final NakedObject object) {
        return (ObjectData) dataToObjectMap.get(object);
    }

    public NakedObject get(final ObjectData data) {
        return (NakedObject) objectToDataMap.get(data);
    }

    public void put(final NakedObject object, final ObjectData data) {
        dataToObjectMap.put(object, data);
        objectToDataMap.put(data, object);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof KnownObjectsRequest) {
            final KnownObjectsRequest other = (KnownObjectsRequest) obj;

            return other.dataToObjectMap.equals(dataToObjectMap) && other.objectToDataMap.equals(objectToDataMap);
        }

        return false;
    }
}

// Copyright (c) Naked Objects Group Ltd.
