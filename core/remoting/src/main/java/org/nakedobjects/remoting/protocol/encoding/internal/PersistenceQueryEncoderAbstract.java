package org.nakedobjects.remoting.protocol.encoding.internal;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;
import org.nakedobjects.remoting.data.Data;
import org.nakedobjects.remoting.data.common.ObjectData;
import org.nakedobjects.remoting.data.query.PersistenceQueryData;
import org.nakedobjects.remoting.exchange.KnownObjectsRequest;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.query.PersistenceQuery;

public abstract class PersistenceQueryEncoderAbstract implements PersistenceQueryEncoder {

    private ObjectEncoderDecoder objectEncoder;

    public PersistenceQuery decode(
    		final PersistenceQueryData persistenceQueryData) {
        String typeName = persistenceQueryData.getType();
		return doDecode(loadSpecification(typeName), persistenceQueryData);
    }

    protected abstract PersistenceQuery doDecode(
	            NakedObjectSpecification specification,
	            PersistenceQueryData persistenceQueryData);

    private NakedObjectSpecification loadSpecification(String typeName) {
    	return getSpecificationLoader().loadSpecification(
    			typeName);
    }

    

    /**
     * Convenience method for any implementations that need to map over
     * {@link NakedObject}s.
     * 
     * @see #decodeObject(ObjectData)
     */
	protected ObjectData encodeObject(final NakedObject adapter) {
		// REVIEW: this implementation is a bit of a hack...
		Data[] datas = getObjectEncoder().encodeActionParameters(
				new NakedObjectSpecification[] { adapter.getSpecification() }, 
				new NakedObject[] { adapter }, 
				new KnownObjectsRequest());
		return (ObjectData) datas[0];
	}

	/**
     * Convenience method for any implementations that need to map over
     * {@link NakedObject}s.
     * 
     * @see #encodeObject(NakedObject)
	 */
	protected NakedObject decodeObject(final ObjectData objectData) {
		return getObjectEncoder().decode(objectData);
	}


    /////////////////////////////////////////////////////////////////////
    // Dependencies (injected)
    /////////////////////////////////////////////////////////////////////

    protected ObjectEncoderDecoder getObjectEncoder() {
		return objectEncoder;
	}
	public void setObjectEncoder(ObjectEncoderDecoder objectEncoder) {
		this.objectEncoder = objectEncoder;
	}
    
    /////////////////////////////////////////////////////////////////////
    // Dependencies (from context)
    /////////////////////////////////////////////////////////////////////
	
	private static SpecificationLoader getSpecificationLoader() {
		return NakedObjectsContext.getSpecificationLoader();
	}


}

// Copyright (c) Naked Objects Group Ltd.
