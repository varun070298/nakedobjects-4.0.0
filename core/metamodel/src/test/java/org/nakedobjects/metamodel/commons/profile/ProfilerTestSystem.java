package org.nakedobjects.metamodel.commons.profile;



public class ProfilerTestSystem extends ProfilerSystem {
    long[] memory = new long[] { 10000, 20300, 30000 };
    int memoryIndex = 0;
    long[] time = new long[] { 1000, 1100, 1300, 1600, 2000 };
    int timeIndex = 0;

    @Override
    protected long memory() {
        return memory[memoryIndex++];
    }

    @Override
    protected long time() {
        return time[timeIndex++];
    }
}
// Copyright (c) Naked Objects Group Ltd.
