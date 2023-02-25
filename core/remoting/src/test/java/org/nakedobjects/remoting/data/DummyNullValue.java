package org.nakedobjects.remoting.data;

import org.nakedobjects.remoting.data.common.NullData;





public class DummyNullValue implements NullData {
    private static final long serialVersionUID = 1L;
    private final String type;

    public DummyNullValue(final String type) {
        super();
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof DummyNullValue) {
            final DummyNullValue ref = (DummyNullValue) obj;
            return type == null ? ref.type == null : type.equals(ref.type);
        }
        return false;
    }
}
// Copyright (c) Naked Objects Group Ltd.
