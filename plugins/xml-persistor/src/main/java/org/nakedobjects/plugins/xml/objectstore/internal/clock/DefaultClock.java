package org.nakedobjects.plugins.xml.objectstore.internal.clock;



public class DefaultClock implements Clock {
    public long getTime() {
        return System.currentTimeMillis();
    }

}
// Copyright (c) Naked Objects Group Ltd.
