package org.nakedobjects.plugins.xml.objectstore.internal.clock;






public class TestClock implements Clock {
    long time = 0;

    public synchronized long getTime() {
        time += 1;
        return time;
    }
}
// Copyright (c) Naked Objects Group Ltd.
