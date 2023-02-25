package org.nakedobjects.metamodel.commons.profile;

public class ProfilerSystem {
    protected long memory() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    protected long time() {
        return System.currentTimeMillis();
    }
}
// Copyright (c) Naked Objects Group Ltd.
