package org.nakedobjects.plugins.berkeley;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.object.encodeable.EncodableFacet;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.runtime.persistence.oidgenerator.simple.SerialOid;


public class DataFile {
    private static final int BLOCK_SIZE = 512;
    private RandomAccessFile file;
    private int nextBlock = 2;

    public static void main(String[] args) throws IOException {
        new DataFile().dump();
    }
    
    public DataFile() {
        try {
            file = new RandomAccessFile("data.txt", "rw");
            
            file.write(new byte[BLOCK_SIZE * 3]);
        } catch (IOException e) {
            throw new BerkeleyObjectStoreException(e);
        }
    }

    public void dump() throws IOException {
        System.out.println("serial number: " + loadSerialNumber());
        seekBlock(1);
        
        int i = 1;
        while(true) {
            System.out.println(i++ + file.readLong());
        }
    }
    
    public void close() {
        try {
            file.close();
        } catch (IOException e) {
            throw new BerkeleyObjectStoreException(e);
        }
    }

    public void insert(NakedObject object) {
        long serialNumber = ((SerialOid) object.getOid()).getSerialNo();
        int block = nextBlock++;
        writeObject(block, object, serialNumber);
        writeId(block, serialNumber);
    }

    private void writeId(int block, long serialNumber) {
        try {
            file.seek(BLOCK_SIZE * 1 + serialNumber * 4);
            file.writeLong(block);
        } catch (IOException e) {
            throw new BerkeleyObjectStoreException(e);
        }
    }
    private void writeObject(int block, NakedObject object, long serialNumber) {
        try {
            seekBlock(block);
            NakedObjectSpecification specification = object.getSpecification();
            ByteBuffer buffer = ByteBuffer.allocate(BLOCK_SIZE);
            buffer.put(specification.getFullName().getBytes());
            buffer.put((byte) '\n');
            buffer.put((serialNumber + "").getBytes());
            buffer.put((byte) '\n');

            NakedObjectAssociation[] associations = specification.getAssociations();
            for (NakedObjectAssociation association : associations) {
                NakedObject property = association.get(object);
                if (property != null) {
                    EncodableFacet encodable = property.getSpecification().getFacet(EncodableFacet.class);
                    if (encodable != null) {
                        String data = encodable.toEncodedString(property);
                        buffer.put(data.getBytes());
                    }
                }
                buffer.put((byte) '\n');
            }

            long length = file.length();
            file.seek(length);
            file.write(buffer.array());

        } catch (IOException e) {
            throw new BerkeleyObjectStoreException(e);
        }
    }

    public void saveSerialNumber(long serialNumber) {
        try {
            seekBlock(0);
            ByteBuffer buffer = ByteBuffer.allocate(BLOCK_SIZE);
            buffer.put(Long.toString(serialNumber).getBytes());
            buffer.put((byte) '\n');
            
            file.write(buffer.array());
        } catch (IOException e) {
            throw new BerkeleyObjectStoreException(e);
        }
    }
    
    public long loadSerialNumber() {
        try {
            seekBlock(0);
            String data = file.readLine();
            return data == null ? 1 : Long.getLong(data).longValue();
        } catch (IOException e) {
            throw new BerkeleyObjectStoreException(e);
        }
    }

    private void seekBlock(int block) throws IOException {
        file.seek(BLOCK_SIZE * block);
    }

}

// Copyright (c) Naked Objects Group Ltd.
