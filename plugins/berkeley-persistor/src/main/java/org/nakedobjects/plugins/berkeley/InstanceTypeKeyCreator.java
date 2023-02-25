package org.nakedobjects.plugins.berkeley;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.runtime.context.NakedObjectsContext;

import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.SecondaryDatabase;
import com.sleepycat.je.SecondaryKeyCreator;


public class InstanceTypeKeyCreator implements SecondaryKeyCreator {

    public boolean createSecondaryKey(SecondaryDatabase database, DatabaseEntry key, DatabaseEntry data, DatabaseEntry result)
            throws DatabaseException {
        try {

            String keyData = new String(key.getData());
            if (Character.isDigit(keyData.charAt(0))) {
                DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(data.getData()));
                String className = inputStream.readUTF();
                NakedObjectSpecification specification = NakedObjectsContext.getSpecificationLoader()
                        .loadSpecification(className);
                String secondaryKey = specification.getShortName();
                result.setData(secondaryKey.getBytes("UTF-8"));
                return true;
            } else {
                return false;
            }
        } catch (IOException willNeverOccur) {
            return false;
        }
    }

}

// Copyright (c) Naked Objects Group Ltd.
