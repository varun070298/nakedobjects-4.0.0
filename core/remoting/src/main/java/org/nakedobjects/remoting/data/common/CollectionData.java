package org.nakedobjects.remoting.data.common;

public interface CollectionData extends ReferenceData {
    ReferenceData[] getElements();

    boolean hasAllElements();

    String getElementype();
}
// Copyright (c) Naked Objects Group Ltd.
