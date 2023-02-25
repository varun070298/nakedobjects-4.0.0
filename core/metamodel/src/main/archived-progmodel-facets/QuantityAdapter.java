package org.nakedobjects.progmodel.java5.value;

import org.nakedobjects.applib.value.Quantity;
import org.nakedobjects.noa.adapter.TextEntryParseException;
import org.nakedobjects.noa.adapter.value.IntegerValue;
import org.nakedobjects.nof.core.adapter.value.AbstractValueAdapter;
import org.nakedobjects.nof.core.conf.Configuration;
import org.nakedobjects.nof.core.context.NakedObjectsContext;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;


public class QuantityAdapter extends AbstractValueAdapter implements IntegerValue {
    private static NumberFormat DEFAULT_FORMAT = NumberFormat.getNumberInstance();
    private NumberFormat format = DEFAULT_FORMAT;
    private Quantity quantity;

    public QuantityAdapter() {
        this.quantity = null;
        String formatRequired = NakedObjectsContext.getConfiguration().getString(Configuration.ROOT + "value.format.quantity");
        if(formatRequired == null) {
            format = DEFAULT_FORMAT;
        } else {
            setMask(formatRequired);
        }
    }

    public QuantityAdapter(final Quantity quantity) {
        this();
        this.quantity = quantity;
    }

    public byte[] asEncodedString() {
        String asString = Integer.toString(quantity.intValue());
        return asString.getBytes();
    }

    public String getIconName() {
        return "quantity";
    }

    public Object getObject() {
        return quantity;
    }

    public Class getValueClass() {
        return String.class;
    }

    public Integer integerValue() {
        return quantity.intValue();
    }

    public void parseTextEntry(final String entry) {
        if (entry == null || entry.trim().equals("")) {
            quantity = null;
        } else {
            try {
                int intValue = format.parse(entry).intValue();
                quantity = new Quantity(intValue);
            } catch (ParseException e) {
                throw new TextEntryParseException("Invalid number", e);
            }
        }
    }

    public void restoreFromEncodedString(final byte[] data) {
        String text = new String(data);
        int value = Integer.valueOf(text).intValue();
        quantity = new Quantity(value);
    }

    public void setMask(String mask) {
        format = new DecimalFormat(mask);
    }

    public void setValue(Integer value) {
        quantity = new Quantity(value);
    }

    public String titleString() {
        return quantity == null ? "" : format.format(quantity.intValue());
    }

    public String toString() {
        return "QunatityAdapter: " + quantity;
    }
}
// Copyright (c) Naked Objects Group Ltd.
