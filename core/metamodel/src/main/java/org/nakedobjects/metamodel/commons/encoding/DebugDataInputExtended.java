package org.nakedobjects.metamodel.commons.encoding;

import java.io.IOException;

import org.apache.log4j.Logger;



public class DebugDataInputExtended extends DataInputExtendedDecorator {
	
    private static final Logger LOG = Logger.getLogger(DebugDataInputExtended.class);

    public DebugDataInputExtended(final DataInputExtended input) {
        super(input);
    }

    @Override
    public boolean readBoolean() throws IOException {
        final boolean b = super.readBoolean();
        if (LOG.isDebugEnabled()) {
        	LOG.debug("boolean: " + b);
        }
        return b;
    }

    @Override
    public byte readByte() throws IOException {
        final byte b = super.readByte();
        if (LOG.isDebugEnabled()) {
        	LOG.debug("byte: " + b);
        }
        return b;
    }

    @Override
    public byte[] readBytes() throws IOException {
        final byte[] bs = super.readBytes();
        if (LOG.isDebugEnabled()) {
        	LOG.debug("bytes: " + new String(bs));
        }
        return bs;
    }

    @Override
    public int readInt() throws IOException {
        final int i = super.readInt();
        if (LOG.isDebugEnabled()) {
        	LOG.debug("int: " + i);
        }
        return i;
    }

    @Override
    public long readLong() throws IOException {
        final long l = super.readLong();
        if (LOG.isDebugEnabled()) {
        	LOG.debug("long: " + l);
        }
        return l;
    }

    @Override
    public String readUTF() throws IOException {
        final String string = super.readUTF();
        if (LOG.isDebugEnabled()) {
        	LOG.debug("string: " + string);
        }
        return string;
    }

    @Override
    public String[] readUTFs() throws IOException {
        final String[] strings = super.readUTFs();
        if (LOG.isDebugEnabled()) {
        	LOG.debug("list: " + strings);
        }
        return strings;
    }

    @Override
    public <T> T readEncodable(Class<T> encodableType) throws IOException {
        final T object = super.readEncodable(encodableType);
        if (LOG.isDebugEnabled()) {
        	LOG.debug(">>> object");
        }
        return object;
    }

    @Override
    public <T> T[] readEncodables(Class<T> encodableType) throws IOException {
        final T[] objects = super.readEncodables(encodableType);
        if (LOG.isDebugEnabled()) {
        	LOG.debug(">>> objects x" + objects.length);
        }
        return objects;
    }



}

// Copyright (c) Naked Objects Group Ltd.
