package org.nakedobjects.application.valueholder;

import org.nakedobjects.application.BusinessObject;
import org.nakedobjects.application.Title;
import org.nakedobjects.application.value.ValueParseException;

import java.net.MalformedURLException;
import java.net.URL;


/**
 * value object to represent an URL.
 * <p>
 * NOTE: this class currently does not support about listeners
 * </p>
 */
public class URLString extends BusinessValueHolder {
    private String urlString;

    public URLString() {
        this(null, "");
    }

    public URLString(final String urlString) {
        this(null, urlString);
    }

    public URLString(final URLString urlString) {
        this(null, urlString);
    }

    public URLString(final BusinessObject parent) {
        this(parent, "");
    }

    public URLString(final BusinessObject parent, final String urlString) {
        super(parent);
        this.urlString = urlString;
    }

    public URLString(final BusinessObject parent, final URLString urlString) {
        super(parent);
        this.urlString = new String(urlString.toString());
    }

    public void clear() {
        setValuesInternal(null, true);
    }

    /**
     * Copies the specified object's contained data to this instance. param object the object to copy the data
     * from
     */
    public void copyObject(final BusinessValueHolder object) {
        if (!(object instanceof URLString)) {
            throw new IllegalArgumentException("Can only copy the value of  a URLString object");
        }
        setValue((URLString) object);
    }

    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof URLString)) {
            return false;
        }
        URLString object = (URLString) obj;
        if (object.isEmpty() && isEmpty()) {
            return true;
        }
        return object.urlString.equals(urlString);
    }

    public String getObjectHelpText() {
        return "A URLString object.";
    }

    public boolean isEmpty() {
        ensureAtLeastPartResolved();
        return urlString == null;
    }

    /**
     * Compares the url string to see if the contain the same text if the specified object is a
     * <code>URLString</code> object else returns false.
     * 
     * @see BusinessValueHolder#isSameAs(BusinessValueHolder)
     */
    public boolean isSameAs(final BusinessValueHolder object) {
        ensureAtLeastPartResolved();
        if (object instanceof URLString) {
        	URLString other = (URLString) object;
        	if (urlString == null) {
        		return other.urlString == null;
        	}
            return urlString.equals(other.urlString);
        } else {
            return false;
        }

    }

    public void parseUserEntry(final String urlString) throws ValueParseException {
        try {
            new URL(urlString);
            setValue(urlString);
        } catch (MalformedURLException e) {
            throw new ValueParseException("Invalid URL", e);
        }
    }

    /**
     * Reset this url string so it contains an empty string, i.e. "".
     */
    public void reset() {
        setValue("");
    }

    public void restoreFromEncodedString(final String data) {
        if (data == null || data.equals("NULL")) {
            setValuesInternal(null, false);
        } else {
            setValuesInternal(data, false);
        }
    }

    public String asEncodedString() {
        return isEmpty() ? "NULL" : urlString;
    }

    public void setValue(final String urlString) {
        setValuesInternal(urlString, true);
    }

    public void setValue(final URLString urlString) {
        setValuesInternal(urlString.urlString, true);
    }

    private void setValuesInternal(final String value, final boolean notify) {
        if (notify) {
            ensureAtLeastPartResolved();
        }
        this.urlString = value;
        if (notify) {
            parentChanged();
        }
    }

    public String stringValue() {
        ensureAtLeastPartResolved();
        return urlString;
    }

    public Title title() {
        ensureAtLeastPartResolved();
        return new Title(urlString);
    }
}
// Copyright (c) Naked Objects Group Ltd.
