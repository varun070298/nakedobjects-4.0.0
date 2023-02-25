package org.nakedobjects.runtime.memento;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;
import org.nakedobjects.metamodel.commons.encoding.Encodable;


class ObjectData extends Data {
	
    private static final long serialVersionUID = 7121411963269613347L;
    private final static Encodable NO_ENTRY = new Null();
    private final Map<String,Object> fields = new HashMap<String,Object>();

    private static enum As {
    	OBJECT(0),
    	NULL(1),
    	STRING(2);
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
    
    public ObjectData(final Oid oid, final String resolveState, final String className) {
        super(oid, resolveState, className);
        initialized();
    }

    public ObjectData(final DataInputExtended input) throws IOException {
        super(input);

        final int size = input.readInt();
        for (int i = 0; i < size; i++) {
            final String key = input.readUTF();
            final As as = As.readFrom(input);
            if (as == As.OBJECT) {
                final Data object = input.readEncodable(Data.class);
                fields.put(key, object);
            } else if (as == As.NULL) {
                fields.put(key, NO_ENTRY);
            } else {
                final String value = input.readUTF();
                fields.put(key, value);
            }
        }
        initialized();
    }

    @Override
    public void encode(DataOutputExtended output) throws IOException {
        super.encode(output);

        output.writeInt(fields.size());

        for (final String key: fields.keySet()) {
            final Object value = fields.get(key);

            output.writeUTF(key);
			if (value instanceof Data) {
				As.OBJECT.writeTo(output);
                output.writeEncodable(value);
            } else if (value instanceof Null) {
            	// nothing to do; if read back corresponds to NO_ENTRY
            } else {
                output.writeUTF(As.STRING.name());
                output.writeUTF((String) value);
            }
        }
    }

	private void initialized() {
		// nothing to do
	}

    /////////////////////////////////////////////////////////
    //
    /////////////////////////////////////////////////////////
    
    
    public void addField(final String fieldName, final Object entry) {
        if (fields.containsKey(fieldName)) {
            throw new IllegalArgumentException("Field already entered " + fieldName);
        }
        fields.put(fieldName, entry == null ? NO_ENTRY : entry);
    }

    public boolean containsField() {
        return fields != null && fields.size() > 0;
    }
    
    public Object getEntry(final String fieldName) {
        final Object entry = fields.get(fieldName);
        return entry == null || entry.getClass() == NO_ENTRY.getClass() ? null : entry;
    }

    @Override
    public String toString() {
        return fields.toString();
    }

    @Override
    public void debug(final DebugString debug) {
        super.debug(debug);
        for (String key: fields.keySet()) {
            final Object value = fields.get(key);

            debug.appendln(key, value);

            // TODO recurse!
        }
    }


}
// Copyright (c) Naked Objects Group Ltd.
