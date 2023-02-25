package org.nakedobjects.progmodel.java5.value;

import org.nakedobjects.applib.value.PhoneNumber;
import org.nakedobjects.noa.adapter.value.StringValue;
import org.nakedobjects.nof.core.adapter.value.AbstractValueAdapter;


public class PhoneNumberAdapter extends AbstractValueAdapter implements StringValue {
    private PhoneNumber number;

    public PhoneNumberAdapter() {
        this.number = null;
    }

    public PhoneNumberAdapter(final PhoneNumber number) {
        this.number = number;
    }

    public byte[] asEncodedString() {
        return number.toString().getBytes();
    }

    public String getIconName() {
        return "phone-number";
    }

    public Object getObject() {
        return number;
    }

    public Class getValueClass() {
        return PhoneNumber.class;
    }

    public void parseTextEntry(final String text) {
        if (text == null || text.trim().equals("")) {
            number = null;
        } else {
            StringBuffer s = new StringBuffer(text.length());
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                if ("01234567890 .-()".indexOf(c) >= 0) {
                    s.append(c);
                }
                // TODO allow 'ext' to be part of string
            }
            number = new PhoneNumber(s.toString().trim());
        }
    }

    public void restoreFromEncodedString(final byte[] data) {
        String text = new String(data);
        number = new PhoneNumber(text);
    }

    public void setMask(String mask) {}

    public void setValue(String value) {
        number = new PhoneNumber(value);
    }

    public String stringValue() {
        return number.toString();
    }

    public String titleString() {
        return number == null ? "" : number.toString();
    }

    public String toString() {
        return "QunatityAdapter: " + number;
    }
}
// Copyright (c) Naked Objects Group Ltd.
