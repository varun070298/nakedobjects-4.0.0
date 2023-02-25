package org.nakedobjects.metamodel.commons.encoding;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.log4j.Logger;



public class DebugDataOutputExtended extends DataOutputExtendedDecorator {
	
    private static final Logger LOG = Logger.getLogger(DebugDataOutputExtended.class);

    public DebugDataOutputExtended(final DataOutputExtended underlying) {
        super(underlying);
    }

    @Override
    public void writeBoolean(final boolean flag) throws IOException {
    	if (LOG.isDebugEnabled()) {
    		LOG.debug("boolean: " + flag);
    	}
        super.writeBoolean(flag);
    }

    @Override
    public void writeBytes(final byte[] value) throws IOException {
    	if (LOG.isDebugEnabled()) {
    		LOG.debug("bytes: (" + value.length + ") " + new String(value));
    	}
    	super.writeBytes(value);
    }

    @Override
    public void writeByte(final int value) throws IOException {
    	if (LOG.isDebugEnabled()) {
    		LOG.debug("byte: " + value);
    	}
    	super.writeByte(value);
    }

    @Override
    public void writeInt(final int value) throws IOException {
    	if (LOG.isDebugEnabled()) {
    		LOG.debug("int: " + value);
    	}
    	super.writeInt(value);
    }
    
    @Override
    public void writeLong(final long value) throws IOException {
    	if (LOG.isDebugEnabled()) {
    		LOG.debug("long: " + value);
    	}
    	super.writeLong(value);
    }

    @Override
    public void writeEncodable(final Object object) throws IOException {
    	if (LOG.isDebugEnabled()) {
    		LOG.debug(">>> object: (" + object + ")");
    	}
    	super.writeEncodable(object);
    }

    @Override
    public void writeEncodables(final Object[] objects) throws IOException {
    	if (LOG.isDebugEnabled()) {
    		LOG.debug(">>> objects x" + objects.length);
    	}
    	super.writeEncodables(objects);
    }

    @Override
    public void writeUTF(final String str) throws IOException {
    	if (LOG.isDebugEnabled()) {
    		LOG.debug("string: " + str);
    	}
    	super.writeUTF(str);
    }

    @Override
    public void writeUTFs(final String[] strings) throws IOException {
    	if (LOG.isDebugEnabled()) {
	        final StringBuffer l = new StringBuffer();
	        for (int i = 0; i < strings.length; i++) {
            if (i > 0) {
                l.append(", ");
            }
	            l.append(strings[i]);
	        }
        	LOG.debug("list: " + l);
        }
        super.writeUTFs(strings);
    }


}

// Copyright (c) Naked Objects Group Ltd.
