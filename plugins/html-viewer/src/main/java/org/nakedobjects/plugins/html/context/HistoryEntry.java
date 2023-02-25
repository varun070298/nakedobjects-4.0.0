package org.nakedobjects.plugins.html.context;

public class HistoryEntry {
    public static final int OBJECT = 1;
    public static final int COLLECTION = 2;

    public final int type;
    public final String id;

    public HistoryEntry(final String idString, final int type) {
        this.id = idString;
        this.type = type;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return ((HistoryEntry) obj).id.equals(id);
    }

    @Override
    public String toString() {
        return (type == OBJECT ? "object " : "collection ") + id;
    }

}

// Copyright (c) Naked Objects Group Ltd.
