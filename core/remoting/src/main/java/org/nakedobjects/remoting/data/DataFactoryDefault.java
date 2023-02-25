package org.nakedobjects.remoting.data;

import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.adapter.version.Version;
import org.nakedobjects.remoting.data.common.CollectionData;
import org.nakedobjects.remoting.data.common.CollectionDataImpl;
import org.nakedobjects.remoting.data.common.EncodableObjectData;
import org.nakedobjects.remoting.data.common.EncodableObjectDataImpl;
import org.nakedobjects.remoting.data.common.IdentityData;
import org.nakedobjects.remoting.data.common.IdentityDataImpl;
import org.nakedobjects.remoting.data.common.NullData;
import org.nakedobjects.remoting.data.common.NullDataImpl;
import org.nakedobjects.remoting.data.common.ObjectData;
import org.nakedobjects.remoting.data.common.ObjectDataImpl;
import org.nakedobjects.remoting.data.common.ReferenceData;

public class DataFactoryDefault implements DataFactory {

    public NullData createNullData(final String type) {
        return new NullDataImpl(type);
    }

    public IdentityData createIdentityData(final String type, final Oid oid, final Version version) {
    	return new IdentityDataImpl(type, oid, version);
    }
    
    public EncodableObjectData createValueData(final String type, final String encodedValue) {
    	return new EncodableObjectDataImpl(type, encodedValue);
    }
    
    public ObjectData createObjectData(final String type, final Oid oid, final boolean hasCompleteData, final Version version) {
        return new ObjectDataImpl(oid, type, hasCompleteData, version);
    }

    public CollectionData createCollectionData(
    		final String collectionType,
    		final String elementType,
    		final Oid oid,
    		final ReferenceData[] elements,
    		final boolean hasAllElements,
    		final Version version) {
    	return new CollectionDataImpl(oid, collectionType, elementType, elements, hasAllElements, version);
    }
    

}
// Copyright (c) Naked Objects Group Ltd.
