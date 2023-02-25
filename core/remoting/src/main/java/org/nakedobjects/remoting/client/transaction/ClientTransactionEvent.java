package org.nakedobjects.remoting.client.transaction;

import org.nakedobjects.metamodel.adapter.NakedObject;


public class ClientTransactionEvent {
    private final NakedObject object;
    private final int type;
    public static final int DELETE = 3;
    public static final int CHANGE = 2;
    public static final int ADD = 1;

    ClientTransactionEvent(final NakedObject object, final int type) {
        this.object = object;
        this.type = type;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof ClientTransactionEvent) {
            return ((ClientTransactionEvent) obj).type == type && ((ClientTransactionEvent) obj).object.equals(object);
        }

        return false;
    }

    @Override
    public int hashCode() {
        int h = 17;
        h = 37 * h + type;
        h = 37 * h + object.hashCode();
        return h;
    }

    public NakedObject getObject() {
        return object;
    }

    public int getType() {
        return type;
    }
}

// Copyright (c) Naked Objects Group Ltd.
