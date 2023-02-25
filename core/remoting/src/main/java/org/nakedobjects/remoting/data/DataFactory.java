package org.nakedobjects.remoting.data;

import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.adapter.version.Version;
import org.nakedobjects.remoting.data.common.CollectionData;
import org.nakedobjects.remoting.data.common.EncodableObjectData;
import org.nakedobjects.remoting.data.common.IdentityData;
import org.nakedobjects.remoting.data.common.NullData;
import org.nakedobjects.remoting.data.common.ObjectData;
import org.nakedobjects.remoting.data.common.ReferenceData;
import org.nakedobjects.remoting.exchange.AuthorizationResponse;


/**
 * Create serializable objects that are used to carry messages across the network. This assumes that the Oid
 * and Version implementations are also serializable.
 */
public interface DataFactory {

    CollectionData createCollectionData(
            String collectionType,
            String elementType,
            Oid oid,
            ReferenceData[] elements,
            boolean hasAllElements,
            Version version);

    NullData createNullData(String type);

    ObjectData createObjectData(String type, Oid oid, boolean hasCompleteData, Version version);

    IdentityData createIdentityData(String type, Oid oid, Version version);

    EncodableObjectData createValueData(String fullName, String encodedValue);

	
}
// Copyright (c) Naked Objects Group Ltd.
