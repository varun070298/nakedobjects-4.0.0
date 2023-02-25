package org.nakedobjects.runtime.memento;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;
import org.nakedobjects.metamodel.facets.object.encodeable.EncodableFacet;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.runtime.context.NakedObjectsContext;

public class StandaloneData extends Data {

	private static final long serialVersionUID = 1L;
	
    private static enum As {
    	ENCODED_STRING(0),
    	SERIALIZABLE(1);
    	static Map<Integer, As> cache = new HashMap<Integer, As>();
    	static {
    		for(As as: values()) {
    			cache.put(as.idx, as);
    		}
    	}
    	private final int idx;
    	private As(int idx) {
    		this.idx = idx;
    	}
    	static As get(int idx) {
    		return cache.get(idx); 
    	}
		public static As readFrom(DataInputExtended input) throws IOException {
			return get(input.readByte());
		}
		public void writeTo(DataOutputExtended output) throws IOException {
			output.writeByte(idx);
		}
    }

	
	private String objectAsEncodedString;
	private Serializable objectAsSerializable;

	public StandaloneData(NakedObject adapter) {
		super(null, adapter.getResolveState().name(), adapter.getSpecification().getFullName());
		
		Object object = adapter.getObject();
		if (object instanceof Serializable) {
			this.objectAsSerializable = (Serializable) object;
			initialized();
			return;
		}
		
		EncodableFacet encodeableFacet = adapter.getSpecification().getFacet(EncodableFacet.class);
		if (encodeableFacet != null) {
			this.objectAsEncodedString = encodeableFacet.toEncodedString(adapter);
			initialized();
			return;
		}
		
		throw new IllegalArgumentException("Object wrapped by standalone adapter is not serializable and its specificatoin does not have an EncodeableFacet");
	}
	
	public StandaloneData(DataInputExtended input) throws IOException {
		super(input);
		As as = As.readFrom(input);
		if (as == As.SERIALIZABLE) {
			this.objectAsSerializable = input.readSerializable(Serializable.class);
		} else {
			this.objectAsEncodedString = input.readUTF();
		}
		initialized();
	}

	public void encode(DataOutputExtended output) throws IOException {
		super.encode(output);
		if(objectAsSerializable != null) {
			As.SERIALIZABLE.writeTo(output);
			output.writeSerializable(objectAsSerializable);
		} else {
			As.ENCODED_STRING.writeTo(output);
			output.writeUTF(objectAsEncodedString);
		}
	}

	private void initialized() {
		// nothing to do
	}

    /////////////////////////////////////////////////////////
    //
    /////////////////////////////////////////////////////////
    


	public NakedObject getAdapter() {
		if (objectAsSerializable != null) {
			return NakedObjectsContext.getPersistenceSession().getAdapterManager().adapterFor(objectAsSerializable);
		} else {
			NakedObjectSpecification spec = NakedObjectsContext.getSpecificationLoader().loadSpecification(getClassName());
			EncodableFacet encodeableFacet = spec.getFacet(EncodableFacet.class);
			return encodeableFacet.fromEncodedString(objectAsEncodedString);
		}
	}
	/**
	 * Either this or {@link #getObjectAsSerializable()} will be non-<tt>null</tt>.
	 * @return
	 */
	private String getObjectAsEncodedString() {
		return objectAsEncodedString;
	}
	
	/**
	 * Either this or {@link #getObjectAsEncodedString()} will be non-<tt>null</tt>.
	 * @return
	 */
	private Serializable getObjectAsSerializable() {
		return objectAsSerializable;
	}

}
