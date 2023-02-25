package org.nakedobjects.remoting.data.common;

import org.nakedobjects.remoting.data.Data;


/**
 * ObjectData is data transfer object that contains all the data for an object in a form that can be passed
 * over the network between a client and a server.
 */
public interface ObjectData extends ReferenceData {
    Data[] getFieldContent();

    boolean hasCompleteData();

    void setFieldContent(Data[] fieldContent);
}
// Copyright (c) Naked Objects Group Ltd.
