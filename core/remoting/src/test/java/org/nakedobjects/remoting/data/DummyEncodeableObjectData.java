package org.nakedobjects.remoting.data;

import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.remoting.data.common.EncodableObjectData;


public class DummyEncodeableObjectData implements EncodableObjectData {
    private static final long serialVersionUID = 1L;
    public String value;
    public String type;

    public DummyEncodeableObjectData(final String value) {
        this(value, String.class.getName());
    }

    public DummyEncodeableObjectData(final String value, final String type) {
        super();
        this.value = value;
        this.type = type;
    }

    public String getEncodedObjectData() {
        return value;
    }

    public String getType() {
        return type;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof DummyEncodeableObjectData) {
            final DummyEncodeableObjectData ref = (DummyEncodeableObjectData) obj;
            return (value == null ? ref.value == null : value.equals(ref.value))
                    && (type == null ? ref.type == null : type.equals(ref.type));
        }
        return false;
    }

    @Override
    public String toString() {
        final ToString str = new ToString(this);
        str.append("type", type);
        str.append("value", value);
        return str.toString();
    }

}
// Copyright (c) Naked Objects Group Ltd.
