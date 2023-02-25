package org.nakedobjects.progmodel.java5.value;

import org.nakedobjects.applib.value.MultilineString;
import org.nakedobjects.noa.adapter.value.MultilineStringValue;
import org.nakedobjects.nof.core.adapter.value.AbstractValueAdapter;
import org.nakedobjects.nof.core.util.ToString;


public class MultilineStringAdapter extends AbstractValueAdapter implements MultilineStringValue {
    private MultilineString string;

    public MultilineStringAdapter() {
        this.string = null;
    }

    public MultilineStringAdapter(final MultilineString string) {
        this.string = string;
    }

    public byte[] asEncodedString() {
        return stringValue().getBytes();
    }

    public boolean canClear() {
        return true;
    }

    public void clear() {
        string = null;
    }

    public String getIconName() {
        return "text";
    }

    public Object getObject() {
        return string;
    }

    public Class getValueClass() {
        return MultilineString.class;
    }

    public boolean isEmpty() {
        return string == null;
    }

    public void parseTextEntry(final String text) {
        string = new MultilineString(text);
    }

    public void restoreFromEncodedString(final byte[] data) {
        if (data == null) {
            string = null;
        } else {
            String text = new String(data);
            string = new MultilineString(text);
        }
    }

    public void setMask(String mask) {}

    public void setValue(final String value) {
        string = new MultilineString(value);
    }

    public String stringValue() {
        return string == null ? "" : string.getString();
    }

    public String titleString() {
        return string == null ? "" : string.getString();
    }

    public String toString() {
        ToString str = new ToString(this);
        str.append("string", string == null ? "" : string.getString());
        return str.toString();
    }
}
// Copyright (c) Naked Objects Group Ltd.
