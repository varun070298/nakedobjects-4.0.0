package org.nakedobjects.remoting.data.common;

import org.nakedobjects.metamodel.facets.object.encodeable.EncodableFacet;
import org.nakedobjects.remoting.data.Data;


/**
 * The data transfer object that contains the data for an encodeable object (ie one that has an
 * {@link EncodableFacet}) in a form that can be passed over the network between a client and a server.
 */
public interface EncodableObjectData extends Data {

    /**
     * Ultimately derived from
     * {@link EncodableFacet#toEncodedString(org.nakedobjects.noa.adapter.NakedObject)}.
     */
    String getEncodedObjectData();
}
// Copyright (c) Naked Objects Group Ltd.
