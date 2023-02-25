package org.nakedobjects.remoting.data;

import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.adapter.version.Version;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.remoting.data.Data;
import org.nakedobjects.remoting.data.common.ObjectData;


public class DummyObjectData extends DummyReferenceData implements ObjectData {
    private static final long serialVersionUID = 1L;
    private Data[] fieldContent;
    private final boolean hasCompleteData;

    public DummyObjectData(final Oid oid, final String type, final boolean hasCompleteData, final Version version) {
        super(oid, type, version);
        this.hasCompleteData = hasCompleteData;
    }

    public DummyObjectData() {
        this(null, "", false, null);
    }

    public Data[] getFieldContent() {
        return fieldContent;
    }

    public boolean hasCompleteData() {
        return hasCompleteData;
    }

    public void setFieldContent(final Data[] fieldContent) {
        this.fieldContent = fieldContent;
    }

    /*
     * public String toString() { ToString str = new ToString(this); toString(str); return str.toString(); }
     */
    @Override
    protected void toString(final ToString str) {
        super.toString(str);
        str.append("resolved", hasCompleteData);
        str.append("fields", fieldContent == null ? 0 : fieldContent.length);
        /*
         * if(fieldContent == null) { str.append("fields", "none"); } else { for (int i = 0; i <
         * fieldContent.length; i++) { str.append("field" + i + ": " + fieldContent[i].); }
         */
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }

        if (super.equals(obj)) {
            if (obj instanceof DummyObjectData) {
                final DummyObjectData ref = (DummyObjectData) obj;
                if (hasCompleteData == ref.hasCompleteData) {
                    if (fieldContent == null && ref.fieldContent == null) {
                        return true;
                    }

                    if (ref.fieldContent == null) {
                        return false;
                    }

                    if (fieldContent == null && ref.fieldContent == null) {
                        return true;
                    }

                    return fieldContent != null && ref.fieldContent != null && fieldContent.length == ref.fieldContent.length;
                    /*
                     * for (int i = 0; i < fieldContent.length; i++) { if( !(fieldContent[i] == null ?
                     * ref.fieldContent[i] == null : fieldContent[i].equals(ref.fieldContent[i]))) { return
                     * false; } } return true;
                     */}
            }
        }
        return false;

    }

    public void setFieldContent(final int i, final DummyReferenceData reference) {
        fieldContent[i] = reference;
    }
}
// Copyright (c) Naked Objects Group Ltd.
